package net.continuumuniverses.item;

import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;

public class VarkestHoeItem extends HoeItem {

	public VarkestHoeItem(Item.Properties properties) {
		super(
				ModToolMaterials.VARKEST,
				-3,
				-2.5f,
				properties.fireResistant()
		);
	}
}
