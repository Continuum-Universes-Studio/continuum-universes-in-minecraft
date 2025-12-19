package net.jensensagastudio.continuumuniverses.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;

import net.minecraft.world.level.block.TintedParticleLeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class UvlaLeavesBlock extends TintedParticleLeavesBlock {

	public UvlaLeavesBlock(BlockBehaviour.Properties properties) {
		super(
				0.0f,      // particle chance
				properties // your chained props can stay
		);
	}
}
