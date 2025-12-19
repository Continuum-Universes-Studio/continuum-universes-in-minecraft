package net.jensensagastudio.continuumuniverses.network;

import net.jensensagastudio.continuumuniverses.network.payload.UvlaKairaEventPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.event.RegisterClientPayloadHandlersEvent;

@EventBusSubscriber(modid = "continuumuniverses", bus = EventBusSubscriber.Bus.MOD)
public final class ModPayloads {

    private ModPayloads() {}

    public static final String NETWORK_VERSION = "1";

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(NETWORK_VERSION);

        // Register the payload for play phase. Handler is registered separately client-side.
        registrar.play(
                UvlaKairaEventPayload.TYPE,
                UvlaKairaEventPayload.STREAM_CODEC
        );
    }

    @SubscribeEvent
    public static void registerClientHandlers(RegisterClientPayloadHandlersEvent event) {
        event.register(
                UvlaKairaEventPayload.TYPE,
                (payload, context) -> {
                    context.enqueueWork(() ->
                            net.jensensagastudio.continuumuniverses.client.ClientLunarState.setKairaEvent(payload.event())
                    );
                }
        );
    }
}
