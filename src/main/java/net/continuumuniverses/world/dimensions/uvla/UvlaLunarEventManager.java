package net.continuumuniverses.world.dimensions.uvla;

import net.continuumuniverses.network.payload.UvlaKairaEventPayload;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = "continuumuniverses")
public final class UvlaLunarEventManager {

    // Adjust these to taste
    private static final int BLOOD_CHANCE_PERCENT = 10;
    private static final int BLUE_CHANCE_PERCENT  = 5;

    // Sunset moment to roll (vanilla-ish night transition)
    private static final long ROLL_TIME = 13000L;
    private static final long[] RAIN_SLOTS = {2000L, 10000L, 18000L};
    private static final int RAIN_DURATION_TICKS = 2400;
    private static final float ALIGNMENT_THRESHOLD_DEG = 2.0F;

    private UvlaLunarEventManager() {}

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (!isUvla(level)) return;

        long dayTime = level.getDayTime();
        long timeOfDay = dayTime % 24000L;
        long day = dayTime / 24000L;

        UvlaLunarSavedData data = UvlaLunarSavedData.get(level);

        if (timeOfDay != ROLL_TIME) return;

        if (data.getLastRolledDay() == day) return; // already rolled this day

        KairaLunarEvent rolled = rollKairaEvent(level);
        data.setKairaEvent(rolled);
        data.setLastRolledDay(day);

        syncToDimension(level, rolled);
    }

    @SubscribeEvent
    public static void onLevelTickWeather(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (!isUvla(level)) return;

        long dayTime = level.getDayTime();
        long timeOfDay = dayTime % 24000L;
        long day = dayTime / 24000L;

        UvlaLunarSavedData data = UvlaLunarSavedData.get(level);

        if (data.getRainDay() != day) {
            data.setRainDay(day);
        }

        for (int i = 0; i < RAIN_SLOTS.length; i++) {
            long slotTime = RAIN_SLOTS[i];
            int slotBit = 1 << i;
            if (timeOfDay == slotTime && (data.getRainSlots() & slotBit) == 0) {
                level.setWeatherParameters(0, RAIN_DURATION_TICKS, true, false);
                data.markRainSlot(slotBit);
            }
        }

        if (!data.hasAlignmentOccurred() && isMoonsAligned(level)) {
            data.setAlignmentOccurred(true, day);
            level.players().forEach(player ->
                    player.sendSystemMessage(Component.literal("The moons of Uvla align in a once-in-a-lifetime event."))
            );
        }
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
        if (!UvlaLunarPhaseHelper.isKairaFull(level)) return KairaLunarEvent.NONE;

        int roll = level.random.nextInt(100); // 0..99

        if (roll < BLOOD_CHANCE_PERCENT) return KairaLunarEvent.BLOOD;
        if (roll < BLOOD_CHANCE_PERCENT + BLUE_CHANCE_PERCENT) return KairaLunarEvent.BLUE;

        return KairaLunarEvent.NONE;
    }

    private static boolean isMoonsAligned(ServerLevel level) {
        float days = level.getDayTime() / 24000.0F;
        float baseAngle = getMoonOrbitAngle(days, UvlaMoons.KAIRA);

        for (UvlaMoon moon : UvlaMoons.ALL) {
            float angle = getMoonOrbitAngle(days, moon);
            float delta = Math.abs(Mth.wrapDegrees(angle - baseAngle));
            if (delta > ALIGNMENT_THRESHOLD_DEG) {
                return false;
            }
        }

        return true;
    }

    private static float getMoonOrbitAngle(float days, UvlaMoon moon) {
        float fraction = (days / moon.orbitalPeriodDays()) % 1.0F;
        return fraction * 360.0F + moon.angleOffsetDeg();
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
