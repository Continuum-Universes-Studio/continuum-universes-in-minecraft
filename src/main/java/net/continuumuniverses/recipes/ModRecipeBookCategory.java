package net.continuumuniverses.recipes;

import net.continuumuniverses.ContinuumUniverses;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;

public class ModRecipeBookCategory {
    public static final DeferredRegister<RecipeBookCategory> RECIPE_BOOK_CATEGORIES =
            DeferredRegister.create(Registries.RECIPE_BOOK_CATEGORY, ContinuumUniverses.MODID);

    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> PLASMA_SMELTING =
            RECIPE_BOOK_CATEGORIES.register("plasma_smelting", RecipeBookCategory::new);
    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> STELLAR_FORGE =
            RECIPE_BOOK_CATEGORIES.register("stellar_forge", RecipeBookCategory::new);
    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> LUNAR_FORGE =
            RECIPE_BOOK_CATEGORIES.register("lunar_forge", RecipeBookCategory::new);
}
