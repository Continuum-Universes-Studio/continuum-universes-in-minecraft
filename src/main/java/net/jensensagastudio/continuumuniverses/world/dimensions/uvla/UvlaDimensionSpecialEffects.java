package net.jensensagastudio.continuumuniverses.world.dimensions.uvla;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.extensions.IDimensionSpecialEffectsExtension;
import org.joml.Matrix4f;

public final class UvlaDimensionEffects extends DimensionSpecialEffects
        implements IDimensionSpecialEffectsExtension {

    private final UvlaSkyRenderer sky;

    public UvlaDimensionEffects() {
        super(
                192.0F,                 // cloudLevel like overworld-ish
                true,                   // hasGround
                SkyType.OVERWORLD,         // sky type
                false,                  // forceBrightLightmap
                false                   // constantAmbientLight
        );

        var mc = Minecraft.getInstance();
        this.sky = new UvlaSkyRenderer(
                mc.getTextureManager(),
                mc.getModelManager().getAtlasManager()
        );
    }

    @Override
    public boolean renderSky(
            ClientLevel level,
            int renderDistance,
            float partialTick,
            PoseStack poseStack,
            Matrix4f projectionMatrix,
            Vec3 cameraPos,
            Runnable setupFog
    ) {
        // call your sky renderer
        sky.render(poseStack, projectionMatrix, partialTick, level, setupFog);

        // true = “we rendered sky, don’t render vanilla sky”
        return true;
    }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 fogColor, float brightness) {
        return null;
    }
}

