package net.continuumuniverses.renderer;

import java.util.List;

import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.QuadCollection;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.UnbakedGeometry;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.ModelState;

import net.neoforged.neoforge.client.extensions.UnbakedGeometryExtension;
import net.neoforged.neoforge.client.model.IQuadTransformer;

/**
 * Bakes:
 *  - base model quads as-is
 *  - emissive model quads with forced max lightmap (fullbright)
 * Then merges them into one QuadCollection.
 */
public final class EmissiveGeometry implements UnbakedGeometry, UnbakedGeometryExtension {

    private final net.minecraft.resources.ResourceLocation baseModel;
    private final net.minecraft.resources.ResourceLocation emissiveModel;
    private final int emissivity; // 0..15

    public EmissiveGeometry(
            net.minecraft.resources.ResourceLocation baseModel,
            net.minecraft.resources.ResourceLocation emissiveModel,
            int emissivity
    ) {
        this.baseModel = baseModel;
        this.emissiveModel = emissiveModel;
        this.emissivity = emissivity;
    }

    /**
     * NeoForge wants you to use the Extension bake that receives additionalProperties in 1.21.9+.
     */
    @Override
    public QuadCollection bake(
            TextureSlots textureSlotsIgnored,
            ModelBaker baker,
            ModelState state,
            ModelDebugName debugName,
            ContextMap additionalProperties
    ) {
        // ---- Bake base model
        ResolvedModel baseResolved = baker.getModel(baseModel);
        TextureSlots baseSlots = baseResolved.getTopTextureSlots();
        QuadCollection baseQuads = baseResolved.bakeTopGeometry(baseSlots, baker, state);

        // ---- Bake emissive model
        ResolvedModel emissiveResolved = baker.getModel(emissiveModel);
        TextureSlots emissiveSlots = emissiveResolved.getTopTextureSlots();
        QuadCollection emissiveQuads = emissiveResolved.bakeTopGeometry(emissiveSlots, baker, state);

        // ---- Merge into one collection
        QuadCollection.Builder out = new QuadCollection.Builder();

        // Base: copy all faces (culled + unculled) as-is
        copyAllQuads(baseQuads, out);

        // Emissive: copy all faces, but force fullbright in vertex lightmap
        copyAllQuadsEmissive(emissiveQuads, out, emissivity);

        return out.build();
    }

    // --- Compatibility fallback (shouldn’t be used on 1.21.10, but harmless)
    @Override
    @Deprecated
    public QuadCollection bake(
            TextureSlots textureSlots,
            ModelBaker baker,
            ModelState state,
            ModelDebugName debugName
    ) {
        return bake(textureSlots, baker, state, debugName, ContextMap.EMPTY);
    }

    private static void copyAllQuads(QuadCollection from, QuadCollection.Builder to) {
        // Unculled
        for (BakedQuad q : from.getQuads(null)) {
            to.addUnculledFace(q);
        }
        // Culled per-direction
        for (Direction dir : Direction.values()) {
            for (BakedQuad q : from.getQuads(dir)) {
                to.addCulledFace(dir, q);
            }
        }
    }

    private static void copyAllQuadsEmissive(QuadCollection from, QuadCollection.Builder to, int emissivity) {
        int packed = LightTexture.pack(emissivity, emissivity);

        // Unculled
        for (BakedQuad q : from.getQuads(null)) {
            to.addUnculledFace(makeFullbrightCopy(q, packed));
        }

        // Culled per-direction
        for (Direction dir : Direction.values()) {
            for (BakedQuad q : from.getQuads(dir)) {
                to.addCulledFace(dir, makeFullbrightCopy(q, packed));
            }
        }
    }

    /**
     * Copies a quad and overwrites its per-vertex lightmap (UV2) to be fullbright.
     *
     * This follows the same underlying idea as Forge/NeoForge QuadTransformers setting emissivity,
     * but done explicitly so you don’t have to fight method name differences between mappings.
     * The UV2 index/stride come from IQuadTransformer’s vertex layout contract.
     */
    private static BakedQuad makeFullbrightCopy(BakedQuad original, int packedLight) {
        // BakedQuad is a record in 1.21.10 with an int[] vertex payload. :contentReference[oaicite:3]{index=3}
        int[] v = original.vertices().clone();

        for (int i = 0; i < 4; i++) {
            int base = i * IQuadTransformer.STRIDE;
            v[base + IQuadTransformer.UV2] = packedLight;
        }

        // Preserve all other quad properties.
        // Note: 1.21.10 has constructors that include hasAmbientOcclusion. :contentReference[oaicite:4]{index=4}
        return new BakedQuad(
                v,
                original.tintIndex(),
                original.direction(),
                original.sprite(),
                original.shade(),
                original.lightEmission(),
                original.hasAmbientOcclusion()
        );
    }
}