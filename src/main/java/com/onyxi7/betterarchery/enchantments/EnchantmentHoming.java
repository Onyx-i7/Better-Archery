package com.onyxi7.betterarchery.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;

public class EnchantmentHoming extends Enchantment {
    
    public EnchantmentHoming() {
        super(
            Rarity.RARE,
            EnumEnchantmentType.BOW,
            new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND}
        );
        setName("homing");
        setRegistryName("betterarchery:homing");
    }
    
    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 10 + (enchantmentLevel - 1) * 10;
    }
    
    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 15;
    }
    
    @Override
    public int getMaxLevel() {
        return 3; // 3 levels: I, II, III
    }
    
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return stack.getItem() instanceof ItemBow;
    }
    
    @Override
    public boolean isAllowedOnBooks() {
        return true;
    }
    
    @Override
    public boolean isTreasureEnchantment() {
        return false;
    }
}
