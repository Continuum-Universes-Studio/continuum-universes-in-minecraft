package net.jensensagastudio.continuumuniverses.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.ButtonBlock;

public class UvlaButtonBlock extends Block {
	public UvlaButtonBlock(Properties properties) {
		super(properties.sound(SoundType.WOOD).strength(0.5f).noCollision().pushReaction(PushReaction.DESTROY));
	}
}