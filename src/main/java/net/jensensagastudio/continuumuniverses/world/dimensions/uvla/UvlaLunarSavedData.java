package net.jensensagastudio.continuumuniverses.world.dimensions.uvla;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.server.level.ServerLevel;

public class UvlaLunarSavedData extends SavedData {

    public static final String FILE_NAME = "uvla_lunar";

    private KairaLunarEvent kairaEvent = KairaLunarEvent.NONE;
    private long lastRolledDay = -1L; // prevent double-rolls in same day

    public KairaLunarEvent getKairaEvent() {
        return kairaEvent;
    }

    public void setKairaEvent(KairaLunarEvent event) {
        this.kairaEvent = event;
        setDirty();
    }

    public long getLastRolledDay() {
        return lastRolledDay;
    }

    public void setLastRolledDay(long day) {
        this.lastRolledDay = day;
        setDirty();
    }

    public static UvlaLunarSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(factory(), FILE_NAME);
    }

    private static SavedData.Factory<UvlaLunarSavedData> factory() {
        return new SavedData.Factory<>(
                UvlaLunarSavedData::new,
                UvlaLunarSavedData::load,
                DataFixTypes.LEVEL
        );
    }

    public static UvlaLunarSavedData load(CompoundTag tag, HolderLookup.Provider registries) {
        UvlaLunarSavedData data = new UvlaLunarSavedData();

        String raw = tag.getString("KairaEvent");
        try {
            data.kairaEvent = KairaLunarEvent.valueOf(raw);
        } catch (Exception ignored) {
            data.kairaEvent = KairaLunarEvent.NONE;
        }

        data.lastRolledDay = tag.getLong("LastRolledDay");
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putString("KairaEvent", kairaEvent.name());
        tag.putLong("LastRolledDay", lastRolledDay);
        return tag;
    }
}
