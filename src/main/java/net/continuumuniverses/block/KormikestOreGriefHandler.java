package net.continuumuniverses.block;

import java.util.Iterator;

import net.continuumuniverses.ContinuumUniverses;
import net.continuumuniverses.fluid.ModFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.ExplosionEvent;

@EventBusSubscriber(modid = ContinuumUniverses.MODID)
public final class KormikestOreGriefHandler {

    private KormikestOreGriefHandler() {}

    // Prevent recursion: explosions we create will also fire Detonate.
    private static final ThreadLocal<Boolean> IN_HANDLER = ThreadLocal.withInitial(() -> false);

    @SubscribeEvent
    public static void onExplosionDetonate(ExplosionEvent.Detonate event) {
        if (IN_HANDLER.get()) return;              // <-- the important line
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        Iterator<BlockPos> it = event.getAffectedBlocks().iterator();
        while (it.hasNext()) {
            BlockPos pos = it.next();
            BlockState state = level.getBlockState(pos);

            if (!state.is(ModBlocks.KORMIKEST_ORE.get())) continue;

            // Remove it from vanilla explosion processing so we control the result
            it.remove();

            // Convert first (so the reaction leaves fluid even if the secondary boom would remove it)
            level.setBlock(
                    pos,
                    ModFluids.KORMIKEST.source().get().defaultFluidState().createLegacyBlock(),
                    3
            );

            // Trigger a small "volatile reaction" explosion without terrain grief
            IN_HANDLER.set(true);
            try {
                level.explode(
                        null,
                        pos.getX() + 0.5,
                        pos.getY() + 0.5,
                        pos.getZ() + 0.5,
                        1.2f, // tweak
                        Level.ExplosionInteraction.NONE // no block damage; still boom + entity damage
                );
            } finally {
                IN_HANDLER.set(false);
            }
        }
    }
}
