package net.continuumuniverses.block;

import net.continuumuniverses.ContinuumUniverses;
import net.continuumuniverses.item.ModItems;
import net.continuumuniverses.world.gen.UvlaTreeGrowers;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.util.valueproviders.UniformInt;

import java.util.function.Function;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(ContinuumUniverses.MODID);

    public static final DeferredHolder<Block, PlasmaFurnaceBlock> PLASMA_FURNACE =
            registerBlock(
                    "plasma_furnace",
                    PlasmaFurnaceBlock::new
            );
    public static final DeferredHolder<Block, StellarForgeBlock> STELLAR_FORGE =
            registerBlock(
                    "stellar_forge",
                    StellarForgeBlock::new
            );
    public static final DeferredHolder<Block, LunarForgeBlock> LUNAR_FORGE =
            registerBlock(
                    "lunar_forge",
                    LunarForgeBlock::new
            );
    public static final DeferredBlock<DropExperienceBlock> AETHYRIL_ORE =
            registerOreBlock("aethyril_ore", 3.0f, UniformInt.of(3, 7));


    /* =========================
       === ORES & BLOCKS ===
       ========================= */

    public static final DeferredBlock<DropExperienceBlock> VARKEST_ORE =
            registerOreBlock("varkest_ore_block",20.0f, UniformInt.of(5, 9));


    public static final DeferredBlock<DropExperienceBlock>  LARZIKEST_ORE =
           registerOreBlock(
                     "larzikest_ore_block",
                     4.0f,
                     UniformInt.of(3, 7)
           );
    public static final DeferredBlock<DropExperienceBlock>  KORMIKEST_ORE =
            registerOreBlock(
                    "kormikest_ore_block",
                    3.5f,
                    UniformInt.of(4, 8)
            );
    public static final DeferredBlock<DropExperienceBlock> LETHURKEST_ORE =
            registerOreBlock(
                    "lethurkest_ore_block",
                    40.0f,
                    UniformInt.of(20, 27)
            );
    public static final DeferredBlock<DropExperienceBlock>  STARSTEEL_ORE =
            registerOreBlock(
                    "starsteel_ore_block",
                    10.0f,
                    UniformInt.of(4, 7)
            );

    public static final DeferredHolder<Block, LarzikestBlockBlock> LARZIKEST_BLOCK =
            registerBlock(
                    "larzikest_block",
                    LarzikestBlockBlock::new
              );
    public static final DeferredHolder<Block, LethurkestBlockBlock> LETHURKEST_BLOCK =
            registerBlock(
                    "lethurkest_block",
                    LethurkestBlockBlock::new
            );
    public static final DeferredHolder<Block, StarSteelBlockBlock> STARSTEEL_BLOCK =
           registerBlock(
                    "starsteel_block",
                    StarSteelBlockBlock::new
           );
    public static final DeferredHolder<Block, VarkestBlockBlock> VARKEST_BLOCK =
            registerBlock("varkest_block", VarkestBlockBlock::new);

