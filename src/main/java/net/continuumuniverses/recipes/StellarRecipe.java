package net.continuumuniverses.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import java.util.List;

public class StellarRecipe implements Recipe<CraftingInput> {

    private final ShapedRecipePattern pattern;
    private final ItemStack result;
    private List<Ingredient> inputs;

    public StellarRecipe(ShapedRecipePattern pattern, ItemStack result) {
        this.pattern = pattern;
        this.result = result;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return pattern.matches(input);
    }


    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries)
    {
        return result;
    }


    @Override
    public boolean isSpecial() {
        return false;
    }

    @Override
    public RecipeSerializer<StellarRecipe> getSerializer() {
        return ModRecipes.STELLAR_FORGE_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends Recipe<CraftingInput>> getType() {
        return ModRecipes.STELLAR_FORGE.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(inputs);
    }


    @Override
    public RecipeBookCategory recipeBookCategory() {
        return ModRecipeBookCategory.STELLAR_FORGE.get();
    }
    public ShapedRecipePattern pattern() {
        return pattern;
    }

    public ItemStack result() {
        return result;
    }

}
