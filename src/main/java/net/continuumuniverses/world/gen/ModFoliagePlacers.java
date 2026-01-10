package net.continuumuniverses.world.gen;

import net.continuumuniverses.ContinuumUniverses;
import net.continuumuniverses.world.gen.foliage.HollowMushroomFoliagePlacer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModFoliagePlacers {
    public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACERS =
            DeferredRegister.create(Registries.FOLIAGE_PLACER_TYPE, ContinuumUniverses.MODID);

    public static final DeferredHolder<FoliagePlacerType<?>, FoliagePlacerType<HollowMushroomFoliagePlacer>>
            UMDRA_MUSHROOM =
                    FOLIAGE_PLACERS.register(
                            "umdra_mushroom_foliage_placer",
                            () -> new FoliagePlacerType<>(HollowMushroomFoliagePlacer.CODEC)
                    );

    private ModFoliagePlacers() {
    }
}
