package com.pouffydev.mw_markets.init;

import com.pouffydev.krystal_core.foundation.KrystalCoreRegistrate;
import com.pouffydev.mw_markets.MWMarkets;
import com.pouffydev.mw_markets.content.item.currency_capsule.CurrencyCapsule;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Rarity;

public class MWMItems {
    private static final KrystalCoreRegistrate REGISTRATE = MWMarkets.registrate.creativeModeTab(() -> MWMarkets.itemGroup);
    public static final ItemEntry<CurrencyCapsule> currencyCapsule = REGISTRATE.item("currency_capsule", CurrencyCapsule::new)
            .properties(p->p.tab(MWMarkets.itemGroup).rarity(Rarity.UNCOMMON))
            .register();
    
    
    
    
    
    public static void register() {
    }
}
