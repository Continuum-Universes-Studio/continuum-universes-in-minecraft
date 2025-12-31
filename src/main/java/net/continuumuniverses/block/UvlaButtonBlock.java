package net.continuumuniverses.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.block.SoundType;

public class UvlaButtonBlock extends Block {
	public UvlaButtonBlock(Properties properties) {
		super(properties.sound(SoundType.WOOD).strength(0.5f).noCollision().pushReaction(PushReaction.DESTROY));
	}
}