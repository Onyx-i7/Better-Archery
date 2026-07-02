package com.onyxi7.betterarchery.compat;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
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
            IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
            
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack stack = handler.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    Item item = stack.getItem();
                    if (item == ItemInit.QUIVER || item == ItemInit.QUIVER_WITH_ARROWS) {
                        return stack;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[BetterArchery] Failed to get quiver from Baubles: " + e.getMessage());
        }
        
        return ItemStack.EMPTY;
    }
    
    @Optional.Method(modid = "baubles")
    public static int findQuiverSlot(EntityPlayer player) {
        if (!isBaublesLoaded()) {
            return -1;
        }
        
        try {
            IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
            
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack stack = handler.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    Item item = stack.getItem();
                    if (item == ItemInit.QUIVER || item == ItemInit.QUIVER_WITH_ARROWS) {
                        return i;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[BetterArchery] Failed to find quiver slot: " + e.getMessage());
        }
        
        return -1;
    }
    
    @Optional.Method(modid = "baubles")
    public static void setBaubleSlot(EntityPlayer player, int slot, ItemStack stack) {
        if (!isBaublesLoaded()) {
            return;
        }
        
        try {
            IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
            handler.setStackInSlot(slot, stack);
        } catch (Exception e) {
            System.err.println("[BetterArchery] Failed to set bauble slot: " + e.getMessage());
        }
    }
}
