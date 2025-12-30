package net.jensensagastudio.continuumuniverses.block;

import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;

public class LarzikestOreBlock extends DropExperienceBlock {
	public LarzikestOreBlock(BlockBehaviour.Properties properties,
							 IntProvider experience) {
		super(experience, properties); // ← ORDER MATTERS
	}
}