package net.continuumuniverses.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

public class AethyrilBlockBlock extends Block {
    public AethyrilBlockBlock(Properties properties) {
        super(properties
                .sound(SoundType.METAL)
                .strength(5f, 6f)
                .requiresCorrectToolForDrops()
                .lightLevel(state -> 15)
        );
    }
}
