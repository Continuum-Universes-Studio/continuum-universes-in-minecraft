package net.continuumuniverses.inventory;

import net.continuumuniverses.ContinuumUniverses;
import net.continuumuniverses.block.ModBlocks;
import net.continuumuniverses.fluid.ModFluids;
import net.continuumuniverses.item.ModItems;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = ContinuumUniverses.MODID)
public class ModCreativeTabs {

    /* ------------------------------------------------------------
     * Custom Continuum Universes tab
     * ------------------------------------------------------------ */

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ContinuumUniverses.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CONTINUUM =
            TABS.register(ContinuumUniverses.MODID, () ->
                    CreativeModeTab.builder()
                            .title(Component.translatable("creativetab.continuumuniverses"))
                            .icon(() -> new ItemStack(ModItems.KORMIKEST_DUST.get()))
                            .displayItems((params, output) -> {

                                /* ---- Blocks ---- */
                                output.accept(ModBlocks.PLASMA_FURNACE.get());
                                output.accept(ModBlocks.AETHYRIL_ORE.get());

                                /* ---- Materials ---- */
                                output.accept(ModItems.VARKEST_INGOT.get());
                                output.accept(ModItems.LARZIKEST_INGOT.get());
                                output.accept(ModItems.KORMIKEST_DUST.get());
                                output.accept(ModItems.LETHURKEST_GEM.get());

                                /* ---- Tools ---- */
                                output.accept(ModItems.VARKEST_PICKAXE.get());
                                output.accept(ModItems.VARKEST_AXE.get());
                                output.accept(ModItems.VARKEST_SHOVEL.get());
                                output.accept(ModItems.VARKEST_HOE.get());
                                output.accept(ModItems.VARKEST_SWORD.get());

                                output.accept(ModItems.LARZIKEST_PICKAXE.get());
                                output.accept(ModItems.LARZIKEST_AXE.get());
                                output.accept(ModItems.LARZIKEST_SHOVEL.get());
                                output.accept(ModItems.LARZIKEST_HOE.get());
                                output.accept(ModItems.LARZIKEST_SWORD.get());

                                /* ---- Misc ---- */
                                output.accept(ModFluids.KORMIKEST.bucket().get());
                                output.accept(ModItems.UVLA_ITEM.get());
                            })
                            .build()
            );

    /* ------------------------------------------------------------
     * Vanilla tab injections (replaces MCreator tab file)
     * ------------------------------------------------------------ */

    @SubscribeEvent
    public static void addToVanillaTabs(BuildCreativeModeTabContentsEvent event) {

        /* INGREDIENTS */
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.VARKEST_INGOT.get());
            event.accept(ModItems.LARZIKEST_INGOT.get());
            event.accept(ModItems.KORMIKEST_DUST.get());
            event.accept(ModItems.LETHURKEST_GEM.get());
            event.accept(ModItems.RAW_LARZIKEST.get());
            event.accept(ModItems.RAW_VARKEST.get());
            event.accept(ModItems.RAW_STARSTEEL.get());
        }

        /* BUILDING BLOCKS */
        else if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(ModBlocks.VARKEST_BLOCK.get());
            event.accept(ModBlocks.LARZIKEST_ORE.get());
            event.accept(ModBlocks.LARZIKEST_BLOCK.get());
            event.accept(ModBlocks.KORMIKEST_ORE.get());
            event.accept(ModBlocks.LETHURKEST_ORE.get());
            event.accept(ModBlocks.LETHURKEST_BLOCK.get());

            event.accept(ModBlocks.UVLA_LOG.get());
            event.accept(ModBlocks.UVLA_WOOD.get());
            event.accept(ModBlocks.STRIPPED_UVLA_LOG.get());
            event.accept(ModBlocks.STRIPPED_UVLA_WOOD.get());
            event.accept(ModBlocks.UVLA_PLANKS.get());
            event.accept(ModBlocks.UVLA_STAIRS.get());
            event.accept(ModBlocks.UVLA_SLAB.get());
            event.accept(ModBlocks.UVLA_FENCE.get());
            event.accept(ModBlocks.UVLA_FENCE_GATE.get());
            event.accept(ModBlocks.UVLA_DOOR.get());
            event.accept(ModBlocks.UVLA_TRAPDOOR.get());
            event.accept(ModBlocks.UVLA_PRESSURE_PLATE.get());
            event.accept(ModBlocks.UVLA_BUTTON.get());
        }

        /* NATURAL BLOCKS */
        else if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(ModBlocks.UVLA_LEAVES.get());
        }

        /* TOOLS */
        else if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.VARKEST_PICKAXE.get());
            event.accept(ModItems.VARKEST_AXE.get());
            event.accept(ModItems.VARKEST_SHOVEL.get());
            event.accept(ModItems.VARKEST_HOE.get());

            event.accept(ModItems.LARZIKEST_PICKAXE.get());
            event.accept(ModItems.LARZIKEST_AXE.get());
            event.accept(ModItems.LARZIKEST_SHOVEL.get());
            event.accept(ModItems.LARZIKEST_HOE.get());

            event.accept(ModItems.UVLA_ITEM.get());
        }

        /* COMBAT */
        else if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ModItems.VARKEST_SWORD.get());
            event.accept(ModItems.LARZIKEST_SWORD.get());
        }

        /* FOOD & DRINKS */
        else if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(ModFluids.KORMIKEST.bucket().get());
        }
    }
}
