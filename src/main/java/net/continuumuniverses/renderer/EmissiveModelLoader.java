package net.continuumuniverses.renderer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.continuumuniverses.ContinuumUniverses;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.RenderTypeGroup;
import net.neoforged.neoforge.client.model.StandardModelParameters;
import net.neoforged.neoforge.client.model.UnbakedModelLoader;

public final class EmissiveModelLoader implements UnbakedModelLoader<EmissiveUnbakedModel> {

    @Override
    public EmissiveUnbakedModel read(JsonObject json, JsonDeserializationContext context) throws JsonParseException {
        StandardModelParameters params = StandardModelParameters.parse(json, context);

        ResourceLocation base = requireModel(json, "base");
        ResourceLocation emissive = requireModel(json, "emissive");

        // Force CUTOUT render type (model-wide)
        RenderTypeGroup forcedCutout = new RenderTypeGroup(ChunkSectionLayer.CUTOUT, RenderType.cutout());

        StandardModelParameters forcedParams = new StandardModelParameters(
                params.parent(),
                params.textures(),
                params.itemTransforms(),
                params.ambientOcclusion(),
                params.guiLight(),
                params.rootTransform(),
                forcedCutout,
                params.partVisibility()
        );

        // ✅ Your EmissiveUnbakedModel expects (params, geometry, baseRL, emissiveRL)
        EmissiveUnbakedGeometry geometry = new EmissiveUnbakedGeometry(base, emissive);
        return new EmissiveUnbakedModel(forcedParams, geometry, base, emissive);
    }

    private static ResourceLocation requireModel(JsonObject json, String key) {
        if (!json.has(key)) throw new JsonParseException("Missing required key '" + key + "'");
        return ResourceLocation.parse(json.get(key).getAsString());
    }
}
