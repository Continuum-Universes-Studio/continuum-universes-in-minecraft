package net.continuumuniverses.renderer;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public class EmissiveBakedModel implements BakedModel {

    private final BakedModel baseModel;
    private final List<BakedQuad> emissiveQuads;

    public static final int FULL_BRIGHT = 0xF000F0;

    public EmissiveBakedModel(BakedModel baseModel, List<BakedQuad> emissiveQuads) {
        this.baseModel = baseModel;
        this.emissiveQuads = emissiveQuads;
    }

    @Override
    public List<BakedQuad> getQuads(
            @Nullable BlockState state,
            @Nullable Direction side,
            RandomSource rand
    ) {
        // Vanilla render path → normal quads only
        return baseModel.getQuads(state, side, rand);
    }

    public List<BakedQuad> getEmissiveQuads(
            @Nullable BlockState state,
            @Nullable Direction side,
            RandomSource rand
    ) {
        return emissiveQuads;
    }

    @Override public boolean useAmbientOcclusion() { return baseModel.useAmbientOcclusion(); }
    @Override public boolean isGui3d() { return baseModel.isGui3d(); }
    @Override public boolean usesBlockLight() { return false; }
    @Override public boolean isCustomRenderer() { return false; }
    @Override public TextureAtlasSprite getParticleIcon() { return baseModel.getParticleIcon(); }
    @Override public ItemOverrides getOverrides() { return baseModel.getOverrides(); }
}

