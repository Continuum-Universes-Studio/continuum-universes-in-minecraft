package net.jensensagastudio.continuumuniverses.screen.custom;

import net.jensensagastudio.continuumuniverses.block.ModBlocks;
import net.jensensagastudio.continuumuniverses.block.entity.LunarForgeBlockEntity;
import net.jensensagastudio.continuumuniverses.screen.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class LunarForgeMenu extends AbstractContainerMenu {

    public static final int GRID_SIZE = 3;

    private final LunarForgeBlockEntity blockEntity;
    private final ContainerLevelAccess access;
    private final Level level;

    private final CraftingContainer craftingGrid;
    private final ResultContainer result;

    /* ------------------------------------------------------------
     * CLIENT constructor
     * ------------------------------------------------------------ */
    public LunarForgeMenu(
            int containerId,
            Inventory playerInventory,
            FriendlyByteBuf buf
    ) {
        this(
                containerId,
                playerInventory,
                (LunarForgeBlockEntity) playerInventory.player.level()
                        .getBlockEntity(buf.readBlockPos())
        );
    }

    /* ------------------------------------------------------------
     * SERVER constructor (single source of truth)
     * ------------------------------------------------------------ */
    public LunarForgeMenu(
            int containerId,
            Inventory playerInventory,
            LunarForgeBlockEntity blockEntity
    ) {
        super(ModMenuTypes.LUNAR_FORGE_MENU.get(), containerId);

        this.blockEntity = blockEntity;
        this.level = playerInventory.player.level();
        this.access = ContainerLevelAccess.create(level, blockEntity.getBlockPos());

        this.craftingGrid = blockEntity.craftingGrid;
        this.result = blockEntity.result;

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
        return stillValid(access, player, ModBlocks.LUNAR_FORGE.get());
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
}
