package net.jensensagastudio.continuumuniverses.client.sky;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.jensensagastudio.continuumuniverses.world.dimensions.uvla.UvlaMoon;
import net.jensensagastudio.continuumuniverses.world.dimensions.uvla.UvlaMoons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.AtlasManager;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

public class UvlaSkyRenderer extends SkyRenderer {

    private final TextureAtlas celestialsAtlas;
    private final GpuBuffer moonBuffer;
    private final RenderSystem.AutoStorageIndexBuffer quadIndices;

    public UvlaSkyRenderer(TextureManager textureManager, AtlasManager atlasManager) {
        super(textureManager, atlasManager);
        this.celestialsAtlas = atlasManager.getAtlasOrThrow(AtlasIds.CELESTIALS);
        this.moonBuffer = buildMoonQuads();
        this.quadIndices = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
    }

    /* ==========================================================
       Entry Point
       ========================================================== */

    public void render(
            PoseStack poseStack,
            Matrix4f projectionMatrix,
            float partialTick,
            Level level,
            Runnable setupFog
    ) {
        setupFog.run();
        renderUvlaMoons(poseStack, partialTick, level);
    }

    /* ==========================================================
       Moon Rendering
       ========================================================== */

    private void renderUvlaMoons(
            PoseStack poseStack,
            float partialTick,
            Level level
    ) {
        Matrix4fStack modelView = RenderSystem.getModelViewStack();
        modelView.pushMatrix();
        modelView.mul(poseStack.last().pose());

        var device = RenderSystem.getDevice();
        var target = Minecraft.getInstance().getMainRenderTarget();

        for (UvlaMoon moon : UvlaMoons.ALL) {
            float angle = getMoonOrbitAngle(level, partialTick, moon);
            int phaseIndex = getMoonPhase(level, partialTick, moon);
            renderSingleMoon(modelView, angle, phaseIndex);
        }

        modelView.popMatrix();
    }

    private void renderSingleMoon(
            Matrix4fStack modelView,
            float angle,
            int phaseIndex
    ) {
        var device = RenderSystem.getDevice();
        var target = Minecraft.getInstance().getMainRenderTarget();

        GpuBufferSlice uniforms =
                RenderSystem.getDynamicUniforms().writeTransform(
                        modelView,
                        new Vector4f(1f, 1f, 1f, 1f),
                        new Vector3f(),
                        new Matrix4f()
                );

        try (RenderPass pass = device.createCommandEncoder()
                .createRenderPass(
                        () -> "Uvla moon",
                        target.getColorTextureView(),
                        null,
                        target.getDepthTextureView(),
                        null
                )) {

            pass.setPipeline(RenderPipelines.CELESTIAL);
            RenderSystem.bindDefaultUniforms(pass);
            pass.setUniform("DynamicTransforms", uniforms);

            // draw calls here
        }
    }


    /* ==========================================================
       GPU Buffer Construction
       ========================================================== */

    private GpuBuffer buildMoonQuads() {
        List<UvlaMoon> moons = UvlaMoons.ALL;
        VertexFormat format = DefaultVertexFormat.POSITION_TEX;

        try (ByteBufferBuilder builder =
                     ByteBufferBuilder.exactlySized(moons.size() * 4 * format.getVertexSize())) {

            BufferBuilder buffer = new BufferBuilder(builder, VertexFormat.Mode.QUADS, format);

            for (UvlaMoon moon : moons) {
                for (int i = 0; i < moon.phaseCount(); i++) {
                    TextureAtlasSprite sprite =
                            celestialsAtlas.getSprite(moon.phaseSprite(i));

                    buffer.addVertex(-1, 0, -1).setUv(sprite.getU0(), sprite.getV1());
                    buffer.addVertex( 1, 0, -1).setUv(sprite.getU1(), sprite.getV1());
                    buffer.addVertex( 1, 0,  1).setUv(sprite.getU1(), sprite.getV0());
                    buffer.addVertex(-1, 0,  1).setUv(sprite.getU0(), sprite.getV0());
                }
            }

            try (MeshData mesh = buffer.buildOrThrow()) {
                return RenderSystem.getDevice().createBuffer(
                        () -> "Uvla moon buffer",
                        32,
                        mesh.vertexBuffer()
                );
            }
        }
    }

    /* ==========================================================
       Orbital Math
       ========================================================== */

    private float getMoonOrbitAngle(Level level, float partialTick, UvlaMoon moon) {
        float days = (level.getDayTime() + partialTick) / 24000.0F;
        float fraction = (days / moon.orbitalPeriodDays()) % 1.0F;
        return fraction * 360.0F + moon.angleOffsetDeg();
    }

    private int getMoonPhase(Level level, float partialTick, UvlaMoon moon) {
        float days = (level.getDayTime() + partialTick) / 24000.0F;
        float cycle = (days / moon.orbitalPeriodDays()) % 1.0F;   // 0..1
        int phase = (int)(cycle * moon.phaseCount());             // 0..15
        return Mth.clamp(phase, 0, moon.phaseCount() - 1);
    }

    /* ==========================================================
       Cleanup
       ========================================================== */

    @Override
    public void close() {
        moonBuffer.close();
    }
}
