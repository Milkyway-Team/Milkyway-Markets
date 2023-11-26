package com.pouffydev.mw_markets;

import com.pouffydev.krystal_core.KrystalCore;
import com.pouffydev.mw_markets.content.currency.CurrencyOverlay;
import com.pouffydev.mw_markets.content.currency.CurrencyUtils;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class MWMarketsClient {
    
    public static void onCtorClient(IEventBus modEventBus, IEventBus forgeEventBus) {
        modEventBus.addListener(MWMarketsClient::clientInit);
    }
    public static void clientInit(final FMLClientSetupEvent event) {
        registerOverlays();
    }
    private static void registerOverlays() {
        OverlayRegistry.registerOverlayAbove(ForgeIngameGui.AIR_LEVEL_ELEMENT, "Milkyway's Currency", CurrencyOverlay.INSTANCE);
    }
    public static void registerModelPredicates() {
        ItemProperties.registerGeneric(new ResourceLocation(MWMarkets.ID, "creative"), (pStack, pLevel, pEntity, pSeed) -> {
            //if the Item has the tag isCreative set to true
            CompoundTag nbt = pStack.getOrCreateTag();
            if (nbt.getBoolean("isCreative")) {
                return 1.0F;
            }
            return 0.0F;
        });
        ItemProperties.registerGeneric(new ResourceLocation(MWMarkets.ID, "currency_value"), (pStack, pLevel, pEntity, pSeed) -> {
            //return the value of the Item. Round the value of the item to the specified value, so if the item has a value of 101 but the model only specifies 100, it will return 100.
            CompoundTag nbt = pStack.getOrCreateTag();
            int requestedValue = nbt.getInt("RequestedValue");
            int value = CurrencyUtils.getItemsValue(pStack);
            return Math.min(value, requestedValue);
        });
    }
}
