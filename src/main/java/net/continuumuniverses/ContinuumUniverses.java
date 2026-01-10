package net.continuumuniverses;

import net.continuumuniverses.block.ModBlocks;
import net.continuumuniverses.block.entity.ModBlockEntities;
import net.continuumuniverses.fluid.ModFluids;
import net.continuumuniverses.inventory.ModCreativeTabs;
import net.continuumuniverses.item.ModItems;
import net.continuumuniverses.recipes.ModRecipes;
import net.continuumuniverses.screen.ModMenuTypes;
import net.continuumuniverses.world.gen.ModFoliagePlacers;
import net.continuumuniverses.world.poi.ModPOIs;
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
		ModRecipes.TYPES.register(modEventBus);
		ModBlocks.BLOCKS.register(modEventBus);
		ModItems.ITEMS.register(modEventBus);
		ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);

		ModFluids.HELPER.FLUIDS.register(modEventBus);
		ModFluids.HELPER.FLUID_TYPES.register(modEventBus);
		ModFluids.HELPER.BLOCKS.register(modEventBus);
		ModFluids.HELPER.ITEMS.register(modEventBus);

		ModMenuTypes.MENU_TYPES.register(modEventBus);
		ModPOIs.POIS.register(modEventBus);
		//ModDimensionTypes.DIMENSION_TYPE.register(modEventBus);
		ModCreativeTabs.TABS.register(modEventBus);
		ModFoliagePlacers.FOLIAGE_PLACERS.register(modEventBus);

		modEventBus.addListener(this::commonSetup);
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		// Some common setup code
		LOGGER.info("HELLO FROM COMMON SETUP");
		event.enqueueWork(ModPOIs::registerPortalPoiStates);


	}
	// You can use SubscribeEvent and let the Event Bus discover methods to call
	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event) {
		// Do something when the server starts
		LOGGER.info("HELLO from server starting");
	}
}
