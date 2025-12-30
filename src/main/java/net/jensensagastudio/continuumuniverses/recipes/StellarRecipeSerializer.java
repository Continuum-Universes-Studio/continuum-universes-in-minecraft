package net.jensensagastudio.continuumuniverses.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

public class StellarRecipeSerializer implements RecipeSerializer<StellarRecipe> {
    public static final StellarRecipeSerializer INSTANCE = new StellarRecipeSerializer();
    public static final MapCodec<StellarRecipe> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    ShapedRecipePattern.MAP_CODEC.fieldOf("pattern")
                            .forGetter(StellarRecipe::pattern),
                    ItemStack.CODEC.fieldOf("result")
                            .forGetter(StellarRecipe::result)
            ).apply(instance, StellarRecipe::new));

    public MapCodec<StellarRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, StellarRecipe> streamCodec() {
        return StreamCodec.composite(
                ShapedRecipePattern.STREAM_CODEC,
                StellarRecipe::pattern,
                ItemStack.STREAM_CODEC,
                StellarRecipe::result,
                StellarRecipe::new
        );
    }
}

