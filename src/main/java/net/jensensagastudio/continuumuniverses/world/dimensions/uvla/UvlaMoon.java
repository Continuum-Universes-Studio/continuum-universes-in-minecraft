package net.jensensagastudio.continuumuniverses.world.dimensions.uvla;

import net.jensensagastudio.continuumuniverses.ContinuumUniverses;
import net.minecraft.resources.Identifier;

public record UvlaMoon(
        String name,
        Identifier phaseTexture,
        int phaseCount,
        float angleOffsetDeg,
        float size,
        float distanceFromUvla,
        float orbitalPeriodDays
) {
    public float height() {
        return 100.0F;
    }
    public Identifier phaseSprite(int phase) {
        return Identifier.fromNamespaceAndPath(
                ContinuumUniverses.MODID, "moon/" + name + "_" + phase
        );
    }

}
