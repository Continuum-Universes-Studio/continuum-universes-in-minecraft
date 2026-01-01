package net.continuumuniverses.world.dimensions.uvla;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.state.LevelRenderState;
import net.minecraft.client.renderer.state.SkyRenderState;
import net.minecraft.client.renderer.state.WeatherRenderState;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.extensions.IDimensionSpecialEffectsExtension;
import org.joml.Matrix4f;

/**
 * Custom DimensionSpecialEffects for the Uvla dimension.
 * - Blocks vanilla sky/cloud/rain rendering
 * - Delegates celestial rendering to {@link UvlaSkyRenderer}
 */
public final class UvlaDimensionEffects extends DimensionSpecialEffects implements IDimensionSpecialEffectsExtension {

    private final UvlaSkyRenderer skyRenderer;

    public UvlaDimensionEffects() {
        super(
                /* skyType */ SkyType.OVERWORLD,
                /* constantAmbientLight */ false,
                /* alternateSkyColor */ false
        );

        Minecraft mc = Minecraft.getInstance();
        TextureManager textureManager = mc.getTextureManager();
        this.skyRenderer = new UvlaSkyRenderer(textureManager);
    }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 fogColor, float sunHeight) {
        return fogColor;
    }

    @Override
    public boolean isFoggyAt(int x, int z) {
        return false;
    }

    /**
     * Return true to say “handled” and block vanilla clouds.
     */
    @Override
    public boolean renderClouds(LevelRenderState levelRenderState,
                                Vec3 camPos,
                                net.minecraft.client.CloudStatus cloudStatus,
                                int cloudColor,
                                float cloudHeight,
                                Matrix4f modelViewMatrix) {
        return true;
    }

    /**
     * Main hook: render custom sky and return true to block vanilla sun/moon/stars.
     */
    @Override
    public boolean renderSky(LevelRenderState levelRenderState,
                             SkyRenderState skyRenderState,
                             Matrix4f modelViewMatrix,
                             Runnable setupFog) {

        // Defensive: if we're somehow called without a level, just block vanilla sky.
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            setupFog.run();
            return true;
        }

        // UvlaSkyRenderer already does rotation-only + pushes RenderSystem model-view stack.
        skyRenderer.render(level, modelViewMatrix, setupFog);

        return true; // blocks vanilla sun/moon/stars
    }

    /**
     * Return true to say “handled” and block vanilla precipitation rendering.
     */
    @Override
    public boolean renderSnowAndRain(LevelRenderState levelRenderState,
                                     WeatherRenderState weatherRenderState,
                                     MultiBufferSource bufferSource,
                                     Vec3 camPos) {
        return true;
    }

    /**
     * Return true to say “handled” and block vanilla rain tick logic.
     */
    @Override
    public boolean tickRain(net.minecraft.client.multiplayer.ClientLevel level,
                            int ticks,
                            net.minecraft.client.Camera camera) {
        return true;
    }

    /**
     * Optional manual cleanup hook (DimensionSpecialEffects has no lifecycle callback).
     * Call this from your client shutdown/reload handler if you want to free GPU buffers.
     */
    public void closeRenderer() {
        try {
            skyRenderer.close();
        } catch (Exception ignored) {
        }
    }
}
