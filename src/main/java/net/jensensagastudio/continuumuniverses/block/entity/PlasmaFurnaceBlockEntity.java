package net.jensensagastudio.continuumuniverses.block.entity;

import net.jensensagastudio.continuumuniverses.item.ModItems;
import net.jensensagastudio.continuumuniverses.recipes.ModRecipes;
import net.jensensagastudio.continuumuniverses.recipes.PlasmaRecipeInput;
import net.jensensagastudio.continuumuniverses.screen.custom.PlasmaFurnaceMenu;
import net.jensensagastudio.continuumuniverses.tags.ModTags;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;
import java.util.Optional;
import net.minecraft.nbt.CompoundTag;

public class PlasmaFurnaceBlockEntity extends AbstractFurnaceBlockEntity {

    public PlasmaFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PLASMA_FURNACE.get(), pos, state, ModRecipes.PLASMA.get());
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.continuumuniverses.plasma_furnace");
    }
    public ContainerData getDataAccess() {
        return this.dataAccess;
    }
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player) {
        return new PlasmaFurnaceMenu(id, playerInv, this);
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new PlasmaFurnaceMenu(containerId, inventory, this);
    }


    protected int getBurnDuration(ItemStack fuel) {
        if (fuel.is(ModItems.KORMIKEST_GELATIN.get())) return 30_000;
        if (fuel.is(ModItems.KORMIKEST_DUST.get()))   return 10_000;
        return 0;
    }


}

