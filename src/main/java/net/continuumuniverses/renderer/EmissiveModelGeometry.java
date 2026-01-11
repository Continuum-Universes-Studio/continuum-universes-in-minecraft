package net.continuumuniverses.renderer;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class EmissiveModelGeometry implements IUnbakedGeometry<EmissiveModelGeometry> {

    private final ResourceLocation baseTexture;
    private final ResourceLocation emissiveTexture;

    public EmissiveModelGeometry(ResourceLocation base, ResourceLocation emissive) {
        this.baseTexture = base;
        this.emissiveTexture = emissive;
    }

    @Override
    public BakedModel bake(
            ModelBaker baker,
            Function<Material, TextureAtlasSprite> spriteGetter,
            ModelState modelState,
            ResourceLocation modelLocation
    ) {
        // Bake the base model normally
        BakedModel baseModel = baker.bake(
                new ResourceLocation(modelLocation, ""),
                modelState
        );

        // Bake emissive quads
        List<BakedQuad> emissiveQuads = new ArrayList<>();

        TextureAtlasSprite emissiveSprite = spriteGetter.apply(
                new Material(TextureAtlas.LOCATION_BLOCKS, emissiveTexture)
        );

        for (Direction dir : Direction.values()) {
            emissiveQuads.addAll(
                    QuadUtils.retexture(
                            baseModel.getQuads(null, dir, RandomSource.create()),
                            emissiveSprite,
                            EmissiveBakedModel.FULL_BRIGHT
                    )
            );
        }

        emissiveQuads.addAll(
                QuadUtils.retexture(
                        baseModel.getQuads(null, null, RandomSource.create()),
                        emissiveSprite,
                        EmissiveBakedModel.FULL_BRIGHT
                )
        );

        return new EmissiveBakedModel(baseModel, emissiveQuads);
    }
}

