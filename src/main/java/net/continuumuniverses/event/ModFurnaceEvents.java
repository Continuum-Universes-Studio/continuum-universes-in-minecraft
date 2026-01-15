package net.continuumuniverses.event;

import net.continuumuniverses.ContinuumUniverses;
import net.continuumuniverses.block.entity.PlasmaFurnaceBlockEntity;
import net.continuumuniverses.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;

@EventBusSubscriber(modid = ContinuumUniverses.MODID)
public final class ModFurnaceEvents {
    private static final float KORMIKEST_EXPLOSION_POWER = 8.0F;

    private ModFurnaceEvents() {
    }

    @SubscribeEvent
    public static void onFurnaceFuelBurnTime(FurnaceFuelBurnTimeEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack == null || stack.isEmpty()) {
            return;
        }
        if (!isKormikest(stack)) {
            return;
        }
        AbstractFurnaceBlockEntity furnace = event.getBlockEntity();
        if (furnace == null || furnace instanceof PlasmaFurnaceBlockEntity) {
            return;
        }
        if (!(furnace instanceof FurnaceBlockEntity || furnace instanceof BlastFurnaceBlockEntity)) {
            return;
        }
        Level level = furnace.getLevel();
        if (level == null || level.isClientSide()) {
            return;
        }
        BlockPos pos = furnace.getBlockPos();
        furnace.clearContent();
        level.removeBlock(pos, false);
        level.explode(
                null,
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5,
                KORMIKEST_EXPLOSION_POWER,
                Level.ExplosionInteraction.BLOCK
        );
        event.setBurnTime(0);
    }

    private static boolean isKormikest(ItemStack stack) {
        return stack.is(ModItems.KORMIKEST_GELATIN.get()) || stack.is(ModItems.KORMIKEST_DUST.get());
    }
}
