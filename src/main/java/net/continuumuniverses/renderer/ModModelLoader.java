package net.continuumuniverses.renderer;

import net.continuumuniverses.ContinuumUniverses;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.ModelEvent;

public class ModModelLoaders {

    public static final ResourceLocation EMISSIVE_LOADER =
            new ResourceLocation(ContinuumUniverses.MODID,
                    "emissive");

    public static void register(ModelEvent.RegisterGeometryLoaders event) {
        event.register(EMISSIVE_LOADER, EmissiveModelLoader::new);
    }
}
