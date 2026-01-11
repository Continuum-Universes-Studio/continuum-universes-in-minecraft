package net.continuumuniverses.renderer;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.QuadCollection;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.DelegateBlockStateModel;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public final class EmissiveBakedModel extends DelegateBlockStateModel {

    public static final int FULL_BRIGHT = 0xF000F0;
    private static final String EMISSIVE_SUFFIX = "_emissive";

    public EmissiveBakedModel(BlockStateModel baseModel) {
        super(baseModel);
    }

    /* --------------------------------------------------------------------- */
    /* Base rendering — untouched                                             */
    /* --------------------------------------------------------------------- */

    @Override
    @Deprecated
    public void collectParts(RandomSource random, List<BlockModelPart> parts) {
        delegate.collectParts(random, parts);
    }

    @Override
    public void collectParts(
            BlockAndTintGetter level,
            net.minecraft.core.BlockPos pos,
            BlockState state,
            RandomSource random,
            List<BlockModelPart> parts
    ) {
        delegate.collectParts(level, pos, state, random, parts);
    }

    /* --------------------------------------------------------------------- */
    /* Emissive extraction                                                    */
    /* --------------------------------------------------------------------- */

    public List<BakedQuad> getAllEmissiveQuads() {
        List<BakedQuad> result = new ArrayList<>();
        List<BlockModelPart> parts = new ObjectArrayList<>();

        delegate.collectParts(RandomSource.create(42L), parts);

        for (BlockModelPart part : parts) {
            if (part instanceof SimpleModelWrapper wrapper) {
                extractEmissive(wrapper.quads(), result);
            }
        }
        return result;
    }

    private static void extractEmissive(
            QuadCollection quads,
            List<BakedQuad> out
    ) {
        for (Direction dir : Direction.values()) {
            for (BakedQuad quad : quads.getQuads(dir)) {
                if (isEmissiveQuad(quad)) {
                    out.add(
                            QuadUtils.retextureEmissive(
                                    quad,
                                    quad.sprite(),
                                    FULL_BRIGHT
                            )
                    );
                }
            }
        }

        for (BakedQuad quad : quads.getQuads(null)) {
            if (isEmissiveQuad(quad)) {
                out.add(
                        QuadUtils.retextureEmissive(
                                quad,
                                quad.sprite(),
                                FULL_BRIGHT
                        )
                );
            }
        }
    }

    /* --------------------------------------------------------------------- */

    private static boolean isEmissiveQuad(BakedQuad quad) {
        TextureAtlasSprite sprite = quad.sprite();
        return sprite.contents()
                .name()
                .getPath()
                .endsWith(EMISSIVE_SUFFIX);
    }
    public static boolean hasEmissiveQuads(BlockStateModel model) {
        List<BlockModelPart> parts = new ObjectArrayList<>();
        model.collectParts(RandomSource.create(42L), parts);

        for (BlockModelPart part : parts) {
            if (part instanceof SimpleModelWrapper wrapper) {
                if (hasEmissiveQuads(wrapper.quads())) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean hasEmissiveQuads(QuadCollection quads) {
        for (Direction dir : Direction.values()) {
            for (BakedQuad quad : quads.getQuads(dir)) {
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

}
