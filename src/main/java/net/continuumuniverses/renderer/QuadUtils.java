package net.continuumuniverses.renderer;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import java.util.ArrayList;
import java.util.List;

public final class QuadUtils {

    private static final int VERTEX_STRIDE = 8;
    private static final int COLOR_INDEX = 3;
    private static final int U_INDEX = 4;
    private static final int V_INDEX = 5;
    private static final int LIGHT_INDEX = 6;

    private QuadUtils() {}

    /* -------------------------------------------------------------------------
     * Public API
     * ---------------------------------------------------------------------- */

    public static List<BakedQuad> retexture(
            List<BakedQuad> source,
            TextureAtlasSprite sprite,
            int light
    ) {
        List<BakedQuad> result = new ArrayList<>(source.size());
        for (BakedQuad quad : source) {
            result.add(retexture(quad, sprite, light));
        }
        return result;
    }

    public static BakedQuad retexture(
            BakedQuad quad,
            TextureAtlasSprite sprite,
            int light
    ) {
        int[] data = cloneAndRemap(quad, sprite);
        applyLight(data, light);

        return new BakedQuad(
                data,
                quad.tintIndex(),
                quad.direction(),
                sprite,
                quad.shade(),
                quad.lightEmission(),
                quad.hasAmbientOcclusion()
        );
    }

    /**
     * Creates a pure emissive quad:
     * - FULL_BRIGHT
     * - no shading
     * - no AO
     * - additive-ready vertex color
     */
    public static BakedQuad retextureEmissive(
            BakedQuad quad,
            TextureAtlasSprite sprite,
            int light
    ) {
        int[] data = cloneAndRemap(quad, sprite);

        for (int v = 0; v < 4; v++) {
            int base = v * VERTEX_STRIDE;
            data[base + LIGHT_INDEX] = light;
            data[base + COLOR_INDEX] = 0xFFFFFFFF;
        }

        return new BakedQuad(
                data,
                -1,
                quad.direction(),
                sprite,
                false, // no shading
                0,
                false  // no AO
        );
    }

    /* -------------------------------------------------------------------------
     * Internal helpers
     * ---------------------------------------------------------------------- */

    private static int[] cloneAndRemap(
            BakedQuad quad,
            TextureAtlasSprite newSprite
    ) {
        int[] data = quad.vertices().clone();
        remapUvs(data, quad.sprite(), newSprite);
        return data;
    }

    private static void applyLight(int[] data, int light) {
        for (int v = 0; v < 4; v++) {
            data[v * VERTEX_STRIDE + LIGHT_INDEX] = light;
        }
    }

    private static void remapUvs(
            int[] data,
            TextureAtlasSprite oldSprite,
            TextureAtlasSprite newSprite
    ) {
        float oldU0 = oldSprite.getU0();
        float oldV0 = oldSprite.getV0();
        float oldUSize = oldSprite.getU1() - oldU0;
        float oldVSize = oldSprite.getV1() - oldV0;

        for (int v = 0; v < 4; v++) {
            int base = v * VERTEX_STRIDE;

            float u = Float.intBitsToFloat(data[base + U_INDEX]);
            float vTex = Float.intBitsToFloat(data[base + V_INDEX]);

            float uNorm = oldUSize == 0f ? 0f : (u - oldU0) / oldUSize;
            float vNorm = oldVSize == 0f ? 0f : (vTex - oldV0) / oldVSize;

            data[base + U_INDEX] = Float.floatToRawIntBits(newSprite.getU(uNorm));
            data[base + V_INDEX] = Float.floatToRawIntBits(newSprite.getV(vNorm));
        }
    }
}
