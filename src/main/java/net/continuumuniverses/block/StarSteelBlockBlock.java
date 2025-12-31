package net.continuumuniverses.block;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;

public class StarSteelBlockBlock extends Block {
    public StarSteelBlockBlock(BlockBehaviour.Properties properties) {
        super(properties
                .sound(SoundType.METAL)
                .strength(5f, 6f)
                .requiresCorrectToolForDrops()
        );
    }
}
