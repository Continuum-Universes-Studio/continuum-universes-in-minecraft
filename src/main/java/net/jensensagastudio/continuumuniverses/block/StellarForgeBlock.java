package net.jensensagastudio.continuumuniverses.block;

import net.jensensagastudio.continuumuniverses.block.entity.StellarForgeBlockEntity;
import net.jensensagastudio.continuumuniverses.screen.ModMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class StellarForgeBlock extends Block implements EntityBlock {

    public StellarForgeBlock(Properties properties) {
        super(properties);
    }


    public InteractionResult use(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit
    ) {
        if (!level.isClientSide()) {
            MenuProvider provider = state.getMenuProvider(level, pos);
            if (provider != null) {
                player.openMenu(provider);
            }
        }
        return level.isClientSide()
                ? InteractionResult.SUCCESS
                : InteractionResult.CONSUME;

    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof StellarForgeBlockEntity be ? be : null;
    }

    @Override
    public StellarForgeBlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StellarForgeBlockEntity(pos, state);
    }
}
