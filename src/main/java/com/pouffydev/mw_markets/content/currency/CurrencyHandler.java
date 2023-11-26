package com.pouffydev.mw_markets.content.currency;

import cofh.core.item.InventoryContainerItem;
import cofh.lib.inventory.SimpleItemInv;
import cofh.thermal.lib.item.InventoryContainerItemAugmentable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import static com.pouffydev.mw_markets.content.currency.CurrencyJsonListener.currency;

public class CurrencyHandler {
    public static int inventoryValue;
    public CurrencyHandler() {
    }
    public static void updateInventoryValue(Player player) {
        int newInventoryValue = 0;
        for (JsonElement jsonElement : currency) {
            JsonObject json = jsonElement.getAsJsonObject();
            for (ItemStack itemStack : player.getInventory().items) {
                if (!(itemStack.getItem() instanceof InventoryContainerItem)) {
                    if (itemStack.getItem() == CurrencyJsonListener.instance.deserializeItem(new ResourceLocation(GsonHelper.getAsString(json, "item")))) {
                        newInventoryValue += json.get("value").getAsInt() * itemStack.getCount();
                    }
                } else if (itemStack.getItem() instanceof InventoryContainerItem) {
                    SimpleItemInv containerInventory = ((InventoryContainerItem) itemStack.getItem()).getContainerInventory(itemStack);
                    for (int i = 0; i < containerInventory.getSlots(); i++) {
                        ItemStack itemStack1 = containerInventory.getStackInSlot(i);
                        if (itemStack1.getItem() == CurrencyJsonListener.instance.deserializeItem(new ResourceLocation(GsonHelper.getAsString(json, "item")))) {
                            newInventoryValue += json.get("value").getAsInt() * itemStack1.getCount();
                        }
                    }
                }
                inventoryValue = newInventoryValue;
            }
        }
    }
}
