package net.jensensagastudio.continuumuniverses.recipes;

import net.jensensagastudio.continuumuniverses.ContinuumUniverses;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipes {

    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, ContinuumUniverses.MODID);

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, ContinuumUniverses.MODID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<PlasmaRecipe>> PLASMA =
            TYPES.register("plasma", () -> new RecipeType<>() {});

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<PlasmaRecipe>> PLASMA_SERIALIZER =
            SERIALIZERS.register("plasma",
                    () -> new AbstractCookingRecipe.Serializer<>(PlasmaRecipe::new, 40)
            );

    public static final DeferredHolder<RecipeType<?>, RecipeType<StellarRecipe>>
            STELLAR_FORGE = TYPES.register("stellar_forge", () -> new RecipeType<>() {});

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<StellarRecipe>>
            STELLAR_FORGE_SERIALIZER =
            SERIALIZERS.register(
                    "stellar_forge",
                    StellarRecipeSerializer::new
            );
    public static final DeferredHolder<RecipeType<?>, RecipeType<LunarRecipe>>
            LUNAR_FORGE = TYPES.register("lunar_forge", () -> new RecipeType<>() {});

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<LunarRecipe>>
            LUNAR_FORGE_SERIALIZER =
            SERIALIZERS.register(
                    "lunar_forge",
                    LunarRecipeSerializer::new
            );
    public static void register(IEventBus bus) {
        TYPES.register(bus);
        SERIALIZERS.register(bus);
    }
}

