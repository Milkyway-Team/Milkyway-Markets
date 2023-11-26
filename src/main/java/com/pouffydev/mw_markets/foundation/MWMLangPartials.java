package com.pouffydev.mw_markets.foundation;

import com.google.gson.JsonElement;
import com.pouffydev.krystal_core.foundation.data.lang.KrystalCoreLang;
import com.pouffydev.krystal_core.foundation.data.lang.LangPartial;
import com.pouffydev.mw_markets.MWMarkets;

import java.util.function.Supplier;

public enum MWMLangPartials implements LangPartial {
    
    //INTERFACE("Milkyway's UI & Messages"),
    TOOLTIPS("Milkyway Market's Item Descriptions"),
    //TINKERS("Create: Milkyway's Tinkers' Construct Compatibility"),
    //FLUIDS("Create: Milkyway's Fluids"),
    //ADVANCEMENTS("Create: Milkyway's Advancements", MWAdvancements::provideLangEntries),
    
    ;
    
    private final String displayName;
    private final Supplier<JsonElement> provider;
    
    private MWMLangPartials(String displayName) {
        this.displayName = displayName;
        String fileName = KrystalCoreLang.asId(name());
        this.provider = () -> LangPartial.fromResource(MWMarkets.ID, fileName);
    }
    
    private MWMLangPartials(String displayName, Supplier<JsonElement> provider) {
        this.displayName = displayName;
        this.provider = provider;
    }
    
    @Override
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public JsonElement provide() {
        return provider.get();
    }
}
