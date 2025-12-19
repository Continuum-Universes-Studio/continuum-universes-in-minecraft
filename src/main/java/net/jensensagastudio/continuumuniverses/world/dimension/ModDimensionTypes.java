package net.jensensagastudio.continuumuniverses.world.dimension;

import net.jensensagastudio.continuumuniverses.fluid.ModFluids;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.DimensionTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.DimensionType;

public class ModDimensionTypes extends DimensionTypes {
    public static final ResourceKey<DimensionType> UVLA_DIMENSION_TYPE = register("uvla");

    private static ResourceKey<DimensionType> register(String name) {
        return ResourceKey.create(Registries.DIMENSION_TYPE, Identifier.withDefaultNamespace(name));
    }

}
