package net.continuumuniverses.block;

import java.util.function.Function;
import java.util.function.Supplier;

import net.continuumuniverses.ContinuumUniverses;
import net.continuumuniverses.item.ModItems;
import net.continuumuniverses.world.gen.UvlaTreeGrowers;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.resources.ResourceLocation;
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
            registerOreBlock("varkest_ore", 20.0f, UniformInt.of(5, 9));

    public static final DeferredBlock<DropExperienceBlock> LARZIKEST_ORE =
            registerOreBlock(
                    "larzikest_ore",
                    4.0f,
                    UniformInt.of(3, 7)
            );
    public static final DeferredBlock<DropExperienceBlock> KORMIKEST_ORE =
            registerOreBlock(
                    "kormikest_ore",
                    3.5f,
                    UniformInt.of(4, 8),
                    12
            );
    public static final DeferredBlock<DropExperienceBlock> LETHURKEST_ORE =
            registerOreBlock(
                    "lethurkest_ore",
                    40.0f,
                    UniformInt.of(20, 27)
            );
    public static final DeferredBlock<DropExperienceBlock> STARSTEEL_ORE =
            registerOreBlock(
                    "starsteel_ore",
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
    public static final DeferredHolder<Block, AethyrilBlockBlock> AETHYRIL_BLOCK =
            registerBlock("aethyril_block", AethyrilBlockBlock::new);
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

    /* =========================
       === UMDRA WOOD SET ===
       ========================= */

    public static final DeferredHolder<Block, UmdraLogBlock> UMDRA_LOG =
            registerBlock(
                    "umdra_log",
                    UmdraLogBlock::new
            );

    public static final DeferredHolder<Block, UmdraWoodBlock> UMDRA_WOOD =
            registerBlock(
                    "umdra_wood",
                    UmdraWoodBlock::new
            );

    public static final DeferredHolder<Block, StrippedUmdraLogBlock> STRIPPED_UMDRA_LOG =
            registerBlock(
                    "stripped_umdra_log",
                    StrippedUmdraLogBlock::new
            );

    public static final DeferredHolder<Block, StrippedUmdraWoodBlock> STRIPPED_UMDRA_WOOD =
            registerBlock(
                    "stripped_umdra_wood",
                    StrippedUmdraWoodBlock::new
            );

    public static final DeferredHolder<Block, UmdraPlanksBlock> UMDRA_PLANKS =
            registerBlock(
                    "umdra_planks",
                    UmdraPlanksBlock::new
            );

    public static final DeferredHolder<Block, UmdraLeavesBlock> UMDRA_LEAVES =
            registerBlock(
                    "umdra_leaves",
                    UmdraLeavesBlock::new
            );

    public static final DeferredHolder<Block, SaplingBlock> UVLA_WILLOW_SAPLING =
            registerBlock(
                    "uvla_willow_sapling",
                    props -> new SaplingBlock(UvlaTreeGrowers.UVLA_WILLOW, props),
                    BlockBehaviour.Properties.of()
                            .noCollision()
                            .randomTicks()
                            .instabreak()
                            .sound(SoundType.GRASS)
            );
    public static final DeferredHolder<Block, SaplingBlock> UVLA_MANGROVE_SAPLING =
            registerBlock(
                    "uvla_mangrove_sapling",
                    props -> new SaplingBlock(UvlaTreeGrowers.UVLA_MANGROVE, props),
                    BlockBehaviour.Properties.of()
                            .noCollision()
                            .randomTicks()
                            .instabreak()
                            .sound(SoundType.GRASS)
            );
    public static final DeferredHolder<Block, SaplingBlock> UVLA_GIANT_SAPLING =
            registerBlock(
                    "uvla_giant_sapling",
                    props -> new SaplingBlock(UvlaTreeGrowers.UVLA_GIANT, props),
                    BlockBehaviour.Properties.of()
                            .noCollision()
                            .randomTicks()
                            .instabreak()
                            .sound(SoundType.GRASS)
            );
    public static final DeferredHolder<Block, SaplingBlock> UMDRA_SAPLING =
            registerBlock(
                    "umdra_sapling",
                    props -> new SaplingBlock(UvlaTreeGrowers.UMDRA, props),
                    BlockBehaviour.Properties.of()
                            .noCollision()
                            .randomTicks()
                            .instabreak()
                            .sound(SoundType.GRASS)
            );

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
    public static final DeferredBlock<StairBlock> UVLA_STAIRS = registerBlock(
            "uvla_stairs",
            properties -> new StairBlock(
                    ModBlocks.UVLA_PLANKS.get().defaultBlockState(),
                    properties.strength(2f).requiresCorrectToolForDrops()
            )
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
        DeferredBlock<T> toReturn = BLOCKS.registerBlock(name, factory);
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
                registryName -> {
                    var key = ResourceKey.create(Registries.BLOCK, registryName);
                    return factory.apply(properties.setId(key));
                }
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

    private static DeferredBlock<DropExperienceBlock> registerOreBlock(
            String name,
            float hardness,
            UniformInt xp,
            int lightLevel
    ) {
        DeferredBlock<DropExperienceBlock> block = BLOCKS.registerBlock(
                name,
                props -> new DropExperienceBlock(
                        xp,
                        props
                                .strength(hardness)
                                .lightLevel(state -> lightLevel)
                                .requiresCorrectToolForDrops()
                )
        );

        registerBlockItem(name, block);
        return block;
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.registerItem(name, (properties) -> new BlockItem(block.get(), properties.useBlockDescriptionPrefix()));
    }

}
