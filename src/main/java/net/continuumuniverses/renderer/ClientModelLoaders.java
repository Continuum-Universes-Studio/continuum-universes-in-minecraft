package net.continuumuniverses.renderer;

import net.continuumuniverses.ContinuumUniverses;
import net.continuumuniverses.renderer.EmissiveModelLoader;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;

@EventBusSubscriber(modid = ContinuumUniverses.MODID, value = Dist.CLIENT)
public final class ClientModelLoaders {
    public static final ResourceLocation EMISSIVE_LOADER_ID =
            ResourceLocation.fromNamespaceAndPath(ContinuumUniverses.MODID, "emissive");

    private ClientModelLoaders() {}

    @SubscribeEvent
    public static void registerLoaders(ModelEvent.RegisterLoaders event) {
        event.register(EMISSIVE_LOADER_ID, new EmissiveModelLoader());
    }
}
