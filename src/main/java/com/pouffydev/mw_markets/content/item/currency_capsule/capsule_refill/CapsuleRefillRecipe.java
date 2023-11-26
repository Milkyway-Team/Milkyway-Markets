package com.pouffydev.mw_markets.content.item.currency_capsule.capsule_refill;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pouffydev.mw_markets.init.MWMItems;
import com.pouffydev.mw_markets.content.currency.CurrencyJsonListener;
import com.pouffydev.mw_markets.content.currency.CurrencyUtils;
import com.pouffydev.mw_markets.content.item.currency_capsule.CurrencyCapsule;
import com.pouffydev.mw_markets.init.MWMSpecialRecipes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class CapsuleRefillRecipe extends CustomRecipe {
    private static final int maxCapacity = CurrencyCapsule.max;
    public CapsuleRefillRecipe(ResourceLocation location) {
        super(location);
    }
    private static ItemStack getCurrencyItem() {
        ItemStack itemStack = ItemStack.EMPTY;
        for (JsonElement jsonElement : CurrencyJsonListener.currency) {
            JsonObject json = jsonElement.getAsJsonObject();
            Item item = CurrencyJsonListener.instance.deserializeItem(new ResourceLocation(GsonHelper.getAsString(json, "item")));
            itemStack = new ItemStack(item);
        }
        return itemStack;
    }
    
    private static final Ingredient currencyIngredient = Ingredient.of(getCurrencyItem());
    @Override
    public boolean matches(CraftingContainer container, Level level) {
        int additions = 0;
        boolean flag = false;
        
        for (int i = 0; i < container.getContainerSize(); ++i) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                if (currencyIngredient.test(stack)) {
                    if (flag) {
                        return false;
                    }
                    
                    flag = true;
                } else {
                    if (currencyIngredient.test(stack)) {
                        ++additions;
                        if (i > maxCapacity) {
                            return false;
                        }
                    }
                }
                
                if (additions > maxCapacity) {
                    return false;
                }
            }
        }
        return flag && additions >= 1;
    }
    
    @Override
    public @NotNull ItemStack assemble(CraftingContainer container) {
        int addedValue = 0;
        ItemStack oldCapsule = ItemStack.EMPTY;
        for(int j = 0; j < container.getContainerSize(); ++j) {
            ItemStack stack = container.getItem(j);
            if (!stack.isEmpty()) {
                if ((stack.getItem()) instanceof CurrencyCapsule) {
                    oldCapsule = stack;
                }
            }
            ItemStack itemstack1 = container.getItem(j);
            if (!itemstack1.isEmpty()) {
                if (currencyIngredient.test(itemstack1)) {
                    //get the value of the itemstack
                    addedValue += CurrencyUtils.getItemsValue(itemstack1);
                }
            }
        }
        CompoundTag oldTag = oldCapsule.getTag();
        int oldValue = oldTag.getInt("RequestedValue");
        ItemStack modifiedCapsule = new ItemStack(MWMItems.currencyCapsule.get());
        CompoundTag compoundtag = modifiedCapsule.getOrCreateTag();
        compoundtag.putInt("RequestedValue", addedValue + oldValue);
        if (compoundtag.getInt("RequestedValue") > maxCapacity) {
            return ItemStack.EMPTY;
        }
        
        return modifiedCapsule;
    }
    
    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }
    
    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return MWMSpecialRecipes.CAPSULE_REFILL.getSerializer();
    }
}
