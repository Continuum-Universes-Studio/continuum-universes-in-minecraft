package net.jensensagastudio.continuumuniverses.item;

import net.minecraft.world.item.Item;

public class VarkestSwordItem extends Item {

	public VarkestSwordItem(Item.Properties properties) {
		super(
				properties.sword(
				ModToolMaterials.VARKEST,
				120.0f,
				4.0f
				)
		);
	}
}
