package com.pouffydev.mw_markets.foundation.mixin.currency;

import com.pouffydev.mw_markets.content.currency.CurrencyHandler;
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
    private static final String TAG_MONEY = "Money";
    @Unique
    private static final String TAG_INVENTORY_VALUE = "InventoryValue";
    @Inject(at = @At("TAIL"), method = "tick")
    private void tickMoney(CallbackInfo ci) {
        Player player = (Player) (Object) this;
        CompoundTag compoundTag = player.getPersistentData();
        
        int inventoryValue = CurrencyHandler.inventoryValue;
        
        if (!compoundTag.contains(TAG_MONEY)) {
            compoundTag.put(TAG_MONEY, new CompoundTag());
        }
        CompoundTag moneyTag = compoundTag.getCompound(TAG_MONEY);
        if (!moneyTag.contains(TAG_INVENTORY_VALUE)) {
            moneyTag.putInt(TAG_INVENTORY_VALUE, inventoryValue);
        }
        
        CurrencyHandler.updateInventoryValue(player);
    }
}
