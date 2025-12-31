package net.continuumuniverses.block.entity;

import net.continuumuniverses.screen.custom.LunarForgeMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.inventory.TransientCraftingContainer;

public class LunarForgeBlockEntity extends BlockEntity implements MenuProvider {

    public static final int GRID_SIZE = 3;

    public final TransientCraftingContainer craftingGrid =
            new TransientCraftingContainer(null, GRID_SIZE, GRID_SIZE);

    public final ResultContainer result = new ResultContainer();

    public LunarForgeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LUNAR_FORGE.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.continuumuniverses.lunar_forge");
    }

    @Override
    public AbstractContainerMenu createMenu(
            int id,
            Inventory playerInventory,
            Player player
    ) {
        return new LunarForgeMenu(id, playerInventory, this);
    }
}


