package net.continuumuniverses.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import net.continuumuniverses.ContinuumUniverses;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class ContinuumJeiPlugin implements IModPlugin {

    public static final ResourceLocation UID =
            ResourceLocation.fromNamespaceAndPath(ContinuumUniverses.MODID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }
}

