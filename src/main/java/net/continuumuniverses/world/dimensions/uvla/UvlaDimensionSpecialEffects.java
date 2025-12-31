package net.continuumuniverses.world.dimensions.uvla;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.state.LevelRenderState;
import net.minecraft.client.renderer.state.SkyRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public final class UvlaDimensionSpecialEffects extends DimensionSpecialEffects {

    private final UvlaSkyRenderer skyRenderer;

    public UvlaDimensionSpecialEffects() {
        super(SkyType.NONE, false, false); // use custom renderer, skip vanilla sky
        this.skyRenderer = new UvlaSkyRenderer(Minecraft.getInstance().getTextureManager());
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

    @Override
    public boolean renderSky(LevelRenderState levelRenderState, SkyRenderState skyRenderState, Matrix4f modelViewMatrix, Runnable setupFog) {
        if (Minecraft.getInstance().level == null) {
            return false;
        }

        PoseStack poseStack = new PoseStack();
        poseStack.mulPose(modelViewMatrix);
        skyRenderer.render(poseStack, modelViewMatrix, Minecraft.getInstance().getFrameTimeNs(), Minecraft.getInstance().level, setupFog);
        return true;
    }
}
