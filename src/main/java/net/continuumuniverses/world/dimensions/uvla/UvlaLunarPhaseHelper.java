package net.continuumuniverses.world.dimensions.uvla;

import net.minecraft.world.level.Level;

public final class UvlaLunarPhaseHelper {
    private UvlaLunarPhaseHelper() {}

    public static int getKairaPhase(Level level) {
        float days = level.getDayTime() / 24000.0F;
        float cycle = (days / UvlaMoons.KAIRA.orbitalPeriodDays()) % 1.0F;
        return (int) (cycle * UvlaMoons.KAIRA.phaseCount());
    }

    public static boolean isKairaFull(Level level) {
        return getKairaPhase(level) == 0;
    }
}
