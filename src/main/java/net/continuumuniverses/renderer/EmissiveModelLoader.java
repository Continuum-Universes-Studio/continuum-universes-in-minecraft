package net.continuumuniverses.renderer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class EmissiveModelLoader implements IGeometryLoader<EmissiveModelGeometry> {

    @Override
    public EmissiveModelGeometry read(
            JsonObject json,
            JsonDeserializationContext context
    ) {
        JsonObject textures = json.getAsJsonObject("textures");

        ResourceLocation base =
                new ResourceLocation(textures.get("all").getAsString());

        ResourceLocation emissive =
                textures.has("emissive")
                        ? new ResourceLocation(textures.get("emissive").getAsString())
                        : null;

        return new EmissiveModelGeometry(base, emissive);
    }
}
