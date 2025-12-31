package net.continuumuniverses.network.payload;

import net.continuumuniverses.ContinuumUniverses;
import net.continuumuniverses.world.dimensions.uvla.KairaLunarEvent;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record UvlaKairaEventPayload(KairaLunarEvent event) implements CustomPacketPayload {

    public static final Type<UvlaKairaEventPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(ContinuumUniverses.MODID, "uvla_kaira_event"));

    public static final StreamCodec<RegistryFriendlyByteBuf, UvlaKairaEventPayload> STREAM_CODEC =
            StreamCodec.of(
                    (buf, payload) -> buf.writeVarInt(payload.event().ordinal()),
                    buf -> {
                        int ord = buf.readVarInt();
                        KairaLunarEvent[] values = KairaLunarEvent.values();
                        KairaLunarEvent ev = (ord >= 0 && ord < values.length) ? values[ord] : KairaLunarEvent.NONE;
                        return new UvlaKairaEventPayload(ev);
                    }
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
