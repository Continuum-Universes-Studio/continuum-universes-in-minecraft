package net.continuumuniverses.renderer;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.util.RandomSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RenderBlockScreenEffectEvent;

@Mod.EventBusSubscriber(
        modid = "continuumuniverses",
        bus = Mod.EventBusSubscriber.Bus.FORGE,
        value = Dist.CLIENT
)
public final class ClientRenderEvents {

    private ClientRenderEvents() {} // no instancing

    @SubscribeEvent
    public static void onRenderBlock(RenderBlockScreenEffectEvent event) {
        BakedModel model = event.getModel();

        if (!(model instanceof EmissiveBakedModel emissive)) {
            return;
        }

        MultiBufferSource.BufferSource buffers = event.getRenderBuffers().bufferSource();
        VertexConsumer consumer = buffers.getBuffer(ModRenderTypes.EMISSIVE);

        BlockState state = event.getBlockState();
        RandomSource random = event.getRandom();

        for (BakedQuad quad : emissive.getEmissiveQuads(
                state,
                event.getSide(),
                random
        )) {
            consumer.putBulkData(
                    event.getPoseStack().last(),
                    quad,
                    1f, 1f, 1f,
                    EmissiveBakedModel.FULL_BRIGHT,
                    event.getOverlay()
            );
        }
    }
}
