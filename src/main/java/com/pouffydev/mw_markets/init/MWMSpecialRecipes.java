package com.pouffydev.mw_markets.init;

import com.pouffydev.krystal_core.foundation.data.lang.KrystalCoreLang;
import com.pouffydev.mw_markets.MWMarkets;
import com.pouffydev.mw_markets.content.item.currency_capsule.capsule_refill.CapsuleRefillRecipe;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public enum MWMSpecialRecipes implements IRecipeTypeInfo {
    CAPSULE_REFILL(() -> new SimpleRecipeSerializer<>(CapsuleRefillRecipe::new), () -> RecipeType.CRAFTING, false),
    ;
    private final ResourceLocation id;
    private final RegistryObject<RecipeSerializer<?>> serializerObject;
    @Nullable
    private final RegistryObject<RecipeType<?>> typeObject;
    private final Supplier<RecipeType<?>> type;
    
    MWMSpecialRecipes(Supplier<RecipeSerializer<?>> serializerSupplier, Supplier<RecipeType<?>> typeSupplier, boolean registerType) {
        String name = KrystalCoreLang.asId(name());
        id = MWMarkets.asResource(name);
        serializerObject = Registers.SERIALIZER_REGISTER.register(name, serializerSupplier);
        if (registerType) {
            typeObject = Registers.TYPE_REGISTER.register(name, typeSupplier);
            type = typeObject;
        } else {
            typeObject = null;
            type = typeSupplier;
        }
    }
    public static <T extends Recipe<?>> RecipeType<T> simpleType(ResourceLocation id) {
        String stringId = id.toString();
        return new RecipeType<T>() {
            @Override
            public String toString() {
                return stringId;
            }
        };
    }
    MWMSpecialRecipes(Supplier<RecipeSerializer<?>> serializerSupplier) {
        String name = KrystalCoreLang.asId(name());
        id = MWMarkets.asResource(name);
        serializerObject = Registers.SERIALIZER_REGISTER.register(name, serializerSupplier);
        typeObject = Registers.TYPE_REGISTER.register(name, () -> simpleType(id));
        type = typeObject;
    }
    @Override
    public ResourceLocation getId() {
        return id;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T extends RecipeSerializer<?>> T getSerializer() {
        return (T) serializerObject.get();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T extends RecipeType<?>> T getType() {
        return (T) type.get();
    }
    
    public static void register(IEventBus modEventBus) {
        ShapedRecipe.setCraftingSize(9, 9);
        Registers.SERIALIZER_REGISTER.register(modEventBus);
        Registers.TYPE_REGISTER.register(modEventBus);
    }
    
    private static class Registers {
        private static final DeferredRegister<RecipeSerializer<?>> SERIALIZER_REGISTER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MWMarkets.ID);
        private static final DeferredRegister<RecipeType<?>> TYPE_REGISTER = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, MWMarkets.ID);
    }
}
