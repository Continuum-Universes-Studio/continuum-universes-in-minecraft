package net.continuumuniverses.network;

import net.continuumuniverses.ContinuumUniverses;
import net.continuumuniverses.network.payload.UvlaKairaEventPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@EventBusSubscriber(modid = ContinuumUniverses.MODID)
public final class ModPayloads {
    private ModPayloads() {}

    public static final String NETWORK_VERSION = "1";

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(NETWORK_VERSION);

        // ✅ This payload is allowed to be sent from server -> client
        registrar.playToClient(
                UvlaKairaEventPayload.TYPE,
                UvlaKairaEventPayload.STREAM_CODEC
        );
    }

    @EventBusSubscriber(modid = ContinuumUniverses.MODID, value = Dist.CLIENT)
    public static final class ClientHandlers {
        private ClientHandlers() {}

        @SubscribeEvent
        public static void registerClientHandlers(RegisterClientPayloadHandlersEvent event) {
            event.register(
                    UvlaKairaEventPayload.TYPE,
                    (payload, context) -> context.enqueueWork(() ->
                            net.continuumuniverses.world.dimensions.uvla.UvlaSkyRenderer
                                    .setClientKairaEvent(payload.event())
                    )
            );
        }
    }
}
