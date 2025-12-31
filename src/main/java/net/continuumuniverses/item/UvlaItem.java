package net.continuumuniverses.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;

import net.continuumuniverses.block.UvlaPortalBlock;

public class UvlaItem extends Item {
	public UvlaItem(Item.Properties properties) {
		super(properties.rarity(Rarity.EPIC).durability(64));
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Player entity = context.getPlayer();
		BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
		ItemStack itemstack = context.getItemInHand();
		Level world = context.getLevel();
		UvlaPortalBlock.portalSpawn(world, pos);
		EquipmentSlot slot = (context.getHand() == InteractionHand.MAIN_HAND)
				? EquipmentSlot.MAINHAND
				: EquipmentSlot.OFFHAND;

		if (!entity.mayUseItemAt(pos, context.getClickedFace(), itemstack)) {
			return InteractionResult.FAIL;
		} else {
			int x = pos.getX();
			int y = pos.getY();
			int z = pos.getZ();
			boolean success = false;
			if (world.isEmptyBlock(pos) && true) {

				itemstack.hurtAndBreak(1, entity, slot);
				success = true;
			}
			return success ? InteractionResult.SUCCESS : InteractionResult.FAIL;
		}
	}
}