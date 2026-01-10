package net.continuumuniverses.world.gen.foliage;

import com.mojang.datafixers.Products.P3;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.RecordCodecBuilder.Instance;
import com.mojang.serialization.codecs.RecordCodecBuilder.Mu;
import net.continuumuniverses.world.gen.ModFoliagePlacers;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

public class HollowMushroomFoliagePlacer extends FoliagePlacer {
    public static final MapCodec<HollowMushroomFoliagePlacer> CODEC =
            RecordCodecBuilder.mapCodec(instance -> hollowMushroomParts(instance).apply(instance, HollowMushroomFoliagePlacer::new));
    private final int height;

    private static <P extends HollowMushroomFoliagePlacer> P3<Mu<P>, IntProvider, IntProvider, Integer> hollowMushroomParts(
            Instance<P> instance
    ) {
        return foliagePlacerParts(instance).and(Codec.intRange(1, 16).fieldOf("height").forGetter(HollowMushroomFoliagePlacer::getHeight));
    }

    public HollowMushroomFoliagePlacer(IntProvider radius, IntProvider offset, int height) {
        super(radius, offset);
        this.height = height;
    }

    private int getHeight() {
        return height;
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return ModFoliagePlacers.UMDRA_MUSHROOM.get();
    }

    @Override
    protected void createFoliage(
            LevelSimulatedReader level,
            FoliageSetter foliageSetter,
            RandomSource random,
            TreeConfiguration config,
            int maxFreeTreeHeight,
            FoliageAttachment attachment,
            int foliageHeight,
            int foliageRadius,
            int offset
    ) {
        int baseRadius = foliageRadius + attachment.radiusOffset();

        for (int layer = 0; layer < foliageHeight; layer++) {
            int localY = offset - layer;
            int layerRadius = baseRadius;
            if (layer == 0) {
                layerRadius = baseRadius + 1;
            } else if (layer == foliageHeight - 1) {
                layerRadius = Math.max(baseRadius - 1, 0);
            }

            placeLeavesRow(level, foliageSetter, random, config, attachment.pos(), layerRadius, localY, attachment.doubleTrunk());
        }
    }

    @Override
    public int foliageHeight(RandomSource random, int height, TreeConfiguration config) {
        return this.height;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int localX, int localY, int localZ, int range, boolean large) {
        if (localX == range && localZ == range) {
            return true;
        }

        if (localY < 0) {
            return localX < range && localZ < range;
        }

        return false;
    }
}
