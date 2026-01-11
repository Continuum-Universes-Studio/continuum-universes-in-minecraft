package net.continuumuniverses.renderer;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderBlockScreenEffectEvent;

@EventBusSubscriber(modid = "continuumuniverses", value = Dist.CLIENT)
public final class ClientRenderEvents {

    private ClientRenderEvents() {} // no instancing

    @SubscribeEvent
    public static void onRenderBlock(RenderBlockScreenEffectEvent event) {
        BlockState state = event.getBlockState();
        BlockStateModel model = Minecraft.getInstance()
                .getBlockRenderer()
                .getBlockModel(state);

        if (!(model instanceof EmissiveBakedModel emissive)) {
            return;
        }

        MultiBufferSource buffers = event.getBufferSource();
        VertexConsumer consumer = buffers.getBuffer(ModRenderTypes.emissive(TextureAtlas.LOCATION_BLOCKS));

        for (BakedQuad quad : emissive.getAllEmissiveQuads()) {
            consumer.putBulkData(
                    event.getPoseStack().last(),
                    quad,
                    1f, 1f, 1f, 1f,
                    EmissiveBakedModel.FULL_BRIGHT,
                    OverlayTexture.NO_OVERLAY
            );
        }
    }
}
