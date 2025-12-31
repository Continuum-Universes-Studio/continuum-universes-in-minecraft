package net.continuumuniverses.item;

import net.minecraft.world.item.Item;

public class LarzikestSwordItem extends Item {

	public LarzikestSwordItem(Item.Properties properties) {
		super(
			properties.sword(
				ModToolMaterials.LARZIKEST,
				40.0f,
				-2.4F
			)
		);
	}
}
