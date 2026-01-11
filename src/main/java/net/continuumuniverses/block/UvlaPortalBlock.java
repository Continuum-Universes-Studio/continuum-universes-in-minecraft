package net.continuumuniverses.block;

import com.mojang.logging.LogUtils;
import net.continuumuniverses.ContinuumUniverses;
import net.continuumuniverses.world.teleporter.UvlaPortalShape;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

import javax.annotation.Nullable;

public class UvlaPortalBlock extends Block implements Portal {

	private static final Logger LOGGER = LogUtils.getLogger();

	public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;

	public static final ResourceKey<Level> UVLA_DIMENSION =
			ResourceKey.create(Registries.DIMENSION,
					ResourceLocation.fromNamespaceAndPath(ContinuumUniverses.MODID, "uvla"));

	public UvlaPortalBlock(BlockBehaviour.Properties properties) {
		super(properties
				.noOcclusion()
				.noCollision()
				.randomTicks()
				.pushReaction(PushReaction.BLOCK)
				.strength(-1.0F)
				.sound(SoundType.GLASS)
				.lightLevel(s -> 2)
				.noLootTable()
		);

		this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AXIS);
	}

    /* ------------------------------------------------------------
       Portal creation
     ------------------------------------------------------------ */

	public static void portalSpawn(Level level, BlockPos pos) {
		UvlaPortalShape.findEmptyPortalShape(level, pos, Direction.Axis.X)
				.ifPresent(shape -> shape.createPortalBlocks(level));
	}

    /* ------------------------------------------------------------
       Block survival logic
     ------------------------------------------------------------ */

	@Override
	protected BlockState updateShape(
			BlockState state,
			LevelReader level,
			ScheduledTickAccess tickAccess,
			BlockPos pos,
			Direction direction,
			BlockPos neighborPos,
			BlockState neighborState,
			RandomSource random
	) {
		Direction.Axis axis = direction.getAxis();
		Direction.Axis portalAxis = state.getValue(AXIS);
		boolean invalid = portalAxis != axis && axis.isHorizontal();

		if (!(level instanceof LevelAccessor accessor)) {
			return Blocks.AIR.defaultBlockState();
		}

		boolean stillValid =
				(!invalid && neighborState.is(this))
						|| UvlaPortalShape.findAnyShape(accessor, pos, portalAxis).isComplete();

		return stillValid
				? super.updateShape(state, level, tickAccess, pos, direction, neighborPos, neighborState, random)
				: Blocks.AIR.defaultBlockState();
	}

    /* ------------------------------------------------------------
       Entering portal
     ------------------------------------------------------------ */

	@Override
	protected void entityInside(
			BlockState state,
			Level level,
			BlockPos pos,
			Entity entity,
			InsideBlockEffectApplier applier,
			boolean intersects
	) {
		if (!level.isClientSide() && intersects) {
			entity.setAsInsidePortal(this, pos);
		}
	}

    /* ------------------------------------------------------------
       Dimension transition
     ------------------------------------------------------------ */

	@Override
	@Nullable
	public TeleportTransition getPortalDestination(ServerLevel sourceLevel, Entity entity, BlockPos portalPos) {
		ResourceKey<Level> targetKey =
				sourceLevel.dimension() == UVLA_DIMENSION ? Level.OVERWORLD : UVLA_DIMENSION;

		ServerLevel targetLevel = sourceLevel.getServer().getLevel(targetKey);
		if (targetLevel == null) return null;

		WorldBorder border = targetLevel.getWorldBorder();

		double scale = DimensionType.getTeleportationScale(sourceLevel.dimensionType(), targetLevel.dimensionType());

		BlockPos clamped = border.clampToBounds(
				entity.getX() * scale,
				entity.getY(),
				entity.getZ() * scale
		);

		BlockPos scaledPos = clamped;



		// 1) Try to find an existing portal nearby in the target dimension
		BlockUtil.FoundRectangle found = findPortalRectangleNear(targetLevel, scaledPos);
		BlockPos exitBlockPos;

		if (found != null) {
			exitBlockPos = found.minCorner;
		} else {
			// 2) No portal found: create a safe platform + spawn a portal at a safe Y
			exitBlockPos = ensurePlatformAndPortal(targetLevel, scaledPos, Direction.Axis.X);
		}

		// Put the entity in the center and slightly above ground
		Vec3 exitPos = Vec3.atBottomCenterOf(exitBlockPos).add(0, 0.1, 0);

		return new TeleportTransition(
				targetLevel,
				exitPos,
				Vec3.ZERO,
				entity.getYRot(),
				entity.getXRot(),
				ent -> {}
		);
	}

	@Override
	public int getPortalTransitionTime(ServerLevel level, Entity entity) {
		return 80;
	}

	@Override
	public Transition getLocalTransition() {
		return Transition.NONE;
	}

    /* ------------------------------------------------------------
       Helpers
     ------------------------------------------------------------ */

	@Nullable
	private BlockUtil.FoundRectangle findPortalRectangleNear(ServerLevel level, BlockPos near) {
		// Search within a small radius for any portal block and then compute its rectangle
		int radius = 64;

		BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
		for (int dx = -radius; dx <= radius; dx++) {
			for (int dz = -radius; dz <= radius; dz++) {
				int x = near.getX() + dx;
				int z = near.getZ() + dz;

				// Find surface-ish Y for scanning
				int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
				cursor.set(x, y, z);

				// Scan downward a bit to catch portals in caves/ledges
				for (int dy = 0; dy < 32; dy++) {
					BlockPos p = cursor.below(dy);
					if (level.getBlockState(p).is(this)) {
						// We found one portal block; compute the portal rectangle around it
						return BlockUtil.getLargestRectangleAround(
								p,
								Direction.Axis.Y,
								21,
								Direction.Axis.X,
								21,
								bp -> level.getBlockState(bp).is(this)
						);
					}
				}
			}
		}
		return null;
	}

	private BlockPos ensurePlatformAndPortal(ServerLevel level, BlockPos desired, Direction.Axis axis) {
		int x = desired.getX();
		int z = desired.getZ();

		// Choose a safe Y using heightmap, with a floor clamp
		int surfaceY = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
		int minY = level.getMinY() + 8;
		int y = Math.max(surfaceY, minY);

		// Center of platform
		BlockPos center = new BlockPos(x, y, z);

		// Build platform one block below standing position
		buildPlatform(level, center.below());

		// Clear headroom
		clearAir(level, center);
		clearAir(level, center.above());

		// Build a portal on the platform (you can swap frame material)
		BlockPos portalBaseInside = center.offset(0, 1, 0);
		buildPortalFrameAndFill(level, portalBaseInside, axis);

		// Return the portal “standing” location (inside portal)
		return portalBaseInside;
	}

	private void buildPlatform(ServerLevel level, BlockPos center) {
		BlockState floor = ModBlocks.LETHURKEST_BLOCK.get().defaultBlockState();

		for (int dx = -2; dx <= 2; dx++) {
			for (int dz = -2; dz <= 2; dz++) {
				level.setBlock(center.offset(dx, 0, dz), floor, 3);
			}
		}

		// Optional rails (prevents instant walking off)
		// If you want rails also made of Lethurkest, use the same block.
		// If LETHURKEST_BLOCK isn't a wall-type, a full block "rail" is fine.
		BlockState rail = floor;

		for (int dx = -2; dx <= 2; dx++) {
			for (int dz = -2; dz <= 2; dz++) {
				if (Math.abs(dx) == 2 || Math.abs(dz) == 2) {
					level.setBlock(center.offset(dx, 1, dz), rail, 3);
				}
			}
		}
	}

	private void clearAir(ServerLevel level, BlockPos pos) {
		if (!level.getBlockState(pos).isAir()) {
			level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
		}
	}

	private void buildPortalFrameAndFill(ServerLevel level, BlockPos insideBottomLeft, Direction.Axis axis) {
		// Nether-like 4x5 outer, 2x3 inner.
		// insideBottomLeft is the bottom-left block INSIDE the portal interior.

		BlockState frame = ModBlocks.LETHURKEST_BLOCK.get().defaultBlockState();
		BlockState portal = this.defaultBlockState().setValue(AXIS, axis);

		// Build frame around interior
		// Frame corners relative to interior: x:-1..2 and y:0..4 in local “width” axis
		for (int dy = 0; dy < 5; dy++) {
			setLocal(level, insideBottomLeft, axis, -1, dy, frame);
			setLocal(level, insideBottomLeft, axis,  2, dy, frame);
		}
		for (int dx = -1; dx <= 2; dx++) {
			setLocal(level, insideBottomLeft, axis, dx, 0, frame);
			setLocal(level, insideBottomLeft, axis, dx, 4, frame);
		}

		// Fill portal interior 2x3
		for (int dx = 0; dx <= 1; dx++) {
			for (int dy = 1; dy <= 3; dy++) {
				setLocal(level, insideBottomLeft, axis, dx, dy, portal);
			}
		}
	}

	private void setLocal(ServerLevel level, BlockPos originInside, Direction.Axis axis, int dx, int dy, BlockState state) {
		// If axis is X, portal plane is Z (like vanilla nether). If axis is Z, portal plane is X.
		BlockPos p = (axis == Direction.Axis.X)
				? originInside.offset(dx, dy, 0)
				: originInside.offset(0, dy, dx);
		level.setBlock(p, state, 3);
	}

}
