package net.continuumuniverses.block.entity;

import net.continuumuniverses.block.ModBlocks;
import static net.continuumuniverses.ContinuumUniverses.MODID;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PlasmaFurnaceBlockEntity>>
            PLASMA_FURNACE =
            BLOCK_ENTITIES.register(
                    "plasma_furnace_be", () -> new BlockEntityType<>(
                            PlasmaFurnaceBlockEntity::new,
                            false,
                            ModBlocks.PLASMA_FURNACE.get()
                    )
            );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<StellarForgeBlockEntity>>
            STELLAR_FORGE =
            BLOCK_ENTITIES.register(
                    "stellar_forge_be", () -> new BlockEntityType<>(
                            StellarForgeBlockEntity::new,
                            false,
                            ModBlocks.STELLAR_FORGE.get()
                    )
            );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LunarForgeBlockEntity>>
            LUNAR_FORGE =
            BLOCK_ENTITIES.register(
                    "lunar_forge_be", () -> new BlockEntityType<>(
                            LunarForgeBlockEntity::new,
                            false,
                            ModBlocks.LUNAR_FORGE.get()
                    )
            );

}

