package com.onyxi7.betterarchery.compat;

import com.onyxi7.betterarchery.init.ItemInit;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class BackToolsCompat {
    
    private static final String BACKTOOLS_MODID = "backtools";
    
    public static void init() {
        if (Loader.isModLoaded(BACKTOOLS_MODID)) {
            registerWithBackTools();
        }
    }
    
    @Optional.Method(modid = BACKTOOLS_MODID)
    private static void registerWithBackTools() {
        try {
            // Register custom bows with BackTools
            // Orientation: 1 = bow orientation (vertical)
            registerBowWithBackTools(new ItemStack(ItemInit.LONG_BOW));
            registerBowWithBackTools(new ItemStack(ItemInit.RECURVE_BOW));
            registerBowWithBackTools(new ItemStack(ItemInit.COMPOSITE_BOW));
            registerBowWithBackTools(new ItemStack(ItemInit.YUMI_BOW));
            
            System.out.println("[BetterArchery] BackTools integration enabled - Bows will render on back");
        } catch (Exception e) {
            System.err.println("[BetterArchery] Failed to integrate with BackTools: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Optional.Method(modid = BACKTOOLS_MODID)
    private static void registerBowWithBackTools(ItemStack stack) {
        // Send IMC message to BackTools
        // Key: "backtool"
        // Value: ItemStack with count = orientation (1 for bow)
        ItemStack imcStack = stack.copy();
        imcStack.setCount(1); // Orientation 1 = bow orientation
        FMLInterModComms.sendRuntimeMessage(
            "betterarchery",
            BACKTOOLS_MODID,
            "backtool",
            imcStack
        );
    }
}
