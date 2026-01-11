package net.continuumuniverses.renderer;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.QuadCollection;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.client.model.DelegateBlockStateModel;

import javax.annotation.Nullable;
import java.util.List;

public class EmissiveBakedModel extends DelegateBlockStateModel {

    private final QuadCollection emissiveQuads;

    public static final int FULL_BRIGHT = 0xF000F0;

    public EmissiveBakedModel(BlockStateModel baseModel, QuadCollection emissiveQuads) {
        super(baseModel);
        this.emissiveQuads = emissiveQuads;
    }

    public List<BakedQuad> getEmissiveQuads(@Nullable Direction side) {
        return emissiveQuads.getQuads(side);
    }

    public List<BakedQuad> getAllEmissiveQuads() {
        return emissiveQuads.getAll();
    }
}
