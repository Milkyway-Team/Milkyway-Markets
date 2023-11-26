package com.pouffydev.mw_markets.content.currency;

import com.mojang.blaze3d.vertex.PoseStack;
import com.pouffydev.krystal_core.foundation.data.lang.Components;
import com.simibubi.create.content.equipment.armor.RemainingAirOverlay;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

public class CurrencyOverlay implements IIngameOverlay {
    public static final CurrencyOverlay INSTANCE = new CurrencyOverlay();
    @Override
    public void render(ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui || mc.gameMode.getPlayerMode() == GameType.SPECTATOR)
            return;
        
        LocalPlayer player = mc.player;
        
        if (player == null)
            return;
        int inventoryValue = CurrencyHandler.inventoryValue;
        if (inventoryValue <= 0)
            return;
        
        
        poseStack.pushPose();
        
        ItemStack coinStack = getDisplayedCoin();
        poseStack.translate((double) width / 2 + 120, height - 53, 0);
        Component value = Components.literal(String.valueOf(inventoryValue));
        GuiGameElement.of(coinStack)
                .at(0, 0)
                .render(poseStack);
        int color = 0xf7cb6c;
        mc.font.drawShadow(poseStack, value, 16, 5, color);
        poseStack.popPose();
    }
    
    public static ItemStack getDisplayedCoin() {
        return CurrencyJsonListener.instance.deserializeItem(new ResourceLocation("thermal", "gold_coin")).getDefaultInstance();
    }
}