/* =========================
       === UVLA WOOD SET ===
       ========================= */

    public static final DeferredHolder<Block, UvlaLogBlock> UVLA_LOG =
            registerBlock(
                    "uvla_log",
                    UvlaLogBlock::new
            );

    public static final DeferredHolder<Block, UvlaWoodBlock> UVLA_WOOD =
            registerBlock(
                    "uvla_wood",
                    UvlaWoodBlock::new
            );

    public static final DeferredHolder<Block, StrippedUvlaLogBlock> STRIPPED_UVLA_LOG =
            registerBlock(
                    "stripped_uvla_log",
                    StrippedUvlaLogBlock::new
            );

    public static final DeferredHolder<Block, StrippedUvlaWoodBlock> STRIPPED_UVLA_WOOD =
            registerBlock(
                    "stripped_uvla_wood",
                    StrippedUvlaWoodBlock::new
            );

    public static final DeferredHolder<Block, UvlaPlanksBlock> UVLA_PLANKS =
            registerBlock(
                    "uvla_planks",
                    UvlaPlanksBlock::new
            );

    public static final DeferredHolder<Block, UvlaLeavesBlock> UVLA_LEAVES =
            registerBlock(
                    "uvla_leaves",
                    UvlaLeavesBlock::new
            );
    public static final DeferredHolder<Block, SaplingBlock> UVLA_WILLOW_SAPLING =
            registerBlock(
                    "uvla_willow_sapling",
                    props -> new SaplingBlock(UvlaTreeGrowers.UVLA_WILLOW, props),
                    BlockBehaviour.Properties.of()
                            .noCollission()
                            .randomTicks()
                            .instabreak()
                            .sound(SoundType.GRASS)
            );
    public static final DeferredHolder<Block, SaplingBlock> UVLA_MANGROVE_SAPLING =
            registerBlock(
                    "uvla_mangrove_sapling",
                    props -> new SaplingBlock(UvlaTreeGrowers.UVLA_MANGROVE, props),
                    BlockBehaviour.Properties.of()
                            .noCollission()
                            .randomTicks()
                            .instabreak()
                            .sound(SoundType.GRASS)
            );
    public static final DeferredHolder<Block, SaplingBlock> UVLA_GIANT_SAPLING =
            registerBlock(
                    "uvla_giant_sapling",
                    props -> new SaplingBlock(UvlaTreeGrowers.UVLA_GIANT, props),
                    BlockBehaviour.Properties.of()
                            .noCollission()
                            .randomTicks()
                            .instabreak()
                            .sound(SoundType.GRASS)
            );
    public static final DeferredBlock<StairBlock> UVLA_STAIRS = registerBlock("bismuth_stairs",
            (properties) -> new StairBlock(ModBlocks.UVLA_PLANKS.get().defaultBlockState(),
                    properties.strength(2f).requiresCorrectToolForDrops()));



    public static final DeferredHolder<Block, UvlaSlabBlock> UVLA_SLAB =
            registerBlock(
                    "uvla_slab",
                    UvlaSlabBlock::new
            );

    public static final DeferredHolder<Block, UvlaFenceBlock> UVLA_FENCE =
            registerBlock(
                    "uvla_fence",
                    UvlaFenceBlock::new
            );

    public static final DeferredHolder<Block, UvlaFenceGateBlock> UVLA_FENCE_GATE =
            registerBlock(
                    "uvla_fence_gate",
                    UvlaFenceGateBlock::new
            );

    public static final DeferredHolder<Block, UvlaDoorBlock> UVLA_DOOR =
            registerBlock(
                    "uvla_door",
                    UvlaDoorBlock::new
            );

    public static final DeferredHolder<Block, UvlaTrapdoorBlock> UVLA_TRAPDOOR =
            registerBlock(
                    "uvla_trapdoor",
                    UvlaTrapdoorBlock::new
            );

    public static final DeferredHolder<Block, UvlaPressurePlateBlock> UVLA_PRESSURE_PLATE =
            registerBlock(
                    "uvla_pressure_plate",
                    UvlaPressurePlateBlock::new
            );

    public static final DeferredHolder<Block, UvlaButtonBlock> UVLA_BUTTON =
            registerBlock(
                    "uvla_button",
                    UvlaButtonBlock::new
            );
    /* =========================
       === FLUID / PORTAL ===
       ========================= */

    public static final DeferredHolder<Block, Block> UVLA_PORTAL =
            registerBlock(
                    "uvla_portal",
                    UvlaPortalBlock::new
            );

    private static <T extends Block> DeferredBlock<T> registerBlock(
            String name,
            Function<BlockBehaviour.Properties, T> factory
    ) {
        DeferredBlock<T> toReturn = BLOCKS.registerBlock(name, factory); // <-- THIS
        registerBlockItem(name, toReturn);
        return toReturn;
    }
    private static <T extends Block> DeferredBlock<T> registerBlock(
            String name,
            Function<BlockBehaviour.Properties, T> factory,
            BlockBehaviour.Properties properties
    ) {
        DeferredBlock<T> toReturn = BLOCKS.register(
                name,
                () -> factory.apply(properties)
        );
        registerBlockItem(name, toReturn);
        return toReturn;
    }


    private static DeferredBlock<DropExperienceBlock> registerOreBlock(
            String name,
            float hardness,
            UniformInt xp
    ) {
        DeferredBlock<DropExperienceBlock> block = BLOCKS.registerBlock(
                name,
                props -> new DropExperienceBlock(
                        xp,
                        props
                                .strength(hardness)
                                .requiresCorrectToolForDrops()
                )
        );

        registerBlockItem(name, block);
        return block;
    }


    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.registerItem(name, (properties) -> new BlockItem(block.get(), properties.useBlockDescriptionPrefix()));
    }
}
