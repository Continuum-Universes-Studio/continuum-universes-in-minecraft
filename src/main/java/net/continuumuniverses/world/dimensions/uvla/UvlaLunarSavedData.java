package net.continuumuniverses.world.dimensions.uvla;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public class UvlaLunarSavedData extends SavedData {

    // This string becomes the path/name under the dimension's data folder.
    // (NeoForge docs: identifier maps into ./<world>/<dimension>/data/<identifier>.dat)
    public static final String ID = "uvla_lunar";

    // Robust enum codec: unknown strings -> NONE instead of crashing
    private static final Codec<KairaLunarEvent> KAIRA_EVENT_CODEC =
            Codec.STRING.xmap(
                    s -> {
                        try { return KairaLunarEvent.valueOf(s); }
                        catch (Exception ignored) { return KairaLunarEvent.NONE; }
                    },
                    KairaLunarEvent::name
            );

    public static final Codec<UvlaLunarSavedData> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    KAIRA_EVENT_CODEC.fieldOf("KairaEvent")
                            .orElse(KairaLunarEvent.NONE)
                            .forGetter(sd -> sd.kairaEvent),
                    Codec.LONG.fieldOf("LastRolledDay")
                            .orElse(-1L)
                            .forGetter(sd -> sd.lastRolledDay)
            ).apply(instance, UvlaLunarSavedData::new));

    // NeoForge docs note: DataFixTypes parameter exists but can be null in their patched vanilla use cases.
    public static final SavedDataType<UvlaLunarSavedData> TYPE =
            new SavedDataType<>(ID, UvlaLunarSavedData::new, CODEC /*, null */);

    private KairaLunarEvent kairaEvent = KairaLunarEvent.NONE;
    private long lastRolledDay = -1L;

    // Default constructor (used when no data exists yet)
    public UvlaLunarSavedData() {}

    // “data constructor” (used by the codec)
    public UvlaLunarSavedData(KairaLunarEvent event, long lastRolledDay) {
        this.kairaEvent = event;
        this.lastRolledDay = lastRolledDay;
    }

    public KairaLunarEvent getKairaEvent() {
        return kairaEvent;
    }

    public void setKairaEvent(KairaLunarEvent event) {
        this.kairaEvent = event;
        this.setDirty();
    }

    public long getLastRolledDay() {
        return lastRolledDay;
    }

    public void setLastRolledDay(long day) {
        this.lastRolledDay = day;
        this.setDirty();
    }

    public static UvlaLunarSavedData get(ServerLevel level) {
        // 1.21.10 style: computeIfAbsent takes the SavedDataType
        return level.getDataStorage().computeIfAbsent(TYPE);
    }
}
