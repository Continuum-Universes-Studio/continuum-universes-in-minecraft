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

}
