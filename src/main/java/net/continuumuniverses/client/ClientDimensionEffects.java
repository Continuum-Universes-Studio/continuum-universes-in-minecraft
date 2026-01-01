package net.continuumuniverses.client;

import net.continuumuniverses.ContinuumUniverses;
import net.continuumuniverses.world.dimensions.uvla.UvlaDimensionEffects;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;

@EventBusSubscriber(modid = ContinuumUniverses.MODID, value = Dist.CLIENT)
public final class ClientDimensionEffects {

    private ClientDimensionEffects() {}

    @SubscribeEvent
    public static void registerEffects(RegisterDimensionSpecialEffectsEvent event) {
        event.register(
                ResourceLocation.fromNamespaceAndPath(ContinuumUniverses.MODID, "uvla"),
                new UvlaDimensionEffects()
        );
    }
}
