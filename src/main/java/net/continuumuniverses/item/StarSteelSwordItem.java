package net.continuumuniverses.item;

import net.minecraft.world.item.Item;
public class StarSteelSwordItem extends Item {
    public StarSteelSwordItem(Properties properties) {
        super(
                properties.sword(
                ModToolMaterials.STARSTEEL,
                3.0f,
                -2.4f
                )
        );
    }
}
