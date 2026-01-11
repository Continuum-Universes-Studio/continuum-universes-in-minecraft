package net.continuumuniverses.renderer;

import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.client.resources.model.UnbakedGeometry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.context.ContextMap;
import net.neoforged.neoforge.client.model.AbstractUnbakedModel;
import net.neoforged.neoforge.client.model.StandardModelParameters;

public final class EmissiveUnbakedModel extends AbstractUnbakedModel {

    private final UnbakedGeometry geometry;
    private final ResourceLocation baseModel;
    private final ResourceLocation emissiveModel;

    public EmissiveUnbakedModel(
            StandardModelParameters params,
            UnbakedGeometry geometry,
            ResourceLocation baseModel,
            ResourceLocation emissiveModel
    ) {
        super(params);
        this.geometry = geometry;
        this.baseModel = baseModel;
        this.emissiveModel = emissiveModel;
    }

    @Override
    public UnbakedGeometry geometry() {
        return this.geometry;
    }

    @Override
    public void resolveDependencies(ResolvableModel.Resolver resolver) {
        super.resolveDependencies(resolver);
        resolver.markDependency(baseModel);
        resolver.markDependency(emissiveModel);
    }

    @Override
    public void fillAdditionalProperties(ContextMap.Builder propertiesBuilder) {
        super.fillAdditionalProperties(propertiesBuilder);
        // No extra properties needed right now.
    }
}
