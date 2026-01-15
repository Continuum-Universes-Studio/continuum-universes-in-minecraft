package net.continuumuniverses.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.List;

public class LunarRecipe implements Recipe<CraftingInput> {

    private final ShapedRecipePattern pattern;
    private final ItemStack result;
    private List<Ingredient> inputs;

    public LunarRecipe(ShapedRecipePattern pattern, ItemStack result) {
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
        return true; // keep it out of vanilla recipe book
    }

    @Override
    public RecipeSerializer<LunarRecipe> getSerializer() {
        return ModRecipes.LUNAR_FORGE_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends Recipe<CraftingInput>> getType() {
        return ModRecipes.LUNAR_FORGE.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(inputs);
    }


    @Override
    public RecipeBookCategory recipeBookCategory() {
        return ModRecipeBookCategory.LUNAR_FORGE.get();
    }
    public ShapedRecipePattern pattern() {
        return pattern;
    }

    public ItemStack result() {
        return result;
    }

}
