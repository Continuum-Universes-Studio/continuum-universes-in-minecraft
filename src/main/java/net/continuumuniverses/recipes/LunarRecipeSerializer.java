package net.continuumuniverses.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

public class LunarRecipeSerializer implements RecipeSerializer<LunarRecipe> {
    public static final LunarRecipeSerializer INSTANCE = new LunarRecipeSerializer();
    public static final MapCodec<LunarRecipe> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    ShapedRecipePattern.MAP_CODEC.fieldOf("pattern")
                            .forGetter(LunarRecipe::pattern),
                    ItemStack.CODEC.fieldOf("result")
                            .forGetter(LunarRecipe::result)
            ).apply(instance, LunarRecipe::new));

    public MapCodec<LunarRecipe> codec() {
        return CODEC;
    }
    public StreamCodec<RegistryFriendlyByteBuf, LunarRecipe> streamCodec() {
        return StreamCodec.composite(
                ShapedRecipePattern.STREAM_CODEC,
                LunarRecipe::pattern,
                ItemStack.STREAM_CODEC,
                LunarRecipe::result,
                LunarRecipe::new
        );
    }

}

