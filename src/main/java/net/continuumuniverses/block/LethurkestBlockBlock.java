package net.continuumuniverses.block;


import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;

public class LethurkestBlockBlock extends Block {
	public LethurkestBlockBlock(BlockBehaviour.Properties properties) {
		super(properties
				.sound(SoundType.METAL)
				.strength(5f, 6f)
				.requiresCorrectToolForDrops()
				.lightLevel(state -> 15)
				.isRedstoneConductor((state, level, pos) -> true)

		);
	}
}
