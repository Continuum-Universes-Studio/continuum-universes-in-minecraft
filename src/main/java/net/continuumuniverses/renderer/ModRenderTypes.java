package net.continuumuniverses.renderer;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public final class ModRenderTypes {

    private ModRenderTypes() {}

    public static RenderType emissive(ResourceLocation texture) {
        return RenderType.entityTranslucentEmissive(texture);
    }
}
