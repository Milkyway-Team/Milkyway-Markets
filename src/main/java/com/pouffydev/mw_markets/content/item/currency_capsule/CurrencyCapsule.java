package com.pouffydev.mw_markets.content.item.currency_capsule;

import com.pouffydev.krystal_core.foundation.data.lang.KrystalCoreLang;
import com.pouffydev.mw_markets.MWMarkets;
import com.pouffydev.mw_markets.content.currency.CurrencyUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

import static com.pouffydev.mw_markets.MWMarkets.ID;

public class CurrencyCapsule extends Item {
    public static int max = 1000000;
    public CurrencyCapsule(Properties p) {
        super(p);
    }
    @Override
    public void fillItemCategory(@NotNull CreativeModeTab group, @NotNull NonNullList<ItemStack> items) {
        if (group == MWMarkets.itemGroup) {
            ItemStack stack = new ItemStack(this);
            CompoundTag nbt = stack.getOrCreateTag();
            nbt.putInt("RequestedValue", 0);
            items.add(stack);
        }
    }
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        CompoundTag nbt = stack.getOrCreateTag();
        int requestedValue = nbt.getInt("RequestedValue");
        if (player.isShiftKeyDown()) {
            CurrencyUtils.spawnItemEntitiesWithValue(level, player, requestedValue);
        }
        return super.use(level, player, hand);
    }
    
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        CompoundTag tag = stack.getTag();
        if (tag != null) {
            int percentage = tag.getInt("RequestedValue") * 100 / max;
            
            tooltip.add(KrystalCoreLang.translateDirect(ID, "capsule.value.capacity", percentage + "%").withStyle(Style.EMPTY.withColor(0xb57849)));
            tooltip.add(KrystalCoreLang.translateDirect(ID, "capsule.value", tag.getInt("RequestedValue")).withStyle(Style.EMPTY.withColor(0xf7cb6c)));
        }
    }
}
