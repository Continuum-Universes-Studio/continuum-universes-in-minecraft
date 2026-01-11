package net.continuumuniverses.renderer;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.item.BlockModelWrapper;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class EmissiveItemModel implements ItemModel {
    private static final String EMISSIVE_SUFFIX = "_emissive";
    @Nullable
    private static final Field QUADS_FIELD = getField(BlockModelWrapper.class, "quads");
    @Nullable
    private static final Field TINTS_FIELD = getField(BlockModelWrapper.class, "tints");
    @Nullable
    private static final Field PROPERTIES_FIELD = getField(BlockModelWrapper.class, "properties");
    @Nullable
    private static final Field RENDER_TYPE_FIELD = getField(BlockModelWrapper.class, "renderType");

    private final ItemModel baseModel;
    @Nullable
    private final ItemModel emissiveModel;

    private EmissiveItemModel(ItemModel baseModel, @Nullable ItemModel emissiveModel) {
        this.baseModel = baseModel;
        this.emissiveModel = emissiveModel;
    }

    public static ItemModel wrapIfEmissive(ItemModel model) {
        if (!(model instanceof BlockModelWrapper wrapper)) {
            return model;
        }
        if (QUADS_FIELD == null || TINTS_FIELD == null || PROPERTIES_FIELD == null || RENDER_TYPE_FIELD == null) {
            return model;
        }

        List<BakedQuad> quads = getFieldValue(QUADS_FIELD, wrapper, List.of());
        if (!hasEmissiveQuads(quads)) {
            return model;
        }

        List<BakedQuad> baseQuads = new ArrayList<>();
        List<BakedQuad> emissiveQuads = new ArrayList<>();
        for (BakedQuad quad : quads) {
            if (isEmissiveQuad(quad)) {
                emissiveQuads.add(quad);
            } else {
                baseQuads.add(quad);
            }
        }

        List<?> tints = getFieldValue(TINTS_FIELD, wrapper, List.of());
        ModelRenderProperties properties = getFieldValue(PROPERTIES_FIELD, wrapper, null);
        RenderType baseRenderType = getFieldValue(RENDER_TYPE_FIELD, wrapper, null);

        if (properties == null) {
            return model;
        }

        BlockModelWrapper baseModel = new BlockModelWrapper(castTints(tints), baseQuads, properties, baseRenderType);
        RenderType emissiveRenderType = ModRenderTypes.emissive(TextureAtlas.LOCATION_BLOCKS);
        BlockModelWrapper emissiveModel = new BlockModelWrapper(castTints(tints), emissiveQuads, properties, emissiveRenderType);
        return new EmissiveItemModel(baseModel, emissiveQuads.isEmpty() ? null : emissiveModel);
    }

    @Override
    public void update(
            ItemStackRenderState renderState,
            ItemStack stack,
            ItemModelResolver itemModelResolver,
            ItemDisplayContext displayContext,
            @Nullable net.minecraft.client.multiplayer.ClientLevel level,
            @Nullable ItemOwner owner,
            int seed
    ) {
        baseModel.update(renderState, stack, itemModelResolver, displayContext, level, owner, seed);
        if (emissiveModel != null) {
            emissiveModel.update(renderState, stack, itemModelResolver, displayContext, level, owner, seed);
        }
    }

    private static boolean hasEmissiveQuads(List<BakedQuad> quads) {
        for (BakedQuad quad : quads) {
            if (isEmissiveQuad(quad)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isEmissiveQuad(BakedQuad quad) {
        TextureAtlasSprite sprite = quad.sprite();
        return sprite.contents().name().getPath().endsWith(EMISSIVE_SUFFIX);
    }

    @SuppressWarnings("unchecked")
    private static List<net.minecraft.client.color.item.ItemTintSource> castTints(List<?> tints) {
        return (List<net.minecraft.client.color.item.ItemTintSource>) tints;
    }

    @Nullable
    private static Field getField(Class<?> owner, String name) {
        try {
            Field field = owner.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException ex) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T getFieldValue(Field field, Object target, T fallback) {
        try {
            Object value = field.get(target);
            return value == null ? fallback : (T) value;
        } catch (IllegalAccessException ex) {
            return fallback;
        }
    }
}
