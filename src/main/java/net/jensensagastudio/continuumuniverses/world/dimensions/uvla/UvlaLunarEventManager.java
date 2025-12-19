package net.jensensagastudio.continuumuniverses.world.dimensions.uvla;

import net.jensensagastudio.continuumuniverses.network.payload.UvlaKairaEventPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = "continuumuniverses")
public final class UvlaLunarEventManager {

    // Adjust these to taste
    private static final int BLOOD_CHANCE_PERCENT = 5;
    private static final int BLUE_CHANCE_PERCENT  = 5;

    // Sunset moment to roll (vanilla-ish night transition)
    private static final long ROLL_TIME = 13000L;

    private UvlaLunarEventManager() {}

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (!isUvla(level)) return;

        long dayTime = level.getDayTime();
        long timeOfDay = dayTime % 24000L;
        long day = dayTime / 24000L;

        if (timeOfDay != ROLL_TIME) return;

        UvlaLunarSavedData data = UvlaLunarSavedData.get(level);
        if (data.getLastRolledDay() == day) return; // already rolled this day

        KairaLunarEvent rolled = rollKairaEvent(level);
        data.setKairaEvent(rolled);
        data.setLastRolledDay(day);

        syncToDimension(level, rolled);
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!isUvla(player.level())) return;

        syncToPlayer(player, UvlaLunarSavedData.get((ServerLevel) player.level()).getKairaEvent());
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!isUvla(player.level())) return;

        syncToPlayer(player, UvlaLunarSavedData.get((ServerLevel) player.level()).getKairaEvent());
    }

    private static KairaLunarEvent rollKairaEvent(ServerLevel level) {
        // Only allow special moons when Kaira is FULL
        if (!isKairaFull(level)) return KairaLunarEvent.NONE;

        int roll = level.random.nextInt(100); // 0..99

        if (roll < BLOOD_CHANCE_PERCENT) return KairaLunarEvent.BLOOD;
        if (roll < BLOOD_CHANCE_PERCENT + BLUE_CHANCE_PERCENT) return KairaLunarEvent.BLUE;

        return KairaLunarEvent.NONE;
    }

    private static boolean isKairaFull(ServerLevel level) {
        // Simple baseline: full when phase == 0 (or pick your own)
        // Your moon phase logic can replace this easily.
        long day = level.getDayTime() / 24000L;
        int phaseCount = 8; // match your sprite sheet
        int phase = (int) (day % phaseCount);
        return phase == 0;
    }

    private static void syncToDimension(ServerLevel level, KairaLunarEvent event) {
        PacketDistributor.sendToPlayersInDimension(level, new UvlaKairaEventPayload(event));
    }

    private static void syncToPlayer(ServerPlayer player, KairaLunarEvent event) {
        PacketDistributor.sendToPlayer(player, new UvlaKairaEventPayload(event));
    }

    private static boolean isUvla(Level level) {
        // TODO: replace this with your real UVLA key check.
        // Example:
        // return level.dimension().equals(ModDimensions.UVLA);

        return level.dimension().location().getNamespace().equals("continuumuniverses")
                && level.dimension().location().getPath().equals("uvla");
    }
}
