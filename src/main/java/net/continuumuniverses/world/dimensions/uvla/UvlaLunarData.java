package net.continuumuniverses.world.dimensions.uvla;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;


public class UvlaLunarData extends SavedData {

    public static final String ID = "uvla_lunar_data";

    // Safe enum codec: unknown strings -> NONE
    private static final Codec<KairaLunarEvent> KAIRA_EVENT_CODEC =
            Codec.STRING.xmap(
                    s -> {
                        try { return KairaLunarEvent.valueOf(s); }
                        catch (Exception ignored) { return KairaLunarEvent.NONE; }
                    },
                    KairaLunarEvent::name
            );

    public static final Codec<UvlaLunarData> CODEC =
            RecordCodecBuilder.create(inst -> inst.group(
                    KAIRA_EVENT_CODEC.fieldOf("KairaEvent")
                            .orElse(KairaLunarEvent.NONE)
                            .forGetter(d -> d.kairaEvent)
            ).apply(inst, UvlaLunarData::new));

    public static final SavedDataType<UvlaLunarData> TYPE =
            new SavedDataType<>(ID, UvlaLunarData::new, CODEC /*, null */);

    private KairaLunarEvent kairaEvent = KairaLunarEvent.NONE;

    // Used when the data file doesn't exist yet
    public UvlaLunarData() {}

    // Used by the codec
    private UvlaLunarData(KairaLunarEvent event) {
        this.kairaEvent = event;
    }

    public static UvlaLunarData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(TYPE);
    }

    public KairaLunarEvent getKairaEvent() {
        return kairaEvent;
    }

    public void setKairaLunarEvent(KairaLunarEvent event) {
        this.kairaEvent = (event == null) ? KairaLunarEvent.NONE : event;
        setDirty();
    }
}
