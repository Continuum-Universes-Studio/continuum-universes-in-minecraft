package net.jensensagastudio.continuumuniverses.item;

import net.minecraft.world.item.Item;

public class StarSteelPickaxeItem extends Item {
    public StarSteelPickaxeItem(Properties properties) {
        super(properties.pickaxe(ModToolMaterials.STARSTEEL, 1, -2.8F));
    }
}
