package net.jensensagastudio.continuumuniverses.item;

import net.minecraft.world.food.FoodProperties;

public class ModFoodProperties {
    public static final FoodProperties KARGOROKBLOOD_FOOD = new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).build();
    public static final FoodProperties BORGAMEAT_FOOD = new FoodProperties.Builder().nutrition(6).saturationModifier(0.6f).build();
    public static final FoodProperties COOKED_BORGAMEAT_FOOD = new FoodProperties.Builder().nutrition(10).saturationModifier(0.8f).build();
}
