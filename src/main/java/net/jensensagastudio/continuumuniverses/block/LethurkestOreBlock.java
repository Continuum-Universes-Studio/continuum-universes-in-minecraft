package net.jensensagastudio.continuumuniverses.block;

import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;

public class LethurkestOreBlock extends DropExperienceBlock {
	public LethurkestOreBlock(BlockBehaviour.Properties properties,
							  IntProvider experience) {
		super(experience, properties); // ← ORDER MATTERS
	}
}