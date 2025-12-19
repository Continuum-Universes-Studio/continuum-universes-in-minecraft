package net.jensensagastudio.continuumuniverses.recipes;

import net.jensensagastudio.continuumuniverses.block.ModBlocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

public class PlasmaRecipe extends AbstractCookingRecipe {

    public PlasmaRecipe(
            String group,
            CookingBookCategory category,
            Ingredient ingredient,
            ItemStack result,
            float experience,
            int cookingTime
    ) {
        super(
                group,
                category,
                ingredient,
                result,
                experience,
                cookingTime
        );
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return ModRecipeBookCategory.PLASMA_SMELTING;
    }

    @Override
    public CookingBookCategory category() {
        return super.category();
    }

    @Override
    protected Item furnaceIcon() {
        return ModBlocks.PLASMA_FURNACE.get().asItem();
    }

    @Override
    public RecipeType<? extends AbstractCookingRecipe> getType() {
        return ModRecipes.PLASMA.get();
    }

    @Override
    public RecipeSerializer<? extends AbstractCookingRecipe> getSerializer() {
        return ModRecipes.PLASMA_SERIALIZER.get();
    }
}