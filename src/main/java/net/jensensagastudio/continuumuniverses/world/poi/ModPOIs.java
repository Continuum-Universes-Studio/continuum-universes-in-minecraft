package net.jensensagastudio.continuumuniverses.world.poi;

import net.jensensagastudio.continuumuniverses.ContinuumUniverses;
import net.jensensagastudio.continuumuniverses.block.ModBlocks;

import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.minecraft.resources.Identifier;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

@EventBusSubscriber(modid = ContinuumUniverses.MODID)
public final class ModPOIs {

    public static Holder<PoiType> UVLA_PORTAL_POI;

    @SubscribeEvent
    public static void registerPOIs(RegisterEvent event) {
        event.register(Registries.POINT_OF_INTEREST_TYPE, helper -> {
            helper.register(
                    Identifier.tryParse("continuumuniverses:uvla_portal"),
                    new PoiType(
                            Set.of(ModBlocks.UVLA_PORTAL.value().defaultBlockState()),
                            0,
                            1
                    )
            );
        });
    }

}
