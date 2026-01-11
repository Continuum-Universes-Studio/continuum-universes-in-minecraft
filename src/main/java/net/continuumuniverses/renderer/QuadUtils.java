package net.continuumuniverses.renderer;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import java.util.ArrayList;
import java.util.List;

public class QuadUtils {

    public static List<BakedQuad> retexture(
            List<BakedQuad> original,
            TextureAtlasSprite sprite,
            int light
    ) {
        List<BakedQuad> result = new ArrayList<>();

        for (BakedQuad quad : original) {
            int[] data = quad.vertices().clone();
            remapUvs(data, quad.sprite(), sprite);

            // Lightmap coords live at index 6 in the vertex format
            for (int i = 0; i < 4; i++) {
                int lightIndex = 8 * i + 6;
                data[lightIndex] = light;
            }

            result.add(new BakedQuad(
                    data,
                    quad.tintIndex(),
                    quad.direction(),
                    sprite,
                    quad.shade(),
                    quad.lightEmission(),
                    quad.hasAmbientOcclusion()
            ));
        }

        return result;
    }
    public static BakedQuad retexture(
            BakedQuad quad,
            TextureAtlasSprite sprite,
            int light
    ) {
        int[] data = quad.vertices().clone();
        remapUvs(data, quad.sprite(), sprite);

        for (int i = 0; i < 4; i++) {
            int lightIndex = 8 * i + 6;
            data[lightIndex] = light;
        }

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

    public static BakedQuad retextureEmissive(
            BakedQuad quad,
            TextureAtlasSprite sprite,
            int light,
            float alpha
    ) {
        int[] data = quad.vertices().clone();
        remapUvs(data, quad.sprite(), sprite);

        int alphaByte = Math.max(0, Math.min(255, Math.round(alpha * 255f)));

        for (int i = 0; i < 4; i++) {
            int baseIndex = 8 * i;
            int colorIndex = baseIndex + 3;
            int color = data[colorIndex];
            int rgb = color & 0x00FFFFFF;
            data[colorIndex] = (alphaByte << 24) | rgb;
            data[baseIndex + 6] = light;
        }

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

    private static void remapUvs(int[] data, TextureAtlasSprite oldSprite, TextureAtlasSprite newSprite) {
        float oldU0 = oldSprite.getU0();
        float oldV0 = oldSprite.getV0();
        float oldUSize = oldSprite.getU1() - oldU0;
        float oldVSize = oldSprite.getV1() - oldV0;

        for (int i = 0; i < 4; i++) {
            int baseIndex = 8 * i;
            float u = Float.intBitsToFloat(data[baseIndex + 4]);
            float v = Float.intBitsToFloat(data[baseIndex + 5]);
            float uNormalized = oldUSize == 0f ? 0f : (u - oldU0) / oldUSize;
            float vNormalized = oldVSize == 0f ? 0f : (v - oldV0) / oldVSize;
            data[baseIndex + 4] = Float.floatToRawIntBits(newSprite.getU(uNormalized));
            data[baseIndex + 5] = Float.floatToRawIntBits(newSprite.getV(vNormalized));
        }
    }

}
