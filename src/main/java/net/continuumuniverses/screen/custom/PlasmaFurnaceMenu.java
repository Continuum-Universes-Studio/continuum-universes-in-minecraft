package net.continuumuniverses.screen.custom;

import net.continuumuniverses.block.entity.PlasmaFurnaceBlockEntity;
import net.continuumuniverses.recipes.ModRecipes;
import net.continuumuniverses.screen.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.crafting.RecipePropertySet;

public class PlasmaFurnaceMenu extends AbstractFurnaceMenu {

    public PlasmaFurnaceMenu(
            int containerId,
            Inventory playerInventory,
            PlasmaFurnaceBlockEntity blockEntity
    ) {
        super(
                ModMenuTypes.PLASMA_FURNACE_MENU.get(),
                ModRecipes.PLASMA.get(),
                RecipePropertySet.FURNACE_INPUT,   // ✅ REQUIRED
                RecipeBookType.FURNACE,
                containerId,
                playerInventory,
                blockEntity,                 // treated as Container
                blockEntity.getDataAccess()  // ContainerData
        );
    }


    // CLIENT constructor (called when opening screen)
    public PlasmaFurnaceMenu(
            int containerId,
            Inventory playerInventory,
            FriendlyByteBuf buf
    ) {
        this(
                containerId,
                playerInventory,
                (PlasmaFurnaceBlockEntity) playerInventory.player.level()
                        .getBlockEntity(buf.readBlockPos())
        );
    }

}

