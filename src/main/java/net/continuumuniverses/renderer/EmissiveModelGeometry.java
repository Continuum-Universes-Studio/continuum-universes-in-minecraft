package net.continuumuniverses.renderer;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.QuadCollection;
import net.minecraft.core.Direction;
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
        if (emissiveTexture == null) {
            return QuadCollection.EMPTY;
        }

        // Bake the base block model
        BlockStateModel baseModel = baker.bake(debugName, state);

        QuadCollection.Builder builder = new QuadCollection.Builder();

        // Emissive sprite
        TextureAtlasSprite emissiveSprite = baker
                .getModelManager()
                .getAtlas(TextureAtlas.LOCATION_BLOCKS)
                .getSprite(emissiveTexture);

        // Unculled quads (side == null)
        for (BakedQuad quad : baseModel.getQuads(null)) {
            builder.addUnculledFace(
                    QuadUtils.retexture(quad, emissiveSprite, EmissiveBakedModel.FULL_BRIGHT)
            );
        }

        // Culled face quads
        for (Direction dir : Direction.values()) {
            for (BakedQuad quad : baseModel.getQuads(dir)) {
                builder.addCulledFace(
                        dir,
                        QuadUtils.retexture(quad, emissiveSprite, EmissiveBakedModel.FULL_BRIGHT)
                );
            }
        }

        return builder.build();
    }

}
