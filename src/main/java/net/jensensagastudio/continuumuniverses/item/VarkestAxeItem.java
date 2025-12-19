package net.jensensagastudio.continuumuniverses.item;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;

public class VarkestAxeItem extends AxeItem {

	public VarkestAxeItem(Item.Properties properties) {
		super(
				ModToolMaterials.VARKEST,
				9.0f,
				-3.0f,
				properties.fireResistant()
		);
	}
}
