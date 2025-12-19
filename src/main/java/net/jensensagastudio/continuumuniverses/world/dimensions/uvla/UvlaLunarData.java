package net.jensensagastudio.continuumuniverses.world.dimensions.uvla;

import net.jensensagastudio.continuumuniverses.client.sky.UvlaSkyRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class UvlaLunarData extends SavedData {

    private UvlaSkyRenderer.KairaLunarEvent kairaEvent = UvlaSkyRenderer.KairaLunarEvent.NONE;

    public static UvlaLunarData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                UvlaLunarData::load,
                UvlaLunarData::new,
                "uvla_lunar_data"
        );
    }

    public UvlaSkyRenderer.KairaLunarEvent getKairaEvent() {
        return kairaEvent;
    }

    public void setKairaEvent(UvlaSkyRenderer.KairaLunarEvent event) {
        this.kairaEvent = event;
        setDirty();
    }

    public static UvlaLunarData load(CompoundTag tag) {
        UvlaLunarData data = new UvlaLunarData();
        data.kairaEvent = UvlaSkyRenderer.KairaLunarEvent.valueOf(
                tag.getString("KairaEvent")
        );
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putString("KairaEvent", kairaEvent.name());
        return tag;
    }
}
