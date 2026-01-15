package net.continuumuniverses.block;

import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.block.SoundType;

public class UvlaButtonBlock extends ButtonBlock {
	public UvlaButtonBlock(Properties properties) {
		super(BlockSetType.OAK, 30, properties.sound(SoundType.WOOD).strength(0.5f).noCollision().pushReaction(PushReaction.DESTROY));
	}
}