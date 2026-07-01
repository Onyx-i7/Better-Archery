package com.onyxi7.betterarchery.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import com.onyxi7.betterarchery.init.ItemInit;
import com.onyxi7.betterarchery.items.arrows.ItemPotionArrow;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class JEICompat implements IModPlugin {
    
    @Override
    public void register(IModRegistry registry) {
        ISubtypeRegistry subtypeRegistry = registry.getJeiHelpers().getSubtypeRegistry();
        subtypeRegistry.registerSubtypeInterpreter(ItemInit.POTION_ARROW, stack -> {
            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("PotionType")) {
                return stack.getTagCompound().getString("PotionType");
            }
            return "empty";
        });
        
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
        
        registry.addIngredientInfo(new ItemStack(ItemInit.POTION_ARROW), ItemStack.class, 
            "betterarchery.jei.potion_arrow.description");
    }
}
