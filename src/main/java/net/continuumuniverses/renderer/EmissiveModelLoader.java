package net.continuumuniverses.renderer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.util.GsonHelper;
import net.neoforged.neoforge.client.model.UnbakedModelLoader;

public class EmissiveModelLoader implements UnbakedModelLoader<BlockModel> {

    @Override
    public BlockModel read(
            JsonObject json,
            JsonDeserializationContext context
    ) {
        JsonObject textures = GsonHelper.getAsJsonObject(json, "textures", null);
        if (textures == null) {
            throw new JsonParseException("Expected emissive model to define textures.");
        }

        if (textures.has("emissive")) {
            GsonHelper.getAsString(textures, "emissive");
        }

        BlockModel model = context.deserialize(json, BlockModel.class);
        return new BlockModel(
                new EmissiveModelGeometry(model.geometry()),
                model.guiLight(),
                model.ambientOcclusion(),
                model.transforms(),
                model.textureSlots(),
                model.parent(),
                model.rootTransform(),
                model.renderTypeGroup(),
                model.partVisibility()
        );
    }
}
