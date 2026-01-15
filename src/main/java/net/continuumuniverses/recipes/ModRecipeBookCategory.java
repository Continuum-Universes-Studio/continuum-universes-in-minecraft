package net.continuumuniverses.recipes;

import net.continuumuniverses.ContinuumUniverses;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeBookCategory;

public class ModRecipeBookCategory {
    public static final RecipeBookCategory PLASMA_SMELTING = register("plasma_smelting");
    public static final RecipeBookCategory STELLAR_FORGE = register("stellar_forge");
    public static final RecipeBookCategory LUNAR_FORGE = register("lunar_forge");

    private static RecipeBookCategory register(String name) {
        return Registry.register(
                BuiltInRegistries.RECIPE_BOOK_CATEGORY,
                ResourceLocation.fromNamespaceAndPath(ContinuumUniverses.MODID, name),
                new RecipeBookCategory()
        );
    }
}
