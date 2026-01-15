package net.continuumuniverses;

import net.minecraft.client.Minecraft;
import net.continuumuniverses.screen.ModMenuTypes;
import net.continuumuniverses.screen.custom.LunarForgeScreen;
import net.continuumuniverses.screen.custom.PlasmaFurnaceScreen;
import net.continuumuniverses.screen.custom.StellarForgeScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = ContinuumUniverses.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = ContinuumUniverses.MODID, value = Dist.CLIENT)
public class ContinuumUniversesClient {
    public ContinuumUniversesClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        ContinuumUniverses.LOGGER.info("HELLO FROM CLIENT SETUP");
        ContinuumUniverses.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }

    @SubscribeEvent
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.PLASMA_FURNACE_MENU.get(), PlasmaFurnaceScreen::new);
        event.register(ModMenuTypes.STELLAR_FORGE_MENU.get(), StellarForgeScreen::new);
        event.register(ModMenuTypes.LUNAR_FORGE_MENU.get(), LunarForgeScreen::new);
    }




    /**
     * Register custom dimension effects for the uvla dimension.
     *
     * @param event the event used to register dimension special effects
     */
    @SubscribeEvent
    public static void registerUvlaDimensionEffects(RegisterDimensionSpecialEffectsEvent event) {
        event.register(
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(ContinuumUniverses.MODID, "uvla"),
                new net.continuumuniverses.world.dimensions.uvla.UvlaDimensionEffects()
        );
    }
}
