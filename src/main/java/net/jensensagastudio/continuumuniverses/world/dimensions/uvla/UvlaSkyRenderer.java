package net.jensensagastudio.continuumuniverses.world.dimensions.uvla;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.joml.Vector4f;
import net.jensensagastudio.continuumuniverses.world.dimensions.uvla.KairaLunarEvent;

import java.util.OptionalDouble;
import java.util.OptionalInt;

public class UvlaSkyRenderer extends SkyRenderer {

    /* ==========================================================
       Client lunar state (you chose to store it here)
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

    // One shared unit quad (POSITION_TEX), UV = 0..1
    private final GpuBuffer quadBuffer;

    // Converts QUADS to indices (we use 6 indices per quad draw)
    private final RenderSystem.AutoStorageIndexBuffer quadIndices;

    // Reuse these to avoid allocations every draw
    private static final Vector4f WHITE = new Vector4f(1f, 1f, 1f, 1f);
    private static final Vector3f ORIGIN = new Vector3f(0f, 0f, 0f);
    private static final Matrix4f IDENTITY_UV = new Matrix4f().identity();

    // Visual tuning
    private static final float MOON_SCALE = 30.0F;

    public UvlaSkyRenderer(TextureManager textureManager) {
        super();
        this.textureManager = textureManager;
        this.quadBuffer = buildUnitQuad();
        this.quadIndices = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
    }

    /* ==========================================================
       Entry point
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
       Moon rendering
       ========================================================== */

    private void renderUvlaMoons(PoseStack poseStack, float partialTick, Level level) {
        Matrix4fStack modelView = RenderSystem.getModelViewStack();
        modelView.pushMatrix();
        modelView.mul(poseStack.last().pose());

        for (UvlaMoon moon : UvlaMoons.ALL) {
            float angleDeg = getMoonOrbitAngle(level, partialTick, moon);
            int phaseIndex = getMoonPhase(level, partialTick, moon);
            renderSingleMoon(modelView, moon, angleDeg, phaseIndex);
        }

        modelView.popMatrix();
    }

    private void renderSingleMoon(Matrix4fStack modelView, UvlaMoon moon, float angleDeg, int phaseIndex) {
        modelView.pushMatrix();
        try {
            // Sky-style orientation
            modelView.rotate(Axis.YP.rotationDegrees(-90.0F));
            modelView.rotate(Axis.XP.rotationDegrees(angleDeg));
            modelView.scale(MOON_SCALE, MOON_SCALE, MOON_SCALE);

            GpuBufferSlice transforms =
                    RenderSystem.getDynamicUniforms().writeTransform(
                            modelView,      // Matrix4fc
                            WHITE,          // Vector4fc
                            ORIGIN,         // Vector3fc
                            IDENTITY_UV,    // Matrix4fc (texture matrix)
                            1.0f            // lineWidth (required)
                    );

            var device = RenderSystem.getDevice();
            var target = Minecraft.getInstance().getMainRenderTarget();

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

                // Bind phase texture directly (no atlas)
                var tex = textureManager.getTexture(moon.phaseTexture(phaseIndex));
                pass.bindSampler("Sampler0", tex.getTextureView());

                // Geometry
                pass.setVertexBuffer(0, quadBuffer);
                var indexBuffer = quadIndices.getBuffer(6);
                pass.setIndexBuffer(indexBuffer, quadIndices.type());

                // This is the WebGPU-style signature:
                // drawIndexed(indexCount, instanceCount, firstIndex, baseVertex, firstInstance)
                pass.drawIndexed(6, 1, 0, 0);
            }
        } finally {
            modelView.popMatrix();
        }
    }

    /* ==========================================================
       GPU buffer construction
       ========================================================== */

    private GpuBuffer buildUnitQuad() {
        VertexFormat format = DefaultVertexFormat.POSITION_TEX;
        int bytesNeeded = 4 * format.getVertexSize();

        try (ByteBufferBuilder builder = ByteBufferBuilder.exactlySized(bytesNeeded)) {
            BufferBuilder buffer = new BufferBuilder(builder, VertexFormat.Mode.QUADS, format);

            // full texture UVs
            buffer.addVertex(-1, 0, -1).setUv(0f, 1f);
            buffer.addVertex( 1, 0, -1).setUv(1f, 1f);
            buffer.addVertex( 1, 0,  1).setUv(1f, 0f);
            buffer.addVertex(-1, 0,  1).setUv(0f, 0f);

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

    private float getMoonOrbitAngle(Level level, float partialTick, UvlaMoon moon) {
        float days = (level.getDayTime() + partialTick) / 24000.0F;
        float fraction = (days / moon.orbitalPeriodDays()) % 1.0F;
        return fraction * 360.0F + moon.angleOffsetDeg();
    }

    private int getMoonPhase(Level level, float partialTick, UvlaMoon moon) {
        float days = (level.getDayTime() + partialTick) / 24000.0F;
        float cycle = (days / moon.orbitalPeriodDays()) % 1.0F; // 0..1
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
