package net.continuumuniverses.world.dimensions.uvla;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.state.SkyRenderState;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.state.LevelRenderState;
import net.minecraft.client.renderer.state.WeatherRenderState;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.AtlasManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.extensions.IDimensionSpecialEffectsExtension;
import net.neoforged.neoforge.client.event.ExtractLevelRenderStateEvent;
import org.joml.Matrix4f;

/**
 * Custom DimensionSpecialEffects for the Uvla dimension.
 * Disables vanilla sky/cloud/rain rendering and delegates sky rendering to UvlaSkyRenderer.
 */
public class UvlaDimensionEffects extends DimensionSpecialEffects implements IDimensionSpecialEffectsExtension {

    private final UvlaSkyRenderer skyRenderer;

    public UvlaDimensionEffects() {
        super(
                /* skyType */ SkyType.OVERWORLD,
                /* constantAmbientLight */ false,
                /* alternateSkyColor */ false
        );
        // Create our custom sky renderer using the client’s texture and atlas managers
        Minecraft minecraft = Minecraft.getInstance();
        TextureManager textureManager = minecraft.getTextureManager();
        AtlasManager atlasManager = minecraft.getAtlasManager();
        this.skyRenderer = new UvlaSkyRenderer(textureManager);
    }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 fogColor, float sunHeight) {
        return fogColor; // keep vanilla fog colour
    }

    @Override
    public boolean isFoggyAt(int x, int z) {
        return false; // Uvla has no dense fog
    }

    @Override
    public boolean renderClouds(LevelRenderState levelRenderState,
                                Vec3 camPos,
                                net.minecraft.client.CloudStatus cloudStatus,
                                int cloudColor,
                                float cloudHeight,
                                Matrix4f modelViewMatrix) {
        return true; // prevent vanilla cloud rendering
    }

    @Override
    public boolean renderSky(LevelRenderState levelRenderState, SkyRenderState skyRenderState, Matrix4f modelViewMatrix, Runnable setupFog) {
        return this.skyRenderer.renderSky(levelRenderState, skyRenderState, modelViewMatrix, setupFog);
    }


    @Override
    public boolean renderSnowAndRain(LevelRenderState levelRenderState,
                                     WeatherRenderState weatherRenderState,
                                     MultiBufferSource bufferSource,
                                     Vec3 camPos) {
        return true; // disable vanilla precipitation
    }

    @Override
    public boolean tickRain(net.minecraft.client.multiplayer.ClientLevel level, int ticks, net.minecraft.client.Camera camera) {
        return true; // disable vanilla rain tick logic
    }
}
