package net.continuumuniverses.client;

import net.continuumuniverses.ContinuumUniverses;
import net.continuumuniverses.renderer.EmissiveBakedModel;
import net.continuumuniverses.renderer.EmissiveItemModel;
import net.continuumuniverses.renderer.ModModelLoaders;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.world.level.block.state.BlockState;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Client entry point for the Continuum Universes mod.
 */
@Mod(value = ContinuumUniverses.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = ContinuumUniverses.MODID, value = Dist.CLIENT)
public class ContinuumUniversesClient {
    public ContinuumUniversesClient(ModContainer container) {
        // Register a config screen
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Basic client setup logging
        ContinuumUniverses.LOGGER.info("HELLO FROM CLIENT SETUP");
        ContinuumUniverses.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
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

    @SubscribeEvent
    public static void registerModelLoaders(ModelEvent.RegisterLoaders event) {
        ModModelLoaders.register(event);
    }

    @SubscribeEvent
    public static void onModifyBakingResult(ModelEvent.ModifyBakingResult event) {
        Map<BlockState, BlockStateModel> models =
                event.getBakingResult().blockStateModels();

        IdentityHashMap<BlockStateModel, BlockStateModel> wrapped = new IdentityHashMap<>();

        for (Map.Entry<BlockState, BlockStateModel> entry : models.entrySet()) {
            BlockStateModel original = entry.getValue();

            if (!EmissiveBakedModel.hasEmissiveQuads(original)) {
                continue;
            }

            BlockStateModel replacement = wrapped.computeIfAbsent(
                    original,
                    EmissiveBakedModel::new
            );

            entry.setValue(replacement);
        }

        // Items are fine as-is
        Map<ResourceLocation, ItemModel> itemModels =
                event.getBakingResult().itemStackModels();

        for (Map.Entry<ResourceLocation, ItemModel> entry : itemModels.entrySet()) {
            entry.setValue(EmissiveItemModel.wrapIfEmissive(entry.getValue()));
        }
    }


}
