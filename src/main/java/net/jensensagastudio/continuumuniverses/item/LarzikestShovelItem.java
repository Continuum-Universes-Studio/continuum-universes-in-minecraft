package net.jensensagastudio.continuumuniverses.item;

import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Item;
import net.jensensagastudio.continuumuniverses.item.ModToolMaterials;

public class LarzikestShovelItem extends ShovelItem {

	public LarzikestShovelItem(Item.Properties properties) {
		super(
				ModToolMaterials.LARZIKEST,
				1,
				-2.8f,
				properties
		);
	}
}