package net.continuumuniverses.mixin;

import net.continuumuniverses.block.entity.PlasmaFurnaceBlockEntity;
import net.continuumuniverses.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceBlockEntityMixin {
    private static final float KORMIKEST_EXPLOSION_POWER = 8.0F;
    private static final int FUEL_SLOT = 1;

    @Inject(method = "serverTick", at = @At("HEAD"))
    private static void continuumuniverses$explodeKormikestFuel(
            Level level,
            BlockPos pos,
            BlockState state,
            AbstractFurnaceBlockEntity blockEntity,
            CallbackInfo ci
    ) {
        if (level.isClientSide()) {
            return;
        }
        if (blockEntity instanceof PlasmaFurnaceBlockEntity) {
            return;
        }
        if (!(blockEntity instanceof FurnaceBlockEntity || blockEntity instanceof BlastFurnaceBlockEntity)) {
            return;
        }
        ItemStack fuelStack = blockEntity.getItem(FUEL_SLOT);
        if (fuelStack.isEmpty()) {
            return;
        }
        if (!fuelStack.is(ModItems.KORMIKEST_GELATIN.get())
                && !fuelStack.is(ModItems.KORMIKEST_DUST.get())) {
            return;
        }
        blockEntity.clearContent();
        level.removeBlock(pos, false);
        level.explode(
                null,
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5,
                KORMIKEST_EXPLOSION_POWER,
                Level.ExplosionInteraction.BLOCK
        );
    }
}
