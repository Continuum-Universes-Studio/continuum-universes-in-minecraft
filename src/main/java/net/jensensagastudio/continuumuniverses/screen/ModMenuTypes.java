package net.jensensagastudio.continuumuniverses.screen;

import net.jensensagastudio.continuumuniverses.ContinuumUniverses;
import net.jensensagastudio.continuumuniverses.screen.custom.LunarForgeMenu;
import net.jensensagastudio.continuumuniverses.screen.custom.PlasmaFurnaceMenu;
import net.jensensagastudio.continuumuniverses.screen.custom.StellarForgeMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CrafterMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(
                    Registries.MENU,
                    ContinuumUniverses.MODID
            );
    public static final DeferredHolder<MenuType<?>, MenuType<PlasmaFurnaceMenu>>
            PLASMA_FURNACE_MENU =
            MENU_TYPES.register(
                    "plasma_furnace",
                    () -> IMenuTypeExtension.create(PlasmaFurnaceMenu::new)
            );
    public static final DeferredHolder<MenuType<?>, MenuType<StellarForgeMenu>> STELLAR_FORGE_MENU =
            MENU_TYPES.register(
                    "stellar_forge",
                    () -> IMenuTypeExtension.create(StellarForgeMenu::new)
            );
    public static final DeferredHolder<MenuType<?>, MenuType<LunarForgeMenu>> LUNAR_FORGE_MENU =
            MENU_TYPES.register(
                    "lunar_forge",
                    () -> IMenuTypeExtension.create(LunarForgeMenu::new)
            );

    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(String name,
                                                                                                               IContainerFactory<T> factory) {
        return MENU_TYPES.register(name, () -> IMenuTypeExtension.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }
}
