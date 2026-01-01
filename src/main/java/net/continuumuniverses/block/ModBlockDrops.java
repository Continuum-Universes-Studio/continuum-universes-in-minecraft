package net.continuumuniverses.block;

import net.continuumuniverses.ContinuumUniverses;
import net.continuumuniverses.tags.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.List;

@EventBusSubscriber(modid = ContinuumUniverses.MODID, bus = EventBusSubscriber.Bus.NEOFORGE)
public final class ModBlockDrops {

    private ModBlockDrops() {}

    @SubscribeEvent
    public static void ensureSelfDrop(BlockEvent.BreakEvent event) {
        Level level = event.getLevel();
        if (level.isClientSide()) {
            return;
        }

        BlockState state = event.getState();
        if (!state.is(ModTags.Blocks.SELF_DROPS)) {
            return;
        }

        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        Player player = event.getPlayer();
        if (state.requiresCorrectToolForDrops() && !player.hasCorrectToolForDrops(state)) {
            return;
        }

        BlockPos pos = event.getPos();
        BlockEntity blockEntity = serverLevel.getBlockEntity(pos);
        List<ItemStack> drops = Block.getDrops(state, serverLevel, pos, blockEntity, player, player.getMainHandItem());
        if (!drops.isEmpty()) {
            return;
        }

        ItemStack drop = new ItemStack(state.getBlock().asItem());
        if (drop.isEmpty()) {
            return;
        }

        Block.popResource(serverLevel, pos, drop);
    }
}
