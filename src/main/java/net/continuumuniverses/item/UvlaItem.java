package net.continuumuniverses.item;

import net.continuumuniverses.world.teleporter.UvlaPortalShape;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class UvlaItem extends Item {
	public UvlaItem(Item.Properties properties) {
		super(properties.rarity(Rarity.EPIC).durability(64));
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos targetPos = context.getClickedPos().relative(context.getClickedFace());
		Direction.Axis axis = context.getClickedFace().getAxis().isHorizontal()
				? context.getClickedFace().getAxis()
				: context.getHorizontalDirection().getAxis();
		Optional<UvlaPortalShape> shape = UvlaPortalShape.findEmptyPortalShape(level, targetPos, axis);

		if (shape.isEmpty()) {
			return InteractionResult.FAIL;
		}

		if (!level.isClientSide()) {
			shape.get().createPortalBlocks(level);
			level.playSound(null, targetPos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
			ItemStack stack = context.getItemInHand();
			if (context.getPlayer() != null) {
				stack.hurtAndBreak(1, context.getPlayer(), LivingEntity.getSlotForHand(context.getHand()));
			}
		}

		return InteractionResult.sidedSuccess(level.isClientSide());
	}
}
