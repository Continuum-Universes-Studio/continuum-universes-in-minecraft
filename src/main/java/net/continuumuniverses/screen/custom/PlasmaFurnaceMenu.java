package net.continuumuniverses.screen.custom;

import net.continuumuniverses.block.entity.PlasmaFurnaceBlockEntity;
import net.continuumuniverses.recipes.ModRecipes;
import net.continuumuniverses.screen.ModMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.crafting.RecipePropertySet;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public class PlasmaFurnaceMenu extends AbstractFurnaceMenu {

    public PlasmaFurnaceMenu(
            int containerId,
            Inventory playerInventory,
            @Nullable PlasmaFurnaceBlockEntity blockEntity
    ) {
        super(
                ModMenuTypes.PLASMA_FURNACE_MENU.get(),
                ModRecipes.PLASMA.get(),
                RecipePropertySet.FURNACE_INPUT,   // ✅ REQUIRED
                RecipeBookType.FURNACE,
                containerId,
                playerInventory,
                blockEntity != null ? blockEntity : new SimpleContainer(3),
                blockEntity != null ? blockEntity.getDataAccess() : new SimpleContainerData(4)
        );
    }


    // CLIENT constructor (called when opening screen)
    public PlasmaFurnaceMenu(
            int containerId,
            Inventory playerInventory,
            @Nullable FriendlyByteBuf buf
    ) {
        this(
                containerId,
                playerInventory,
                resolveBlockEntity(playerInventory, buf)
        );
    }

    private static @Nullable PlasmaFurnaceBlockEntity resolveBlockEntity(
            Inventory playerInventory,
            @Nullable FriendlyByteBuf buf
    ) {
        if (buf == null || buf.readableBytes() < Long.BYTES) {
            return null;
        }
        BlockPos pos = buf.readBlockPos();
        BlockEntity blockEntity = playerInventory.player.level().getBlockEntity(pos);
        return blockEntity instanceof PlasmaFurnaceBlockEntity plasma ? plasma : null;
    }

}

