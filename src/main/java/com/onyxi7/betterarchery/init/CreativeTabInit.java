package com.onyxi7.betterarchery.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabInit {
    
    public static final CreativeTabs BETTER_ARCHERY_TAB = new CreativeTabs("betterarchery_tab") {
        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(ItemInit.QUIVER_WITH_ARROWS);
        }
    };
}
