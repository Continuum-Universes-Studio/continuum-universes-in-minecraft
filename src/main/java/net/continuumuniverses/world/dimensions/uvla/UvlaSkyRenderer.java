package net.continuumuniverses.world.dimensions.uvla;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.OptionalDouble;
import java.util.OptionalInt;

public final class UvlaSkyRenderer implements AutoCloseable {

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

    private static final Vector4f WHITE = new Vector4f(1f, 1f, 1f, 1f);
    private static final Vector3f ORIGIN = new Vector3f(0f, 0f, 0f);
    private static final Matrix4f IDENTITY_UV = new Matrix4f().identity();

    private static final float MOON_SCALE = 30.0F;
    private static final float MOON_HEIGHT = 100.0F;

    private final TextureManager textureManager;
    private final GpuBuffer quadBuffer;

    // shared sequential index generator for QUADS -> indices
    private final RenderSystem.AutoStorageIndexBuffer quadIndices =
            RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);

    public UvlaSkyRenderer(TextureManager textureManager) {
        this.textureManager = textureManager;
        this.quadBuffer = buildUnitQuad();
    }

    /* ==========================================================
       Public entry point
       ========================================================== */

    /**
     * Call this from your DimensionSpecialEffects renderSky(...) implementation.
     *
     * @param modelViewMatrix the sky pass modelView matrix provided by MC/NeoForge
     */
    public void render(ClientLevel level, Matrix4f modelViewMatrix, Runnable setupFog) {
        setupFog.run();

        // Use MC's sky pass matrix, but remove translation so the sky doesn't "parallax drift"
        Matrix4f rotOnly = new Matrix4f(modelViewMatrix);
        rotOnly.m30(0f);
        rotOnly.m31(0f);
        rotOnly.m32(0f);

        Matrix4fStack mv = RenderSystem.getModelViewStack();
        mv.pushMatrix();
        try {
            mv.mul(rotOnly);

            float partialTick = Minecraft.getInstance()
                    .getDeltaTracker()
                    .getGameTimeDeltaPartialTick(true);

            renderUvlaMoons(mv, level, partialTick);
        } finally {
            mv.popMatrix();
        }
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

    private void renderSingleMoon(Matrix4fStack modelView, UvlaMoon moon, float angleDeg, int phaseIndex) {
        modelView.pushMatrix();
        try {
            // “sky quad” convention: rotate into sky space, then pitch by angle
            modelView.rotate(Axis.YP.rotationDegrees(-90.0F));
            modelView.rotate(Axis.XP.rotationDegrees(angleDeg));

            // position and size
            modelView.translate(0.0F, MOON_HEIGHT, 0.0F);
            modelView.scale(MOON_SCALE, MOON_SCALE, MOON_SCALE);

            GpuBufferSlice transforms = RenderSystem.getDynamicUniforms().writeTransform(
                    modelView,
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

                // 1.21.x sky code path uses: (firstIndex, baseVertex, indexCount, instanceCount)
                pass.drawIndexed(0, 0, 6, 1);
            }
        } finally {
            modelView.popMatrix();
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

            Matrix4f m = new Matrix4f(); // identity
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

    private static float getMoonOrbitAngle(ClientLevel level, float partialTick, UvlaMoon moon) {
        float days = (level.getDayTime() + partialTick) / 24000.0F;
        float fraction = (days / moon.orbitalPeriodDays()) % 1.0F;
        return fraction * 360.0F + moon.angleOffsetDeg();
    }

    private static int getMoonPhase(ClientLevel level, float partialTick, UvlaMoon moon) {
        float days = (level.getDayTime() + partialTick) / 24000.0F;
        float cycle = (days / moon.orbitalPeriodDays()) % 1.0F;
        int phase = (int) (cycle * moon.phaseCount());
        return Mth.clamp(phase, 0, moon.phaseCount() - 1);
    }

    /* ==========================================================
       Cleanup
       ========================================================== */

    @Override
    public void close() {
        quadBuffer.close();
    }
}
