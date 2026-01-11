package net.continuumuniverses.renderer;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.neoforged.neoforge.client.model.IQuadTransformer;

public final class EmissiveQuadUtil {
    private EmissiveQuadUtil() {}

    public static List<BakedQuad> fullbrightCopy(List<BakedQuad> in, int packedLight) {
        if (in.isEmpty()) return in;

        List<BakedQuad> out = new ArrayList<>(in.size());
        for (BakedQuad q : in) out.add(fullbrightCopy(q, packedLight));
        return out;
    }

    public static BakedQuad fullbrightCopy(BakedQuad original, int packedLight) {
        int[] v = original.vertices().clone();

        for (int i = 0; i < 4; i++) {
            int base = i * IQuadTransformer.STRIDE;
            v[base + IQuadTransformer.UV2] = packedLight;
        }

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
