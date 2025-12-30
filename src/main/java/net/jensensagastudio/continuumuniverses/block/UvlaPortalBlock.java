package net.jensensagastudio.continuumuniverses.block;

import com.mojang.logging.LogUtils;
import net.jensensagastudio.continuumuniverses.ContinuumUniverses;
import net.jensensagastudio.continuumuniverses.world.teleporter.UvlaPortalShape;
import net.jensensagastudio.continuumuniverses.world.teleporter.UvlaTeleporter;
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
				.noCollision()              // ✅ lets you walk into it
				.randomTicks()
				.pushReaction(PushReaction.BLOCK)
				.strength(-1.0F)
				.sound(SoundType.GLASS)
				.lightLevel(s -> 2)
				.noLootTable()
		);

		// ✅ default blockstate must include AXIS or setValue() will crash
		this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
	}

	// ✅ Make the AXIS property actually exist on the blockstate
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
       Block survival logic (your existing method is fine)
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
       THIS is what makes stepping into the portal start the teleport
       (1.21.10 signature!)
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
			// ✅ Tell Minecraft "this entity is inside THIS portal block"
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

		BlockPos scaledPos = border.clampToBounds(entity.getX() * scale, entity.getY(), entity.getZ() * scale);

		BlockUtil.FoundRectangle portalRect =
				BlockUtil.getLargestRectangleAround(
						scaledPos,
						Direction.Axis.Y,
						21,
						Direction.Axis.X,
						21,
						p -> targetLevel.getBlockState(p).getBlock() == this
				);

		Vec3 exitPos = Vec3.atBottomCenterOf(portalRect.minCorner);

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
		return 80; // like vanilla-ish; 0 can behave oddly depending on portal logic
	}

	@Override
	public Transition getLocalTransition() {
		return Transition.NONE;
	}
}
