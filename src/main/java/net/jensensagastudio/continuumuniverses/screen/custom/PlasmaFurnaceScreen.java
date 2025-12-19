package net.jensensagastudio.continuumuniverses.screen.custom;

import net.jensensagastudio.continuumuniverses.ContinuumUniverses;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;

import java.util.List;

public class PlasmaFurnaceScreen extends AbstractFurnaceScreen<PlasmaFurnaceMenu> {
    private static final Identifier ARROW_TEXTURE =
            Identifier.fromNamespaceAndPath(ContinuumUniverses.MODID, "textures/gui/progress/arrow_progress.png");
    private static final Identifier GUI_TEXTURE =
            Identifier.fromNamespaceAndPath(ContinuumUniverses.MODID, "textures/gui/plasma_furnace.png");
    private static final Identifier FIRE_BURN_SPRITE =
            Identifier.fromNamespaceAndPath(ContinuumUniverses.MODID, "textures/gui/progress/fire_burn.png");

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
                List.of() // no tabs (yet)
        );
    }
    private PlasmaFurnaceMenu plasmaMenu() {
        return (PlasmaFurnaceMenu) this.menu;
    }
}

