package net.continuumuniverses.renderer;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.QuadCollection;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.UnbakedGeometry;
import net.minecraft.core.Direction;
import net.minecraft.util.context.ContextMap;
import net.neoforged.neoforge.client.model.ExtendedUnbakedGeometry;
import net.neoforged.neoforge.client.model.NeoForgeModelProperties;
import net.neoforged.neoforge.client.model.UnbakedElementsHelper;

import javax.annotation.Nullable;
import com.mojang.math.Transformation;

public class EmissiveModelGeometry implements ExtendedUnbakedGeometry {
    @Nullable
    private final UnbakedGeometry baseGeometry;

    public EmissiveModelGeometry(@Nullable UnbakedGeometry baseGeometry) {
        this.baseGeometry = baseGeometry;
    }

    @Override
    public QuadCollection bake(
            TextureSlots textureSlots,
            ModelBaker baker,
            ModelState state,
            ModelDebugName debugName,
            ContextMap additionalProperties
    ) {
        Material emissiveMaterial = textureSlots.getMaterial("emissive");
        if (emissiveMaterial == null) {
            return QuadCollection.EMPTY;
        }

        ModelState bakedState = state;
        QuadCollection baseQuads = null;

        if (baseGeometry != null) {
            baseQuads = baseGeometry.bake(textureSlots, baker, bakedState, debugName, additionalProperties);
        } else if (debugName instanceof ResolvedModel resolvedModel && resolvedModel.parent() != null) {
            Transformation rootTransform = additionalProperties.getOptional(NeoForgeModelProperties.TRANSFORM);
            if (rootTransform != null && !rootTransform.isIdentity()) {
                bakedState = UnbakedElementsHelper.composeRootTransformIntoModelState(state, rootTransform);
            }
            baseQuads = resolvedModel.parent().bakeTopGeometry(textureSlots, baker, bakedState);
        }

        if (baseQuads == null || baseQuads == QuadCollection.EMPTY) {
            return QuadCollection.EMPTY;
        }

        QuadCollection.Builder builder = new QuadCollection.Builder();

        // Base quads (normal rendering)
        for (BakedQuad quad : baseQuads.getQuads(null)) {
            builder.addUnculledFace(quad);
        }
        for (Direction dir : Direction.values()) {
            for (BakedQuad quad : baseQuads.getQuads(dir)) {
                builder.addCulledFace(dir, quad);
            }
        }

        // Emissive sprite
        TextureAtlasSprite emissiveSprite = baker.sprites().get(emissiveMaterial, debugName);

        // Unculled quads (side == null)
        for (BakedQuad quad : baseQuads.getQuads(null)) {
            builder.addUnculledFace(
                    QuadUtils.retexture(quad, emissiveSprite, EmissiveBakedModel.FULL_BRIGHT)
            );
        }

        // Culled face quads
        for (Direction dir : Direction.values()) {
            for (BakedQuad quad : baseQuads.getQuads(dir)) {
                builder.addCulledFace(
                        dir,
                        QuadUtils.retexture(quad, emissiveSprite, EmissiveBakedModel.FULL_BRIGHT)
                );
            }
        }

        return builder.build();
    }

}
