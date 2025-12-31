package net.continuumuniverses.item;

import net.continuumuniverses.tags.ModTags;
import net.minecraft.world.item.ToolMaterial;

public class ModToolMaterials {

    public static final ToolMaterial STARSTEEL = new ToolMaterial(
            ModTags.Blocks.INCORRECT_FOR_STARSTEEL_TOOLS,
            10000,
            12.0f,
            40.0f,
            8,
            ModTags.Items.STARSTEEL_TOOL_REPAIR_ITEMS
    );
    public static final ToolMaterial LARZIKEST = new ToolMaterial(
            ModTags.Blocks.INCORRECT_FOR_LARZIKEST_TOOLS,
            24000,
            24.0f,
            60.0f,
            9,
            ModTags.Items.LARZIKEST_TOOL_REPAIR_ITEMS
    );
    public static final ToolMaterial VARKEST = new ToolMaterial(
            ModTags.Blocks.INCORRECT_FOR_VARKEST_TOOLS,
            50000,
            32.0f,
            120.0f,
            10,
            ModTags.Items.VARKEST_TOOL_REPAIR_ITEMS
    );
}
