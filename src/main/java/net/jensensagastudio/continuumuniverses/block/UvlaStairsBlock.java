package net.jensensagastudio.continuumuniverses.block;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;
public class UvlaStairsBlock extends StairBlock {

    private final Supplier<BlockState> logicalBase;

    public UvlaStairsBlock(
            Supplier<BlockState> logicalBase,
            BlockBehaviour.Properties properties
    ) {
        super(Blocks.OAK_PLANKS.defaultBlockState(), properties);
        this.logicalBase = logicalBase;
    }

    public BlockState getLogicalBase() {
        return logicalBase.get();
    }
}

