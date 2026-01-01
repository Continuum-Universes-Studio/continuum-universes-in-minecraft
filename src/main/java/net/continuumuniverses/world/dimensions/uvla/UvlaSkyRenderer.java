package net.continuumuniverses.world.dimensions.uvla;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.LevelRenderState;
import net.minecraft.client.renderer.state.SkyRenderState;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.extensions.IDimensionSpecialEffectsExtension;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.joml.Vector4f;
import net.minecraft.client.multiplayer.ClientLevel;
import java.util.OptionalDouble;
import java.util.OptionalInt;

public final class UvlaSkyRenderer implements IDimensionSpecialEffectsExtension, AutoCloseable {

    /* ==========================================================
       Client lunar state
       ========================================================== */

    private static volatile KairaLunarEvent CLIENT_EVENT = KairaLunarEvent.NONE;

    public static void setClientKairaEvent(KairaLunarEvent e) {
        CLIENT_EVENT = (e == null) ? KairaLunarEvent.NONE : e;
    }

    public static KairaLunarEvent getClientKairaEvent() {
        return CLIENT_EVENT;
    }

    /* ==========================================================
       Rendering resources
       ========================================================== */

    private final TextureManager textureManager;

    // Unit quad: POSITION_TEX, 4 verts
    private final GpuBuffer quadBuffer;

