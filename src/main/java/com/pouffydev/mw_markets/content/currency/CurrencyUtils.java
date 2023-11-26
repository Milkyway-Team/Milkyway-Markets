package com.pouffydev.mw_markets.content.currency;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class CurrencyUtils {
    public static final String TAG_CURRENCY = "Currency";
    public static final String TAG_INVENTORY_VALUE = "PlayerValue";
    public static int inventoryValue;
    public static int playerValue;
    public CurrencyUtils() {
    }
    
    public static int getItemsValue(ItemStack itemStack) {
        int value = 0;
        for (JsonElement jsonElement : CurrencyJsonListener.currency) {
            JsonObject json = jsonElement.getAsJsonObject();
            if (itemStack.getItem() == CurrencyJsonListener.instance.deserializeItem(new ResourceLocation(GsonHelper.getAsString(json, "item")))) {
                value += json.get("value").getAsInt() * itemStack.getCount();
            }
        }
        return value;
    }
    
    public static boolean itemStackHasValue(ItemStack itemStack) {
        for (JsonElement jsonElement : CurrencyJsonListener.currency) {
            JsonObject json = jsonElement.getAsJsonObject();
            if (itemStack.getItem() == CurrencyJsonListener.instance.deserializeItem(new ResourceLocation(GsonHelper.getAsString(json, "item")))) {
                return true;
            }
        }
        return false;
    }
    public static int getInventoryValue() {
        return inventoryValue;
    }
    public static void setInventoryValue(int inventoryValue) {
        CurrencyUtils.inventoryValue = inventoryValue;
    }
    
    public static int readPlayerValue(Player player) {
        CompoundTag compoundTag = player.getPersistentData();
        CompoundTag moneyTag = compoundTag.getCompound(TAG_CURRENCY);
        return moneyTag.getInt(TAG_INVENTORY_VALUE);
    }
    
    public static void writePlayerValue(Player player, int value) {
        CompoundTag compoundTag = player.getPersistentData();
        CompoundTag moneyTag = compoundTag.getCompound(TAG_CURRENCY);
        moneyTag.putInt(TAG_INVENTORY_VALUE, value);
        compoundTag.put(TAG_CURRENCY, moneyTag);
    }
    public static List<ItemStack> pickItemsByValue(int requestedCurrency, Inventory inventory) {
        List<ItemStack> itemsToRemove = new ArrayList<>();
        int remainingCurrency = requestedCurrency;
        
        // Iterate through each slot in the inventory
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack itemStack = inventory.getItem(slot);
            
            // Check if the item stack is not empty and has a value
            if (!itemStack.isEmpty() && itemStackHasValue(itemStack)) {
                int itemValue = getItemsValue(itemStack);
                
                // Check if the item value is less than or equal to the remaining currency
                if (itemValue <= remainingCurrency) {
                    // Create a copy of the item stack and set its count to the number of items to be removed
                    ItemStack itemsToRemoveStack = itemStack.copy();
                    itemsToRemoveStack.setCount(itemValue);
                    
                    // Add the item stack to the list of items to be removed
                    itemsToRemove.add(itemsToRemoveStack);
                    
                    // Subtract the item value from the remaining currency
                    remainingCurrency -= itemValue;
                    
                    // Break the loop if the remaining currency becomes zero
                    if (remainingCurrency == 0) {
                        break;
                    }
                }
            }
        }
        
        return itemsToRemove;
    }
    public static void removeItemsFromInventory(List<ItemStack> itemsToRemove, Inventory inventory) {
        for (ItemStack itemToRemove : itemsToRemove) {
            int countToRemove = itemToRemove.getCount();
            
            // Iterate through each slot in the inventory
            for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
                ItemStack itemStack = inventory.getItem(slot);
                
                // Check if the item stack matches the item to be removed
                if (ItemStack.isSame(itemStack, itemToRemove)) {
                    int countInSlot = itemStack.getCount();
                    
                    // If the count in the slot is less than or equal to the count to be removed,
                    // remove the entire stack from the slot
                    if (countInSlot <= countToRemove) {
                        inventory.setItem(slot, ItemStack.EMPTY);
                        countToRemove -= countInSlot;
                    }
                    // If the count in the slot is greater than the count to be removed,
                    // decrease the count of the stack in the slot
                    else {
                        itemStack.setCount(countInSlot - countToRemove);
                        inventory.setItem(slot, itemStack);
                        countToRemove = 0;
                    }
                    
                    // Break the loop if all items have been removed
                    if (countToRemove == 0) {
                        break;
                    }
                }
            }
        }
    }
    
    public static void spawnItemEntitiesWithValue(Level world, Player player, int requestedValue) {
        int remainingValue = requestedValue;
        
        for (JsonElement jsonElement : CurrencyJsonListener.currency) {
            JsonObject json = jsonElement.getAsJsonObject();
            Item item = CurrencyJsonListener.instance.deserializeItem(new ResourceLocation(GsonHelper.getAsString(json, "item")));
            int itemValue = json.get("value").getAsInt();
            
            // Check if the item value is less than or equal to the remaining value
            if (itemValue <= remainingValue) {
                // Calculate the number of items needed to reach the remaining value
                int quantity = remainingValue / itemValue;
                
                // Create item stack with the specified quantity and item
                ItemStack itemStack = new ItemStack(item, quantity);
                
                // Spawn item entity above the player's head
                ItemEntity itemEntity = new ItemEntity(world, player.getX(), player.getY() + 1, player.getZ(), itemStack);
                itemEntity.setDeltaMovement(0, 0.3, 0); // Adjust the motion if needed
                world.addFreshEntity(itemEntity);
                
                // Subtract the item value multiplied by the quantity from the remaining value
                remainingValue -= itemValue * quantity;
                
                // Break the loop if the remaining value becomes zero
                if (remainingValue == 0) {
                    break;
                }
            }
        }
    }
}
