package net.continuumuniverses.renderer;

import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.QuadCollection;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.context.ContextMap;

import net.neoforged.neoforge.client.model.ExtendedUnbakedGeometry;
import net.neoforged.neoforge.client.model.IQuadTransformer;
import net.neoforged.neoforge.client.model.QuadTransformers;

public final class EmissiveUnbakedGeometry implements ExtendedUnbakedGeometry {

    private final ResourceLocation baseModel;
    private final ResourceLocation emissiveModel;

    public EmissiveUnbakedGeometry(ResourceLocation baseModel, ResourceLocation emissiveModel) {
        this.baseModel = baseModel;
        this.emissiveModel = emissiveModel;
    }

    @Override
    public QuadCollection bake(
            TextureSlots ignoredTopSlots,
            ModelBaker baker,
            ModelState state,
            ModelDebugName debugName,
            ContextMap additionalProperties
    ) {
        ResolvedModel base = baker.getModel(baseModel);
        ResolvedModel emissive = baker.getModel(emissiveModel);

        // Use each model's own top texture slots (more correct than reusing ignoredTopSlots)
        TextureSlots baseSlots = base.getTopTextureSlots();
        TextureSlots emissiveSlots = emissive.getTopTextureSlots();

        // 1.21.10 signature: (TextureSlots, ModelBaker, ModelState)
        QuadCollection baseQuads = base.bakeTopGeometry(baseSlots, baker, state);
        QuadCollection emissiveQuads = emissive.bakeTopGeometry(emissiveSlots, baker, state);

        // Fullbright emissive layer
        IQuadTransformer fullbright = QuadTransformers.settingMaxEmissivity();
        transformAll(emissiveQuads, fullbright);

        // Merge
        QuadCollection.Builder out = new QuadCollection.Builder();
        appendAll(out, baseQuads);
        appendAll(out, emissiveQuads);
        return out.build();
    }

    private static void transformAll(QuadCollection quads, IQuadTransformer transformer) {
        transformer.processInPlace(quads.getQuads(null));
        for (Direction dir : Direction.values()) {
            transformer.processInPlace(quads.getQuads(dir));
        }
    }

    private static void appendAll(QuadCollection.Builder out, QuadCollection in) {
        for (BakedQuad q : in.getQuads(null)) {
            out.addUnculledFace(q);
        }
        for (Direction dir : Direction.values()) {
            List<BakedQuad> face = in.getQuads(dir);
            for (BakedQuad q : face) {
                out.addCulledFace(dir, q);
            }
        }
    }
}
