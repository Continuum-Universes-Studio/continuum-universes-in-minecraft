package net.continuumuniverses.recipes;

import net.continuumuniverses.world.dimensions.uvla.UvlaLunarPhaseHelper;
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
        if (level == null) {
            return false;
        }
        if (!pattern.matches(input)) {
            return false;
        }
        if (isUvla(level)) {
            return UvlaLunarPhaseHelper.isKairaFull(level);
        }
        if (level.dimension().equals(Level.OVERWORLD)) {
            return level.getMoonPhase() == 0;
        }
        return false;
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

    private static boolean isUvla(Level level) {
        return level.dimension().location().getNamespace().equals("continuumuniverses")
                && level.dimension().location().getPath().equals("uvla");
    }

}
