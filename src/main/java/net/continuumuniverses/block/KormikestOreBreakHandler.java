package net.continuumuniverses.block;

import net.continuumuniverses.ContinuumUniverses;
import net.continuumuniverses.fluid.ModFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = ContinuumUniverses.MODID)
public final class KormikestOreBreakHandler {

    private KormikestOreBreakHandler() {}

    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent event) {
        LevelAccessor accessor = event.getLevel();
        if (!(accessor instanceof ServerLevel level)) {
            return; // only do this on the logical server
        }

        BlockPos pos = event.getPos();
        BlockState state = event.getState();
        Player player = event.getPlayer();

        // Only react for your ore
        if (!state.is(ModBlocks.KORMIKEST_ORE.get())) return;

        // Stop normal breaking/drops
        event.setCanceled(true);

        // Optional: don't explode in creative; just remove
        if (player != null && player.isCreative()) {
            level.removeBlock(pos, false);
            return;
        }

        // Small explosion (tweak power to taste)
        level.explode(
                player,
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5,
                1.5f,
                Level.ExplosionInteraction.BLOCK
        );

        // Convert to fluid source
        level.setBlock(
                pos,
                ModFluids.KORMIKEST.source().get().defaultFluidState().createLegacyBlock(),
                3
        );
    }
}
