package net.continuumuniverses.block;

import net.minecraft.world.level.block.state.BlockBehaviour;

import net.minecraft.world.level.block.TintedParticleLeavesBlock;

public class UvlaLeavesBlock extends TintedParticleLeavesBlock {

	public UvlaLeavesBlock(BlockBehaviour.Properties properties) {
		super(
				0.0f,      // particle chance
				properties // your chained props can stay
		);
	}
}
