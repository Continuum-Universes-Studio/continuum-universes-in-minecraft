package net.continuumuniverses.item;

import net.minecraft.world.item.Item;

public class VarkestPickaxeItem extends Item {

	public VarkestPickaxeItem(Item.Properties properties) {
		super(
				properties.pickaxe(
						ModToolMaterials.VARKEST,
						1.0f,
						-2.8f
				)
		);
	}
}
