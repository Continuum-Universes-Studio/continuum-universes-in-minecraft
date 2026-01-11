package net.continuumuniverses.block;


import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class LethurkestBlockBlock extends Block {
	public LethurkestBlockBlock(BlockBehaviour.Properties properties) {
		super(properties
				.sound(SoundType.METAL)
				.strength(50.0F, 3200.0F)
				.requiresCorrectToolForDrops()
				.lightLevel(state -> 15)
				.noTerrainParticles()
		);
	}

}