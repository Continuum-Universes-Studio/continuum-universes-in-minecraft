package net.continuumuniverses.tags;

import net.continuumuniverses.ContinuumUniverses;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {

    public static class Items {

        public static final TagKey<Item> CELESTIAL_METALS =
                tag("celestial_metals");

        public static final TagKey<Item> ADVANCED_FUELS =
                tag("advanced_fuels");
        public static final TagKey<Item> LARZIKEST_TOOL_REPAIR_ITEMS =
                tag("larzikest_tool_repair_items");
        public static final TagKey<Item> VARKEST_TOOL_REPAIR_ITEMS =
                tag("varkest_tool_repair_items");
        public static final TagKey<Item> STARSTEEL_TOOL_REPAIR_ITEMS =
                tag("starsteel_tool_repair_items");
        private static TagKey<Item> tag(String name) {
            return TagKey.create(
                    Registries.ITEM,
                    ResourceLocation.fromNamespaceAndPath(
                            ContinuumUniverses.MODID,
                            name
                    )
            );
        }
    }
    public static class Blocks {

        public static final TagKey<Block> INCORRECT_FOR_LARZIKEST_TOOLS =
                tag("incorrect_for_larzikest_tools");
        public static final TagKey<Block> INCORRECT_FOR_VARKEST_TOOLS =
                tag("incorrect_for_varkest_tools");
        public static final TagKey<Block> INCORRECT_FOR_STARSTEEL_TOOLS =
                tag("incorrect_for_starsteel_tools");
        public static final TagKey<Block> SELF_DROPS =
                tag("self_drops");

        private static TagKey<Block> tag(String name) {
            return TagKey.create(
                    Registries.BLOCK,
                    ResourceLocation.fromNamespaceAndPath(
                            ContinuumUniverses.MODID,
                            name
                    )
            );
        }
    }
}
