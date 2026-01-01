package net.continuumuniverses.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class UvlaItem extends Item {
	public UvlaItem(Item.Properties properties) {
		super(properties.rarity(Rarity.EPIC).durability(64));
	}
}
