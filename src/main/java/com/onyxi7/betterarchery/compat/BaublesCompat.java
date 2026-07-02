package com.onyxi7.betterarchery.compat;

import baubles.api.BaublesApi;
import com.onyxi7.betterarchery.init.ItemInit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;

public class BaublesCompat {
    
    private static final String BAUBLES_MODID = "baubles";
    private static final String BUBBLES_MODID = "bubbles";
    
    public static boolean isBaublesLoaded() {
        return Loader.isModLoaded(BAUBLES_MODID) || Loader.isModLoaded(BUBBLES_MODID);
    }
    
    @Optional.Method(modid = "baubles")
    public static ItemStack getQuiverFromBaubles(EntityPlayer player) {
        if (!isBaublesLoaded()) {
            return ItemStack.EMPTY;
        }
        
        try {
            // Check if quiver is equipped in baubles slots
            int slot = BaublesApi.isBaubleEquipped(player, ItemInit.QUIVER);
            if (slot != -1) {
                return BaublesApi.getBaublesHandler(player).getStackInSlot(slot);
            }
            
            // Check if quiver with arrows is equipped
            slot = BaublesApi.isBaubleEquipped(player, ItemInit.QUIVER_WITH_ARROWS);
            if (slot != -1) {
                return BaublesApi.getBaublesHandler(player).getStackInSlot(slot);
            }
        } catch (Exception e) {
            System.err.println("[BetterArchery] Failed to get quiver from Baubles: " + e.getMessage());
        }
        
        return ItemStack.EMPTY;
    }
}
