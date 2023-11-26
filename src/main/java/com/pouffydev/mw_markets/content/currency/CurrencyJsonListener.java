package com.pouffydev.mw_markets.content.currency;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.pouffydev.mw_markets.MWMarkets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CurrencyJsonListener extends SimpleJsonResourceReloadListener {
    public static List<JsonElement> currency;
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    public static final CurrencyJsonListener instance = new CurrencyJsonListener();
    public CurrencyJsonListener() {
        super(GSON,"currency");
        currency = new ArrayList<>();
        LOGGER.info("Populating currency list...");
    }
    
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> files, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        currency = new ArrayList<>();
        files.forEach((resourceLocation, jsonElement) -> {
            LOGGER.info("%s ||| %s".formatted(resourceLocation, jsonElement));
            currency.add(jsonElement);
        });
        for (JsonElement jsonElement : currency) {
            JsonObject json = jsonElement.getAsJsonObject();
            String item = json.get("item").getAsString();
            LOGGER.debug("Registered currency item: %s".formatted(item));
        }
    }
    public Item deserializeItem(ResourceLocation name) {
        Item item = ForgeRegistries.ITEMS.getValue(name);
        
        if (item == null) {
            MWMarkets.LOGGER.error("Item " + name + " does not exist.");
            throw new IllegalArgumentException();
        }
        return item;
    }
}
