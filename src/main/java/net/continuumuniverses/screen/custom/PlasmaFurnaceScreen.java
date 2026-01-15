package net.continuumuniverses.screen.custom;

import net.continuumuniverses.ContinuumUniverses;
import net.continuumuniverses.block.ModBlocks;
import net.continuumuniverses.recipes.ModRecipeBookCategory;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.SearchRecipeBookCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

public class PlasmaFurnaceScreen extends AbstractFurnaceScreen<PlasmaFurnaceMenu> {
    private static final ResourceLocation ARROW_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ContinuumUniverses.MODID, "textures/gui/progress/arrow_progress.png");
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ContinuumUniverses.MODID, "textures/gui/plasma_furnace.png");
    private static final ResourceLocation FIRE_BURN_SPRITE =
            ResourceLocation.fromNamespaceAndPath(ContinuumUniverses.MODID, "textures/gui/progress/fire_burn.png");

    public PlasmaFurnaceScreen(
            PlasmaFurnaceMenu menu,
            Inventory playerInventory,
            Component title
    ) {
        super(
                menu,
                playerInventory,
                title,
                Component.translatable("container.inventory"),
                GUI_TEXTURE,
                FIRE_BURN_SPRITE,
                ARROW_TEXTURE,
                List.of(
                        new RecipeBookComponent.TabInfo(SearchRecipeBookCategory.FURNACE),
                        new RecipeBookComponent.TabInfo(
                                ModBlocks.PLASMA_FURNACE.get().asItem(),
                                ModRecipeBookCategory.PLASMA_SMELTING
                        )
                )
        );
    }
    private PlasmaFurnaceMenu plasmaMenu() {
        return (PlasmaFurnaceMenu) this.menu;
    }
}

