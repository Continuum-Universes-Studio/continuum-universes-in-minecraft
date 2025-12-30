package net.jensensagastudio.continuumuniverses.screen.custom;

import net.jensensagastudio.continuumuniverses.ContinuumUniverses;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class StellarForgeScreen extends AbstractContainerScreen<StellarForgeMenu> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    ContinuumUniverses.MODID,
                    "textures/gui/stellar_forge.png"
            );

    public StellarForgeScreen(StellarForgeMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics gfx, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        gfx.blit(
                RenderPipelines.GUI_TEXTURED,
                TEXTURE,
                x, y,
                0, 0,
                imageWidth, imageHeight,
                imageWidth, imageHeight
        );
    }
}
