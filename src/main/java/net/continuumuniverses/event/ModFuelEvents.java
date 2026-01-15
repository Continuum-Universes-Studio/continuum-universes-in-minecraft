package net.continuumuniverses.event;

import net.continuumuniverses.ContinuumUniverses;
import net.continuumuniverses.item.ModItems;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;

@EventBusSubscriber(modid = ContinuumUniverses.MODID)
public final class ModFuelEvents {
    private static final int KORMIKEST_GELATIN_BURN_TIME = 200;
    private static final int KORMIKEST_DUST_BURN_TIME = 100;

    private ModFuelEvents() {}

    @SubscribeEvent
    public static void onFuelBurnTime(FurnaceFuelBurnTimeEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.is(ModItems.KORMIKEST_GELATIN.get())) {
            event.setBurnTime(KORMIKEST_GELATIN_BURN_TIME);
        } else if (stack.is(ModItems.KORMIKEST_DUST.get())) {
            event.setBurnTime(KORMIKEST_DUST_BURN_TIME);
        }
    }
}
