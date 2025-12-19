package net.jensensagastudio.continuumuniverses.fluid;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.resources.ResourceKey;

import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class FluidHelper {

    // Keep these in one place so every fluid uses the same registries.
    public final DeferredRegister<FluidType> FLUID_TYPES;
    public final DeferredRegister<Fluid> FLUIDS;
    public final DeferredRegister<Block> BLOCKS;
    public final DeferredRegister<Item> ITEMS;

    public FluidHelper(String modid) {
        this.FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, modid);
        this.FLUIDS = DeferredRegister.create(Registries.FLUID, modid);
        this.BLOCKS = DeferredRegister.create(Registries.BLOCK, modid);
        this.ITEMS = DeferredRegister.create(Registries.ITEM, modid);
    }

    /** Everything you typically want back from a fluid registration. */
    public record FluidSet(
            DeferredHolder<FluidType, FluidType> type,
            DeferredHolder<Fluid, BaseFlowingFluid.Source> source,
            DeferredHolder<Fluid, BaseFlowingFluid.Flowing> flowing,
            DeferredHolder<Block, LiquidBlock> block,
            DeferredHolder<Item, BucketItem> bucket,
            BaseFlowingFluid.Properties properties
    ) {}

    /**
     * Registers: FluidType + Source + Flowing + Block + Bucket.
     *
     * @param name registry name base (e.g. "kormikest")
     * @param typeProps FluidType properties builder
     * @param propsTweak tweak BaseFlowingFluid.Properties (tickRate, slope distance, etc.)
     * @param blockPropsTweak tweak the LiquidBlock properties (collision, strength, etc.)
     * @param bucketPropsTweak tweak the BucketItem properties (rarity, fireResistant, etc.)
     */
    public FluidSet registerStandard(
            String name,
            FluidType.Properties typeProps,
            Function<BaseFlowingFluid.Properties, BaseFlowingFluid.Properties> propsTweak,
            Function<BlockBehaviour.Properties, BlockBehaviour.Properties> blockPropsTweak,
            Function<Item.Properties, Item.Properties> bucketPropsTweak
    ) {
        var type = FLUID_TYPES.register(name, () -> new FluidType(typeProps));

        // We'll fill this AFTER we create the holders. The lambdas run later during registry events.
        final BaseFlowingFluid.Properties[] propsBox = new BaseFlowingFluid.Properties[1];

        var source = FLUIDS.register(name, () -> new BaseFlowingFluid.Source(propsBox[0]));
        var flowing = FLUIDS.register(name + "_flowing", () -> new BaseFlowingFluid.Flowing(propsBox[0]));

        var block = BLOCKS.register(name + "_block", registryName -> {
            var key = ResourceKey.create(Registries.BLOCK, registryName);
            var props = BlockBehaviour.Properties.of().setId(key).noCollision();
            props = blockPropsTweak.apply(props);
            return new LiquidBlock(source.get(), props);
        });

        var bucket = ITEMS.register(name + "_bucket", registryName -> {
            var key = ResourceKey.create(Registries.ITEM, registryName);
            var props = new Item.Properties()
                    .stacksTo(1)
                    .craftRemainder(Items.BUCKET);
            props = bucketPropsTweak.apply(props);
            return new BucketItem(source.get(), props);
        });

        var props = new BaseFlowingFluid.Properties(type::get, source::get, flowing::get)
                .block(() -> (LiquidBlock) block.get())
                .bucket(bucket::get);

        props = propsTweak.apply(props);
        propsBox[0] = props;

        return new FluidSet(type, source, flowing, block, bucket, props);
    }

    /** Convenience overload with sane defaults. */

    public FluidSet registerStandard(
            String name,
            FluidType.Properties typeProps,
            Function<BaseFlowingFluid.Properties, BaseFlowingFluid.Properties> propsTweak,
            Function<BlockBehaviour.Properties, BlockBehaviour.Properties> blockPropsTweak,
            Function<Item.Properties, Item.Properties> bucketPropsTweak,
            BiFunction<Supplier<? extends FlowingFluid>, BlockBehaviour.Properties, LiquidBlock> blockFactory
    ) {
        var type = FLUID_TYPES.register(name, () -> new FluidType(typeProps));

        final BaseFlowingFluid.Properties[] propsBox = new BaseFlowingFluid.Properties[1];

        var source = FLUIDS.register(name, () -> new BaseFlowingFluid.Source(propsBox[0]));
        var flowing = FLUIDS.register(name + "_flowing", () -> new BaseFlowingFluid.Flowing(propsBox[0]));

        var block = BLOCKS.register(name + "_block", registryName -> {
            var key = ResourceKey.create(Registries.BLOCK, registryName);
            var props = BlockBehaviour.Properties.of().setId(key).noCollision();
            props = blockPropsTweak.apply(props);
            return blockFactory.apply(source::get, props); // <<< HERE
        });
        var bucket = ITEMS.register(name + "_bucket", registryName -> {
            var key = ResourceKey.create(Registries.ITEM, registryName);
            var props = new Item.Properties()
                    .setId(key)
                    .stacksTo(1)
                    .craftRemainder(Items.BUCKET);
            props = bucketPropsTweak.apply(props);
            return new BucketItem(source.get(), props);
        });

        var props = new BaseFlowingFluid.Properties(type::get, source::get, flowing::get)
                .block(() -> (LiquidBlock) block.get())
                .bucket(bucket::get);

        props = propsTweak.apply(props);
        propsBox[0] = props;

        return new FluidSet(type, source, flowing, block, bucket, props);
    }
}
