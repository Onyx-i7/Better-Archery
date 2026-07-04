package com.onyxi7.betterarchery.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import com.onyxi7.betterarchery.init.ItemInit;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class JEICompat implements IModPlugin {
    
    @Override
    public void register(IModRegistry registry) {
        registry.addIngredientInfo(new ItemStack(ItemInit.QUIVER), ItemStack.class, 
            "betterarchery.jei.quiver.description");
        
        registry.addIngredientInfo(new ItemStack(ItemInit.QUIVER_WITH_ARROWS), ItemStack.class, 
            "betterarchery.jei.quiver_with_arrows.description");
        
        registry.addIngredientInfo(new ItemStack(ItemInit.DRILL_ARROW), ItemStack.class, 
            "betterarchery.jei.drill_arrow.description");
        
        registry.addIngredientInfo(new ItemStack(ItemInit.FIRE_ARROW), ItemStack.class, 
            "betterarchery.jei.fire_arrow.description");
        
        registry.addIngredientInfo(new ItemStack(ItemInit.TORCH_ARROW), ItemStack.class, 
            "betterarchery.jei.torch_arrow.description");
        
        registry.addIngredientInfo(new ItemStack(ItemInit.IMPACT_ARROW), ItemStack.class, 
            "betterarchery.jei.impact_arrow.description");
        
        registry.addIngredientInfo(new ItemStack(ItemInit.ENDER_ARROW), ItemStack.class, 
            "betterarchery.jei.ender_arrow.description");
        
        registry.addIngredientInfo(new ItemStack(ItemInit.SPLITTING_ARROW), ItemStack.class, 
            "betterarchery.jei.splitting_arrow.description");
    }
}
