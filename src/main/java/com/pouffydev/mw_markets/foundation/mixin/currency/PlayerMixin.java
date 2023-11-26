package com.pouffydev.mw_markets.foundation.mixin.currency;

import com.pouffydev.mw_markets.compat.MarketsMods;
import com.pouffydev.mw_markets.compat.cofh.CurrencyHandlerCoFH;
import com.pouffydev.mw_markets.content.currency.CurrencyHandler;
import com.pouffydev.mw_markets.content.currency.CurrencyUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin {
    public PlayerMixin() {
    }
    
    @Unique
    private static final String TAG_CURRENCY = "Currency";
    @Unique
    private static final String TAG_INVENTORY_VALUE = "PlayerValue";
    @Inject(at = @At("TAIL"), method = "tick")
    private void tickMoney(CallbackInfo ci) {
        Player player = (Player) (Object) this;
        CompoundTag compoundTag = player.getPersistentData();
        int CoFHPlayerValue = 0;
        if (MarketsMods.COFH_CORE.isLoaded()) {
            CurrencyHandlerCoFH.updateInventoryValue(player);
            CoFHPlayerValue = CurrencyHandlerCoFH.coFHInventoryValue;
        }
        CurrencyHandler.updateInventoryValue(player);
        CurrencyUtils.playerValue = CurrencyHandler.inventoryValue + CoFHPlayerValue;
        
        if (!compoundTag.contains(TAG_CURRENCY)) {
            compoundTag.put(TAG_CURRENCY, new CompoundTag());
        }
        CompoundTag moneyTag = compoundTag.getCompound(TAG_CURRENCY);
        if (!moneyTag.contains(TAG_INVENTORY_VALUE)) {
            moneyTag.putInt(TAG_INVENTORY_VALUE, CurrencyUtils.playerValue);
        }
        
       
    }
}
