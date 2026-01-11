package net.continuumuniverses.renderer;

import javax.annotation.Nullable;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.QuadCollection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.context.ContextMap;
import net.neoforged.neoforge.client.model.ExtendedUnbakedGeometry;

public class EmissiveModelGeometry implements ExtendedUnbakedGeometry {
    private final ResourceLocation baseTexture;
    @Nullable
    private final ResourceLocation emissiveTexture;

    public EmissiveModelGeometry(ResourceLocation baseTexture, @Nullable ResourceLocation emissiveTexture) {
        this.baseTexture = baseTexture;
        this.emissiveTexture = emissiveTexture;
    }

    public ResourceLocation getBaseTexture() {
        return baseTexture;
    }

    @Nullable
    public ResourceLocation getEmissiveTexture() {
        return emissiveTexture;
    }

    @Override
    public QuadCollection bake(
            TextureSlots textureSlots,
            ModelBaker baker,
            ModelState state,
            ModelDebugName debugName,
            ContextMap additionalProperties
    ) {
        return QuadCollection.EMPTY;
    }
}
