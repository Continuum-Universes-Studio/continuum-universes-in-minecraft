package net.continuumuniverses.renderer;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.block.model.SimpleModelWrapper;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.QuadCollection;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.DelegateBlockStateModel;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class EmissiveBakedModel extends DelegateBlockStateModel {

    public static final int FULL_BRIGHT = 0xF000F0;
    private static final String EMISSIVE_SUFFIX = "_emissive";
    private final Map<BlockModelPart, SplitParts> splitCache = new IdentityHashMap<>();

    public EmissiveBakedModel(BlockStateModel baseModel) {
        super(baseModel);
    }

    public static boolean hasEmissiveQuads(BlockStateModel model) {
        List<BlockModelPart> parts = new ObjectArrayList<>();
        model.collectParts(RandomSource.create(42L), parts);
        for (BlockModelPart part : parts) {
            if (hasEmissiveQuads(part)) {
                return true;
            }
        }
        return false;
    }

    @Override
    @Deprecated
    public void collectParts(RandomSource random, List<BlockModelPart> parts) {
        List<BlockModelPart> baseParts = new ObjectArrayList<>();
        delegate.collectParts(random, baseParts);
        addSplitParts(baseParts, parts);
    }

    @Override
    public void collectParts(
            BlockAndTintGetter level,
            net.minecraft.core.BlockPos pos,
            BlockState state,
            RandomSource random,
            List<BlockModelPart> parts
    ) {
        List<BlockModelPart> baseParts = new ObjectArrayList<>();
        delegate.collectParts(level, pos, state, random, baseParts);
        addSplitParts(baseParts, parts);
    }

    public List<BakedQuad> getEmissiveQuads(@Nullable Direction side) {
        ensureCache();
        List<BakedQuad> quads = new ArrayList<>();
        for (SplitParts split : splitCache.values()) {
            if (split.emissivePart() != null) {
                quads.addAll(split.emissivePart().getQuads(side));
            }
        }
        return quads;
    }

    public List<BakedQuad> getAllEmissiveQuads() {
        ensureCache();
        List<BakedQuad> quads = new ArrayList<>();
        for (SplitParts split : splitCache.values()) {
            if (split.emissivePart() != null) {
                quads.addAll(split.emissivePart().getQuads(null));
                for (Direction direction : Direction.values()) {
                    quads.addAll(split.emissivePart().getQuads(direction));
                }
            }
        }
        return quads;
    }

    private void addSplitParts(List<BlockModelPart> baseParts, List<BlockModelPart> parts) {
        for (BlockModelPart part : baseParts) {
            SplitParts split = splitCache.computeIfAbsent(part, EmissiveBakedModel::splitPart);
            parts.add(split.basePart());
            if (split.emissivePart() != null) {
                parts.add(split.emissivePart());
            }
        }
    }

    private void ensureCache() {
        if (!splitCache.isEmpty()) {
            return;
        }
        List<BlockModelPart> parts = new ObjectArrayList<>();
        delegate.collectParts(RandomSource.create(42L), parts);
        addSplitParts(parts, new ObjectArrayList<>());
    }

    private static boolean hasEmissiveQuads(BlockModelPart part) {
        if (!(part instanceof SimpleModelWrapper wrapper)) {
            return false;
        }
        return hasEmissiveQuads(wrapper.quads());
    }

    private static boolean hasEmissiveQuads(QuadCollection quads) {
        for (Direction direction : Direction.values()) {
            for (BakedQuad quad : quads.getQuads(direction)) {
                if (isEmissiveQuad(quad)) {
                    return true;
                }
            }
        }
        for (BakedQuad quad : quads.getQuads(null)) {
            if (isEmissiveQuad(quad)) {
                return true;
            }
        }
        return false;
    }

    private static SplitParts splitPart(BlockModelPart part) {
        if (!(part instanceof SimpleModelWrapper wrapper)) {
            return new SplitParts(part, null);
        }

        QuadCollection source = wrapper.quads();
        QuadCollection.Builder baseBuilder = new QuadCollection.Builder();
        QuadCollection.Builder emissiveBuilder = new QuadCollection.Builder();
        boolean hasEmissive = false;

        for (Direction direction : Direction.values()) {
            for (BakedQuad quad : source.getQuads(direction)) {
                if (isEmissiveQuad(quad)) {
                    emissiveBuilder.addCulledFace(direction, quad);
                    hasEmissive = true;
                } else {
                    baseBuilder.addCulledFace(direction, quad);
                }
            }
        }

        for (BakedQuad quad : source.getQuads(null)) {
            if (isEmissiveQuad(quad)) {
                emissiveBuilder.addUnculledFace(quad);
                hasEmissive = true;
            } else {
                baseBuilder.addUnculledFace(quad);
            }
        }

        if (!hasEmissive) {
            return new SplitParts(part, null);
        }

        QuadCollection baseQuads = baseBuilder.build();
        QuadCollection emissiveQuads = emissiveBuilder.build();
        BlockModelPart basePart = new SimpleModelWrapper(
                baseQuads,
                wrapper.useAmbientOcclusion(),
                wrapper.particleIcon(),
                wrapper.renderType()
        );
        BlockModelPart emissivePart = new SimpleModelWrapper(
                emissiveQuads,
                wrapper.useAmbientOcclusion(),
                wrapper.particleIcon(),
                ChunkSectionLayer.TRANSLUCENT
        );
        return new SplitParts(basePart, emissivePart);
    }

    private static boolean isEmissiveQuad(BakedQuad quad) {
        TextureAtlasSprite sprite = quad.sprite();
        return sprite.contents().name().getPath().endsWith(EMISSIVE_SUFFIX);
    }

    private record SplitParts(BlockModelPart basePart, @Nullable BlockModelPart emissivePart) {}
}
