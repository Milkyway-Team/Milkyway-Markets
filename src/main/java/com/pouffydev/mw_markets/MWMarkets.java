package com.pouffydev.mw_markets;

import com.mojang.logging.LogUtils;
import com.pouffydev.krystal_core.foundation.KrystalCoreRegistrate;
import com.pouffydev.krystal_core.foundation.data.lang.KrystalCoreLangMerger;
import com.pouffydev.mw_markets.content.currency.CurrencyJsonListener;
import com.pouffydev.mw_markets.content.currency.CurrencyOverlay;
import com.pouffydev.mw_markets.foundation.MWMLangPartials;
import com.pouffydev.mw_markets.init.MWMItems;
import com.pouffydev.mw_markets.init.MWMSpecialRecipes;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("mw_markets")
public class MWMarkets
{
    public static final String ID = "mw_markets";
    /**
     * Milkyway Core's Registrate
     */
    public static final KrystalCoreRegistrate registrate = KrystalCoreRegistrate.create(MWMarkets.ID);
    /**
     * Milkyway Core's Logger
     */
    public static final Logger LOGGER = LogUtils.getLogger();
    
    public static final CreativeModeTab itemGroup = new CreativeModeTab(ID) {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(CurrencyOverlay.getDisplayedCoin().getItem());
        }
    };
    public MWMarkets()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

        MinecraftForge.EVENT_BUS.register(this);
        registrate.registerEventListeners(eventBus);
        MWMSpecialRecipes.register(eventBus);
        MWMItems.register();
        
        forgeEventBus.addListener(this::jsonReading);
        eventBus.addListener(EventPriority.LOWEST, MWMarkets::gatherData);
        
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> MWMarketsClient.onCtorClient(eventBus, forgeEventBus));
    }
    public void jsonReading(AddReloadListenerEvent event) {
        event.addListener(CurrencyJsonListener.instance);
    }
    
    public static void gatherData(@NotNull GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        if (event.includeClient()) {
            gen.addProvider(new KrystalCoreLangMerger(gen, ID, "Milkyway Markets", MWMLangPartials.values()));
        }
    }
    private void setup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        InterModComms.sendTo(ID, "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.messageSupplier().get()).
                collect(Collectors.toList()));
    }
    @Contract("_ -> new")
    public static @NotNull ResourceLocation asResource(String path) {
        return new ResourceLocation(ID, path);
    }
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("HELLO from server starting");
    }
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent)
        {
            LOGGER.info("HELLO from Register Block");
        }
    }
}
