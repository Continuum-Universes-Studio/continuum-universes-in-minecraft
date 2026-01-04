package net.continuumuniverses.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.ItemAbilities;

public class UmdraLogBlock extends RotatedPillarBlock {

    public UmdraLogBlock(BlockBehaviour.Properties properties) {
        super(
                properties
                        .sound(SoundType.WOOD)
                        .strength(2.0F)
                        .ignitedByLava()
                        .instrument(NoteBlockInstrument.BASS)
        );
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
        return 5;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
        return 5;
    }

    @Override
    public BlockState getToolModifiedState(
            BlockState state,
            UseOnContext context,
            ItemAbility ability,
            boolean simulate
    ) {
        if (ability == ItemAbilities.AXE_STRIP &&
                context.getItemInHand().canPerformAction(ability)) {

            return ModBlocks.STRIPPED_UMDRA_LOG
                    .get()
                    .withPropertiesOf(state);
        }

        return super.getToolModifiedState(state, context, ability, simulate);
    }
}
