package net.continuumuniverses.block;

import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class KormikestOreBlock extends DropExperienceBlock {
	public KormikestOreBlock(BlockBehaviour.Properties properties,
							 IntProvider experience) {
		super(experience, properties); // ← ORDER MATTERS
	}
}