package net.continuumuniverses.world.dimensions.uvla;

import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class UvlaDimensionSpecialEffects extends DimensionSpecialEffects {

    public UvlaDimensionSpecialEffects() {
        super(SkyType.NONE, false, false); // NONE = don't draw vanilla sky
    }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 fogColor, float brightness) {
        // Vanilla-ish behavior: keep a little color even at low brightness
        double s = (double)(Mth.clamp(brightness, 0.0F, 1.0F) * 0.94F + 0.06F);
        return fogColor.scale(s);
    }

    @Override
    public boolean isFoggyAt(int x, int z) {
        return false;
    }
}
