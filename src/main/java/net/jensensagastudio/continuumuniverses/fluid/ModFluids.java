package net.jensensagastudio.continuumuniverses.fluid;

import net.jensensagastudio.continuumuniverses.block.KormikestFluidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.FluidType;

import static net.jensensagastudio.continuumuniverses.ContinuumUniverses.MODID;

public class ModFluids {
    public static final FluidHelper HELPER = new FluidHelper(MODID);

    public static final FluidHelper.FluidSet KORMIKEST =
            HELPER.registerStandard(
                    "kormikest",
                    FluidType.Properties.create()
                            .density(1500)
                            .viscosity(3000)
                            .temperature(350)
                            .lightLevel(2),
                    p -> p.tickRate(5).slopeFindDistance(4).levelDecreasePerBlock(1).explosionResistance(100.0f),
                    p -> p
                            .mapColor(MapColor.WATER)
                            .strength(100f)
                            .hasPostProcess((bs, br, bp) -> true)
                            .emissiveRendering((bs, br, bp) -> true)
                            .lightLevel(s -> 2)
                            .noCollision()
                            .liquid()
                            .pushReaction(PushReaction.DESTROY)
                            .sound(SoundType.EMPTY)
                            .replaceable(),
                    p -> p,
                    (fluid, props) -> new KormikestFluidBlock(fluid.get(), props)

            );

    public static void register(IEventBus bus) {
        HELPER.FLUID_TYPES.register(bus);
        HELPER.FLUIDS.register(bus);
        HELPER.BLOCKS.register(bus);
        HELPER.ITEMS.register(bus);
    }
}
