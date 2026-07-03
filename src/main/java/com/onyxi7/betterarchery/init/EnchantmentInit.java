package com.onyxi7.betterarchery.init;

import com.onyxi7.betterarchery.enchantments.EnchantmentHoming;
import net.minecraft.enchantment.Enchantment;

import java.util.ArrayList;
import java.util.List;

public class EnchantmentInit {
    
    public static final List<Enchantment> ENCHANTMENTS = new ArrayList<>();
    
    public static final Enchantment HOMING = register(new EnchantmentHoming());
    
    private static Enchantment register(Enchantment enchantment) {
        ENCHANTMENTS.add(enchantment);
        return enchantment;
    }
}
