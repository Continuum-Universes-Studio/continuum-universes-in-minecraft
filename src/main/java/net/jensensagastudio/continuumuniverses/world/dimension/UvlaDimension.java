package net.jensensagastudio.continuumuniverses.world.dimension;

import net.jensensagastudio.continuumuniverses.ContinuumUniverses;
import net.minecraft.data.worldgen.DimensionTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.minecraft.world.level.dimension.DimensionType;

import net.minecraft.core.registries.Registries;

public final class UvlaDimension extends DimensionTypes {

	// IMPORTANT: this must match your *dimension type* id (the thing used by your dimension JSON "type")
	public static final ResourceKey<DimensionType> UVLA_DIMENSION_TYPE =
			register("uvla");

	private static ResourceKey<DimensionType> register(String name) {
		return ResourceKey.create(Registries.DIMENSION_TYPE, ResourceLocation.withDefaultNamespace(name));
	}
}
