package net.continuumuniverses.world.poi;

import net.continuumuniverses.ContinuumUniverses;
import net.continuumuniverses.block.ModBlocks;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public final class ModPOIs {

    public static final DeferredRegister<PoiType> POIS =
            DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, ContinuumUniverses.MODID);

    public static final DeferredHolder<PoiType, PoiType> UVLA_PORTAL_POI =
            POIS.register(
                    "uvla_portal",
                    () -> new PoiType(
                            Set.of(),
                            0,
                            1
                    )
            );

    public static void registerPortalPoiStates() {
        if (!ModBlocks.UVLA_PORTAL.isBound() || !UVLA_PORTAL_POI.isBound()) {
            ContinuumUniverses.LOGGER.warn("Skipping UVLA portal POI registration because registries are not ready.");
            return;
        }

        Set<BlockState> states = new HashSet<>(ModBlocks.UVLA_PORTAL.get().getStateDefinition().getPossibleStates());

        try {
            Method registerBlockStates = PoiTypes.class.getDeclaredMethod("registerBlockStates", Holder.class, Set.class);
            registerBlockStates.setAccessible(true);
            registerBlockStates.invoke(null, (Holder<PoiType>) UVLA_PORTAL_POI, states);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Failed to register UVLA portal POI block states.", exception);
        }
    }

    private ModPOIs() {
    }
}
