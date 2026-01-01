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
                            .forGetter(sd -> sd.lastRolledDay),
                    Codec.BOOL.fieldOf("AlignmentOccurred")
                            .orElse(false)
                            .forGetter(sd -> sd.alignmentOccurred),
                    Codec.LONG.fieldOf("AlignmentDay")
                            .orElse(-1L)
                            .forGetter(sd -> sd.alignmentDay),
                    Codec.LONG.fieldOf("RainDay")
                            .orElse(-1L)
                            .forGetter(sd -> sd.rainDay),
                    Codec.INT.fieldOf("RainSlots")
                            .orElse(0)
                            .forGetter(sd -> sd.rainSlots)
            ).apply(instance, UvlaLunarSavedData::new));

    // NeoForge docs note: DataFixTypes parameter exists but can be null in their patched vanilla use cases.
    public static final SavedDataType<UvlaLunarSavedData> TYPE =
            new SavedDataType<>(ID, UvlaLunarSavedData::new, CODEC /*, null */);

    private KairaLunarEvent kairaEvent = KairaLunarEvent.NONE;
    private long lastRolledDay = -1L;
    private boolean alignmentOccurred = false;
    private long alignmentDay = -1L;
    private long rainDay = -1L;
    private int rainSlots = 0;

    // Default constructor (used when no data exists yet)
    public UvlaLunarSavedData() {}

    // “data constructor” (used by the codec)
    public UvlaLunarSavedData(KairaLunarEvent event,
                              long lastRolledDay,
                              boolean alignmentOccurred,
                              long alignmentDay,
                              long rainDay,
                              int rainSlots) {
        this.kairaEvent = event;
        this.lastRolledDay = lastRolledDay;
        this.alignmentOccurred = alignmentOccurred;
        this.alignmentDay = alignmentDay;
        this.rainDay = rainDay;
        this.rainSlots = rainSlots;
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

    public boolean hasAlignmentOccurred() {
        return alignmentOccurred;
    }

    public long getAlignmentDay() {
        return alignmentDay;
    }

    public void setAlignmentOccurred(boolean occurred, long day) {
        this.alignmentOccurred = occurred;
        this.alignmentDay = day;
        this.setDirty();
    }

    public long getRainDay() {
        return rainDay;
    }

    public int getRainSlots() {
        return rainSlots;
    }

    public void setRainDay(long day) {
        this.rainDay = day;
        this.rainSlots = 0;
        this.setDirty();
    }

    public void markRainSlot(int slotBit) {
        this.rainSlots |= slotBit;
        this.setDirty();
    }

    public static UvlaLunarSavedData get(ServerLevel level) {
        // 1.21.10 style: computeIfAbsent takes the SavedDataType
        return level.getDataStorage().computeIfAbsent(TYPE);
    }
}
