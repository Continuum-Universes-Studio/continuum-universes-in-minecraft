package net.jensensagastudio.continuumuniverses.block;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import org.apache.commons.compress.compressors.lz77support.LZ77Compressor;

public class StarSteelBlockBlock extends Block {
    public StarSteelBlockBlock(BlockBehaviour.Properties properties) {
        super(properties
                .sound(SoundType.METAL)
                .strength(5f, 6f)
                .requiresCorrectToolForDrops()
        );
    }
}
