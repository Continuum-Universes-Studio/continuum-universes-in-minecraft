package net.continuumuniverses.world.poi;

import net.continuumuniverses.ContinuumUniverses;
import net.continuumuniverses.block.ModBlocks;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;

public final class ModPOIs {

    public static final DeferredRegister<PoiType> POIS =
            DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, ContinuumUniverses.MODID);

    public static final DeferredHolder<PoiType, PoiType> UVLA_PORTAL_POI =
            POIS.register(
                    "uvla_portal",
                    () -> new PoiType(
                            Set.of(ModBlocks.UVLA_PORTAL.get().defaultBlockState()),
                            0,
                            1
                    )
            );

    private ModPOIs() {
    }
}