    // Shared quad indices (6 indices per quad)
    private final RenderSystem.AutoStorageIndexBuffer quadIndices = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);

    // Reuse constants to avoid per-frame allocations
    private static final Vector4f WHITE = new Vector4f(1f, 1f, 1f, 1f);
    private static final Vector3f ORIGIN = new Vector3f(0f, 0f, 0f);
    private static final Matrix4f IDENTITY_UV = new Matrix4f().identity();

    // Visual tuning
    private static final float MOON_SCALE = 30.0F;
    private static final float MOON_HEIGHT = 100.0F;

    public UvlaSkyRenderer(TextureManager textureManager) {
        this.textureManager = textureManager;
        this.quadBuffer = buildUnitQuad();
    }

    /* ==========================================================
       NeoForge hook: called by DimensionSpecialEffects
       ========================================================== */

    @Override
    public boolean renderSky(LevelRenderState levelRenderState, SkyRenderState skyRenderState, Matrix4f modelViewMatrix, Runnable setupFog) {
        setupFog.run();

        Matrix4fStack mv = RenderSystem.getModelViewStack();
        mv.pushMatrix();
        mv.mul(modelViewMatrix);

        try {
            float partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true);
            renderUvlaMoons(mv, (ClientLevel) Minecraft.getInstance().level, partialTick);
        } finally {
            mv.popMatrix();
        }

        return true; // <-- THIS is what blocks vanilla sky (sun/moon/stars)
    }


    /* ==========================================================
       Moon rendering
       ========================================================== */

    private void renderUvlaMoons(Matrix4fStack modelView, ClientLevel level, float partialTick) {
        for (UvlaMoon moon : UvlaMoons.ALL) {
            float angleDeg = getMoonOrbitAngle(level, partialTick, moon);
            int phaseIndex = getMoonPhase(level, partialTick, moon);
            renderSingleMoon(modelView, moon, angleDeg, phaseIndex);
        }
    }

    private void renderSingleMoon(Matrix4fStack baseModelView, UvlaMoon moon, float angleDeg, int phaseIndex) {
        // All transforms for this moon happen on a pushed matrix stack
        baseModelView.pushMatrix();
        try {
            // Sky-style orientation
            baseModelView.rotate(Axis.YP.rotationDegrees(-90.0F));
            baseModelView.rotate(Axis.XP.rotationDegrees(angleDeg));
            baseModelView.translate(0.0F, MOON_HEIGHT, 0.0F);
            baseModelView.scale(MOON_SCALE, 1.0F, MOON_SCALE);

            GpuBufferSlice transforms = RenderSystem.getDynamicUniforms().writeTransform(
                    baseModelView,
                    WHITE,
                    ORIGIN,
                    IDENTITY_UV,
                    1.0f
            );

            var device = RenderSystem.getDevice();
            var target = Minecraft.getInstance().getMainRenderTarget();

            AbstractTexture tex = textureManager.getTexture(moon.phaseTexture(phaseIndex));

            try (RenderPass pass = device.createCommandEncoder().createRenderPass(
                    () -> "Uvla moon",
                    target.getColorTextureView(),
                    OptionalInt.empty(),
                    target.getDepthTextureView(),
                    OptionalDouble.empty()
            )) {
                pass.setPipeline(RenderPipelines.CELESTIAL);
                RenderSystem.bindDefaultUniforms(pass);

                pass.setUniform("DynamicTransforms", transforms);
                pass.bindSampler("Sampler0", tex.getTextureView());

                pass.setVertexBuffer(0, quadBuffer);

                GpuBuffer indexBuffer = quadIndices.getBuffer(6);
                pass.setIndexBuffer(indexBuffer, quadIndices.type());

                // In this API, the signature is (firstIndex, baseVertex, indexCount, instanceCount)
                // (This matches how vanilla SkyRenderer calls it in 1.21.10.)
                pass.drawIndexed(0, 0, 6, 1);
            }
        } finally {
            baseModelView.popMatrix();
        }
    }

    /* ==========================================================
       GPU buffer construction
       ========================================================== */

    private static GpuBuffer buildUnitQuad() {
        VertexFormat format = DefaultVertexFormat.POSITION_TEX;
        int bytesNeeded = 4 * format.getVertexSize();

        try (ByteBufferBuilder builder = ByteBufferBuilder.exactlySized(bytesNeeded)) {
            BufferBuilder buffer = new BufferBuilder(builder, VertexFormat.Mode.QUADS, format);

            // Same winding/UV convention as vanilla celestial quads
            Matrix4f m = new Matrix4f();
            buffer.addVertex(m, -1, 0, -1).setUv(0f, 1f);
            buffer.addVertex(m,  1, 0, -1).setUv(1f, 1f);
            buffer.addVertex(m,  1, 0,  1).setUv(1f, 0f);
            buffer.addVertex(m, -1, 0,  1).setUv(0f, 0f);

            try (MeshData mesh = buffer.buildOrThrow()) {
                return RenderSystem.getDevice().createBuffer(
                        () -> "Uvla unit quad",
                        32,
                        mesh.vertexBuffer()
                );
            }
        }
    }

    /* ==========================================================
       Orbital math
       ========================================================== */

    private static float getMoonOrbitAngle(Level level, float partialTick, UvlaMoon moon) {
        float days = (level.getDayTime() + partialTick) / 24000.0F;
        float fraction = (days / moon.orbitalPeriodDays()) % 1.0F;
        return fraction * 360.0F + moon.angleOffsetDeg();
    }

    private static int getMoonPhase(Level level, float partialTick, UvlaMoon moon) {
        float days = (level.getDayTime() + partialTick) / 24000.0F;
        float cycle = (days / moon.orbitalPeriodDays()) % 1.0F;
        int phase = (int) (cycle * moon.phaseCount());
        return Mth.clamp(phase, 0, moon.phaseCount() - 1);
    }
    public void render(PoseStack poseStack, Matrix4f modelViewMatrix, long frameTimeNs, ClientLevel level, Runnable setupFog) {
        setupFog.run();

        // Neo/vanilla sky rendering uses RenderSystem's ModelViewStack.
        // We need to push the modelViewMatrix (the one MC gives for sky pass)
        // so our dynamic uniforms are correct.
        Matrix4fStack mv = RenderSystem.getModelViewStack();
        mv.pushMatrix();
        mv.mul(modelViewMatrix);

        try {
            // Convert ns -> partialTick-ish if you want motion to interpolate.
            // If you already compute from level time, you can ignore frameTimeNs.
            float partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true);

            renderUvlaMoons(mv, level, partialTick); // implement this signature
        } finally {
            mv.popMatrix();
        }
    }
    /* ==========================================================
       Cleanup
       ========================================================== */

    @Override
    public void close() {
        quadBuffer.close();
    }
}
