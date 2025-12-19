package net.jensensagastudio.continuumuniverses.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class LarzikestBlockBlock extends Block {

	public LarzikestBlockBlock(BlockBehaviour.Properties properties) {
		super(properties
				.sound(SoundType.METAL)
				.strength(5f, 6f)
				.requiresCorrectToolForDrops()
				.lightLevel(state -> 15)

		);
	}
}
