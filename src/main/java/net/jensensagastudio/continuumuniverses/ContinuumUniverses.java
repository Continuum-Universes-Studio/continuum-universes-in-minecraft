package net.jensensagastudio.continuumuniverses;

import net.jensensagastudio.continuumuniverses.block.ModBlocks;
import net.jensensagastudio.continuumuniverses.block.entity.ModBlockEntities;
import net.jensensagastudio.continuumuniverses.fluid.ModFluids;
import net.jensensagastudio.continuumuniverses.inventory.ModCreativeTabs;
import net.jensensagastudio.continuumuniverses.item.ModItems;
import net.jensensagastudio.continuumuniverses.recipes.ModRecipes;
import net.jensensagastudio.continuumuniverses.screen.ModMenuTypes;
import net.jensensagastudio.continuumuniverses.world.dimension.ModDimensionTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod("continuumuniverses")
public class ContinuumUniverses {
	public static final Logger LOGGER = LogManager.getLogger(ContinuumUniverses.class);
	public static final String MODID = "continuumuniverses";

	public ContinuumUniverses(IEventBus modEventBus) {

		// REMOVE ONE OF THESE — keep only one
		NeoForge.EVENT_BUS.register(this);

		ModRecipes.SERIALIZERS.register(modEventBus);
		ModBlocks.BLOCKS.register(modEventBus);
		ModItems.ITEMS.register(modEventBus);
		ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);

		ModFluids.HELPER.FLUIDS.register(modEventBus);
		ModFluids.HELPER.FLUID_TYPES.register(modEventBus);
		ModFluids.HELPER.BLOCKS.register(modEventBus);
		ModFluids.HELPER.ITEMS.register(modEventBus);

		ModMenuTypes.MENU_TYPES.register(modEventBus);
		//ModDimensionTypes.DIMENSION_TYPE.register(modEventBus);
		ModCreativeTabs.TABS.register(modEventBus);

		modEventBus.addListener(this::commonSetup);
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		// Some common setup code
		LOGGER.info("HELLO FROM COMMON SETUP");


	}
	// You can use SubscribeEvent and let the Event Bus discover methods to call
	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event) {
		// Do something when the server starts
		LOGGER.info("HELLO from server starting");
	}
}