package com.pouffydev.mw_markets;

import com.pouffydev.mw_markets.content.currency.CurrencyOverlay;
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
}
