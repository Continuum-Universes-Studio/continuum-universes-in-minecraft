package net.continuumuniverses.world.dimensions.uvla;

import net.continuumuniverses.ContinuumUniverses;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public record UvlaMoon(
        String id,              // "kaira"
        int phaseCount,         // 16
        float angleOffsetDeg,   // starting angle offset
        float orbitTiltDeg,     // tilt of orbital plane
        float orbitalPeriodDays,
        float size,             // your scale factor, if you want it
        float brightness,       // if you want it later
        float distance          // if you want it later
) {
    public ResourceLocation phaseTexture(int phaseIndex) {
        int i = Mth.clamp(phaseIndex, 0, phaseCount - 1);

        // Put your PNGs here:
        // assets/continuumuniverses/textures/sky/uvla/moons/kaira/phase_0.png
        return ResourceLocation.fromNamespaceAndPath(
                ContinuumUniverses.MODID,
                "textures/sky/uvla/moons/" + id + "/phase_" + i + ".png"
        );
    }
}
