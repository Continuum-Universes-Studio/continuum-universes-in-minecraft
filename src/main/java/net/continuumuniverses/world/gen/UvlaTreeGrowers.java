package net.continuumuniverses.world.gen;

import net.continuumuniverses.ContinuumUniverses;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.Optional;

public final class UvlaTreeGrowers {
    private UvlaTreeGrowers() {}

    private static ResourceKey<ConfiguredFeature<?, ?>> key(String name) {
        return ResourceKey.create(
                Registries.CONFIGURED_FEATURE,
                ResourceLocation.fromNamespaceAndPath(ContinuumUniverses.MODID, name)
        );
    }

    public static final TreeGrower UVLA_WILLOW =
            new TreeGrower("uvla_willow", Optional.empty(), Optional.of(key("uvla_willow_tree")), Optional.empty());

    public static final TreeGrower UVLA_MANGROVE =
            new TreeGrower("uvla_mangrove", Optional.empty(), Optional.of(key("uvla_mangrove_tree")), Optional.empty());

    public static final TreeGrower UVLA_GIANT =
            new TreeGrower("uvla_giant", Optional.of(key("uvla_giant_tree")), Optional.empty(), Optional.empty());
}
