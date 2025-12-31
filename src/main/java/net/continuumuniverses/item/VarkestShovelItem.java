package net.continuumuniverses.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShovelItem;

public class VarkestShovelItem extends ShovelItem {

	public VarkestShovelItem(Item.Properties properties) {
		super(
				ModToolMaterials.VARKEST,
				1.5F,
				-3.0F,
				properties.fireResistant()
		);
	}
}
