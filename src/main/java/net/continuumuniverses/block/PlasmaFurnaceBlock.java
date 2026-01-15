package net.continuumuniverses.block;

import com.mojang.serialization.MapCodec;
import net.continuumuniverses.block.entity.ModBlockEntities;
import net.continuumuniverses.block.entity.PlasmaFurnaceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.BlockHitResult;

public class PlasmaFurnaceBlock extends AbstractFurnaceBlock {

    /* ------------------------------------------------------------
     * Codec
     * ------------------------------------------------------------ */

    public static final MapCodec<PlasmaFurnaceBlock> CODEC =
            simpleCodec(PlasmaFurnaceBlock::new);

    @Override
    protected MapCodec<? extends AbstractFurnaceBlock> codec() {
        return CODEC;
    }

    /* ------------------------------------------------------------
     * Construction
     * ------------------------------------------------------------ */

    public PlasmaFurnaceBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                        .setValue(BlockStateProperties.LIT, false)
        );
    }

    /* ------------------------------------------------------------
     * Block Entity
     * ------------------------------------------------------------ */

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PlasmaFurnaceBlockEntity(pos, state);
    }


    /* ------------------------------------------------------------
     * Interaction
     * ------------------------------------------------------------ */

    @Override
    protected void openContainer(Level level, BlockPos pos, Player player) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof PlasmaFurnaceBlockEntity furnace) {
            player.openMenu(furnace);
            player.awardStat(Stats.INTERACT_WITH_FURNACE);
        }
    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        return be instanceof MenuProvider provider ? provider : null;
    }

    @Override
    public InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hit
    ) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof PlasmaFurnaceBlockEntity) {
            if (!level.isClientSide()) {
                openContainer(level, pos, player);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
    /* ------------------------------------------------------------
     * Particles
     * ------------------------------------------------------------ */

    @Override
    public void animateTick(
            BlockState state,
            Level level,
            BlockPos pos,
            RandomSource random
    ) {
        if (!state.getValue(BlockStateProperties.LIT)) return;

        Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.4 + random.nextDouble() * 0.2;
        double z = pos.getZ() + 0.5;

        double offset = 0.52;
        double dx = 0;
        double dz = 0;

        switch (facing) {
            case NORTH -> dz = -offset;
            case SOUTH -> dz =  offset;
            case WEST  -> dx = -offset;
            case EAST  -> dx =  offset;
        }

        level.addParticle(
                ParticleTypes.END_ROD,
                x + dx, y, z + dz,
                dx * 0.1, 0.02, dz * 0.1
        );

        if (random.nextFloat() < 0.35F) {
            level.addParticle(
                    ParticleTypes.ELECTRIC_SPARK,
                    x + dx,
                    y + random.nextDouble() * 0.1,
                    z + dz,
                    dx * 0.15 + random.nextGaussian() * 0.02,
                    random.nextDouble() * 0.05,
                    dz * 0.15 + random.nextGaussian() * 0.02
            );
        }

        if (random.nextFloat() < 0.05F) {
            for (int i = 0; i < 3; i++) {
                level.addParticle(
                        ParticleTypes.CRIT,
                        x + dx, y, z + dz,
                        dx * 0.2 + random.nextGaussian() * 0.05,
                        random.nextDouble() * 0.08,
                        dz * 0.2 + random.nextGaussian() * 0.05
                );
            }
        }
    }

    /* ------------------------------------------------------------
     * Block State
     * ------------------------------------------------------------ */

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder
    ) {
        builder.add(
                BlockStateProperties.HORIZONTAL_FACING,
                BlockStateProperties.LIT
        );
    }
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createFurnaceTicker(level, type, ModBlockEntities.PLASMA_FURNACE.get());
    }



}
