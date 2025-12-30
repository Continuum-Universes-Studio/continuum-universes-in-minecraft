package net.jensensagastudio.continuumuniverses.item;

import net.minecraft.world.item.Item;

public class LarzikestPickaxeItem extends Item {
	public LarzikestPickaxeItem(Item.Properties properties) {
		super(
				properties.pickaxe(
						ModToolMaterials.LARZIKEST, 1, -2.8F
				)
		);
	}
}
