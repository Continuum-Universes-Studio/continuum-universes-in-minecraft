package net.jensensagastudio.continuumuniverses.block;

import com.mojang.logging.LogUtils;
import net.jensensagastudio.continuumuniverses.ContinuumUniverses;
import net.jensensagastudio.continuumuniverses.world.teleporter.UvlaPortalShape;
import net.jensensagastudio.continuumuniverses.world.teleporter.UvlaTeleporter;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.BlockUtil;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

import org.slf4j.Logger;
import org.jspecify.annotations.Nullable;

public class UvlaPortalBlock extends Block implements Portal {

	private static final Logger LOGGER = LogUtils.getLogger();
	public static final EnumProperty<Direction.Axis> AXIS =
			BlockStateProperties.HORIZONTAL_AXIS;

	public static final ResourceKey<Level> UVLA_DIMENSION =
			ResourceKey.create(
					Registries.DIMENSION,
					Identifier.fromNamespaceAndPath(ContinuumUniverses.MODID, "uvla")
			);

	public UvlaPortalBlock(BlockBehaviour.Properties properties) {
		super(properties
				.noOcclusion()
				.randomTicks()
				.pushReaction(PushReaction.BLOCK)
				.strength(-1.0F)
				.sound(SoundType.GLASS)
				.lightLevel(s -> 2)
				.noLootTable()
		);
	}

    /* ------------------------------------------------------------
       Portal creation
     ------------------------------------------------------------ */

	public static void portalSpawn(Level level, BlockPos pos) {
		UvlaPortalShape.findEmptyPortalShape(level, pos, Direction.Axis.X)
				.ifPresent(shape -> shape.createPortalBlocks(level));
	}

	private UvlaTeleporter getTeleporter(ServerLevel level) {
		return new UvlaTeleporter(level);
	}

    /* ------------------------------------------------------------
       Block survival logic (1.21 SIGNATURE)
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
				? super.updateShape(
				state,
				level,
				tickAccess,
				pos,
				direction,
				neighborPos,
				neighborState,
				random
		)
				: Blocks.AIR.defaultBlockState();
	}

    /* ------------------------------------------------------------
       Dimension transition
     ------------------------------------------------------------ */

	@Override
	@Nullable
	public TeleportTransition getPortalDestination(ServerLevel sourceLevel, Entity entity, BlockPos portalPos) {

		ResourceKey<Level> targetKey =
				sourceLevel.dimension() == UVLA_DIMENSION
						? Level.OVERWORLD
						: UVLA_DIMENSION;

		ServerLevel targetLevel = sourceLevel.getServer().getLevel(targetKey);
		if (targetLevel == null) return null;

		WorldBorder border = targetLevel.getWorldBorder();
		double scale = DimensionType.getTeleportationScale(
				sourceLevel.dimensionType(),
				targetLevel.dimensionType()
		);

		BlockPos scaledPos = border.clampToBounds(
				entity.getX() * scale,
				entity.getY(),
				entity.getZ() * scale
		);

		BlockUtil.FoundRectangle portalRect =
				BlockUtil.getLargestRectangleAround(
						scaledPos,
						Direction.Axis.Y,
						21,
						Direction.Axis.X,
						21,
						pos -> targetLevel.getBlockState(pos).getBlock() == this
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

    /* ------------------------------------------------------------
       Portal behavior
     ------------------------------------------------------------ */

	@Override
	public int getPortalTransitionTime(ServerLevel level, Entity entity) {
		return 0;
	}

	@Override
	public Transition getLocalTransition() {
		return Transition.NONE;
	}

    /* ------------------------------------------------------------
       Client effects
     ------------------------------------------------------------ */

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		for (int i = 0; i < 4; i++) {
			level.addParticle(
					ParticleTypes.PORTAL,
					pos.getX() + random.nextDouble(),
					pos.getY() + random.nextDouble(),
					pos.getZ() + random.nextDouble(),
					(random.nextDouble() - 0.5) * 0.5,
					(random.nextDouble() - 0.5) * 0.5,
					(random.nextDouble() - 0.5) * 0.5
			);
		}

		if (random.nextInt(110) == 0) {
			level.playLocalSound(
					pos.getX() + 0.5,
					pos.getY() + 0.5,
					pos.getZ() + 0.5,
					SoundEvents.PORTAL_AMBIENT,
					SoundSource.BLOCKS,
					0.5F,
					random.nextFloat() * 0.4F + 0.8F,
					false
			);
		}
	}
}
