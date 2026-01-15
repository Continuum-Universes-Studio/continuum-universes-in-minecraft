package net.continuumuniverses.screen.custom;

import net.continuumuniverses.block.ModBlocks;
import net.continuumuniverses.block.entity.StellarForgeBlockEntity;
import net.continuumuniverses.screen.ModMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public class StellarForgeMenu extends AbstractContainerMenu {

    public static final int GRID_SIZE = 3;

    private final StellarForgeBlockEntity blockEntity;
    private final ContainerLevelAccess access;
    private final Level level;

    private final CraftingContainer craftingGrid;
    private final ResultContainer result;

    /* ------------------------------------------------------------
     * CLIENT constructor
     * ------------------------------------------------------------ */
    public StellarForgeMenu(
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

    /* ------------------------------------------------------------
     * SERVER constructor (single source of truth)
     * ------------------------------------------------------------ */
    public StellarForgeMenu(
            int containerId,
            Inventory playerInventory,
            @Nullable StellarForgeBlockEntity blockEntity
    ) {
        super(ModMenuTypes.STELLAR_FORGE_MENU.get(), containerId);

        this.blockEntity = blockEntity != null ? blockEntity : new StellarForgeBlockEntity(BlockPos.ZERO, ModBlocks.STELLAR_FORGE.get().defaultBlockState());
        this.level = playerInventory.player.level();
        this.access = ContainerLevelAccess.create(level, this.blockEntity.getBlockPos());

        this.craftingGrid = this.blockEntity.craftingGrid;
        this.result = this.blockEntity.result;

        // Result slot
        this.addSlot(new ResultSlot(
                playerInventory.player,
                craftingGrid,
                result,
                0,
                124,
                35
        ));

        // Crafting grid (3x3)
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                this.addSlot(new Slot(
                        craftingGrid,
                        col + row * GRID_SIZE,
                        30 + col * 18,
                        17 + row * 18
                ));
            }
        }

        // Player inventory
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    /* ------------------------------------------------------------
     * Shift-click logic
     * ------------------------------------------------------------ */
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack retStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            retStack = stack.copy();

            // Result slot
            if (index == 0) {
                if (!this.moveItemStackTo(stack, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack, retStack);
            }
            // Player inventory → crafting grid
            else if (index >= 10 && index < 46) {
                if (!this.moveItemStackTo(stack, 1, 10, false)) {
                    return ItemStack.EMPTY;
                }
            }
            // Crafting grid → player inventory
            else if (!this.moveItemStackTo(stack, 10, 46, false)) {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == retStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
        }

        return retStack;
    }

    /* ------------------------------------------------------------
     * Validity
     * ------------------------------------------------------------ */
    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, ModBlocks.STELLAR_FORGE.get());
    }

    /* ------------------------------------------------------------
     * Player inventory slots
     * ------------------------------------------------------------ */
    private void addPlayerInventory(Inventory inv) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(
                        inv,
                        col + row * 9 + 9,
                        8 + col * 18,
                        84 + row * 18
                ));
            }
        }
    }

    private void addPlayerHotbar(Inventory inv) {
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(
                    inv,
                    col,
                    8 + col * 18,
                    142
            ));
        }
    }

    private static @Nullable StellarForgeBlockEntity resolveBlockEntity(
            Inventory playerInventory,
            @Nullable FriendlyByteBuf buf
    ) {
        if (buf == null || buf.readableBytes() < Long.BYTES) {
            return null;
        }
        BlockPos pos = buf.readBlockPos();
        BlockEntity blockEntity = playerInventory.player.level().getBlockEntity(pos);
        return blockEntity instanceof StellarForgeBlockEntity forge ? forge : null;
    }
}
