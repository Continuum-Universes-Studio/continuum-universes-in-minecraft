package net.continuumuniverses.item;

import net.continuumuniverses.ContinuumUniverses;
import net.minecraft.world.food.FoodProperties;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.minecraft.world.item.*;

import java.util.function.Function;

import static net.continuumuniverses.item.ModFoodProperties.*;

public class ModItems {

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(ContinuumUniverses.MODID);

    // Food
    public static final DeferredItem<CustomItem> KARGOROKBLOOD =
            foodItem(
                    "kargorokblood",
                    CustomItem::new,
                    KARGOROKBLOOD_FOOD
            );

    public static final DeferredItem<CustomItem> BORGAMEAT =
            foodItem(
                    "borgameat",
                    CustomItem::new,
                    BORGAMEAT_FOOD
            );

    public static final DeferredItem<CustomItem> COOKED_BORGAMEAT =
            foodItem(
                    "cooked_borgameat",
                    CustomItem::new,
                    COOKED_BORGAMEAT_FOOD
            );
    // Plasma fuels
    public static final DeferredItem<CustomItem> KORMIKEST_GELATIN =
            simpleItem("kormikest_gelatin", CustomItem::new);

    public static final DeferredItem<CustomItem> KORMIKEST_DUST =
            simpleItem("kormikest_dust", CustomItem::new);

    // Ingots / materials
    public static final DeferredItem<CustomItem> LARZIKEST_INGOT =
            simpleItem("larzikest_ingot", CustomItem::new);

    public static final DeferredItem<CustomItem> VARKEST_INGOT =
            simpleItem("varkest_ingot", CustomItem::new);

    public static final DeferredItem<CustomItem> STARSTEEL_INGOT =
            simpleItem("starsteel_ingot", CustomItem::new);

    public static final DeferredItem<CustomItem> LETHURKEST_GEM =
            simpleItem("lethurkest_gem", CustomItem::new);

    public static final DeferredItem<CustomItem> RAW_VARKEST =
            simpleItem("raw_varkest", CustomItem::new);

    public static final DeferredItem<CustomItem> RAW_LARZIKEST =
            simpleItem("raw_larzikest", CustomItem::new);

    public static final DeferredItem<CustomItem> RAW_STARSTEEL =
            simpleItem("raw_starsteel", CustomItem::new);


    // Tools (your custom classes should accept Item.Properties)
    public static final DeferredItem<Item> LARZIKEST_PICKAXE =
            tool("larzikest_pickaxe", LarzikestPickaxeItem::new);

    public static final DeferredItem<Item> LARZIKEST_SWORD =
            tool("larzikest_sword", LarzikestSwordItem::new);

    public static final DeferredItem<HoeItem> LARZIKEST_HOE =
            tool("larzikest_hoe", LarzikestHoeItem::new);

    public static final DeferredItem<ShovelItem> LARZIKEST_SHOVEL =
            tool("larzikest_shovel", LarzikestShovelItem::new);

    public static final DeferredItem<AxeItem> LARZIKEST_AXE =
            tool("larzikest_axe", LarzikestAxeItem::new);

    // Starsteel Tools (your custom classes should accept Item.Properties)
    public static final DeferredItem<Item> STARSTEEL_PICKAXE =
           tool("starsteel_pickaxe", StarSteelPickaxeItem::new);

    public static final DeferredItem<Item> STARSTEEL_SWORD =
            tool("starsteel_sword", StarSteelSwordItem::new);

    public static final DeferredItem<HoeItem> STARSTEEL_HOE =
            tool("starsteel_hoe", StarSteelHoeItem::new);

    public static final DeferredItem<ShovelItem> STARSTEEL_SHOVEL =
            tool("starsteel_shovel", StarSteelShovelItem::new);

    public static final DeferredItem<AxeItem> STARSTEEL_AXE =
            tool("starsteel_axe", StarSteelAxeItem::new);

    // Varkest Tools(your custom classes should accept Item.Properties)
    public static final DeferredItem<Item> VARKEST_PICKAXE =
            tool("varkest_pickaxe", VarkestPickaxeItem::new);

    public static final DeferredItem<Item> VARKEST_SWORD =
            tool("varkest_sword", VarkestSwordItem::new);

    public static final DeferredItem<HoeItem> VARKEST_HOE =
            tool("varkest_hoe", VarkestHoeItem::new);

    public static final DeferredItem<ShovelItem> VARKEST_SHOVEL =
            tool("varkest_shovel", VarkestShovelItem::new);

    public static final DeferredItem<AxeItem> VARKEST_AXE =
            tool("varkest_axe", VarkestAxeItem::new);
    // Example “special item”
    public static final DeferredItem<Item> UVLA_ITEM =
            ITEMS.registerItem("uvla_item", UvlaItem::new);
    private static <T extends Item> DeferredItem<T> tool(
            String name,
            Function<Item.Properties, T> factory
    ) {
        return ITEMS.registerItem(name, factory);
    }


    private static <T extends Item> DeferredItem<T> simpleItem(
            String name,
            Function<Item.Properties, T> factory
    ) {
        return ITEMS.registerItem(name, factory);
    }


    private static <T extends Item> DeferredItem<T> foodItem(
            String name,
            Function<Item.Properties, T> factory,
            FoodProperties food
    ) {
        return ITEMS.registerItem(
                name,
                properties -> factory.apply(properties.food(food))
        );
    }


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
