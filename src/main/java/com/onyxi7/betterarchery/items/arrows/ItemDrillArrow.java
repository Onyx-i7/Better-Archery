package com.onyxi7.betterarchery.items.arrows;

import com.onyxi7.betterarchery.betterarchery;
import com.onyxi7.betterarchery.entities.EntityDrillArrow;
import com.onyxi7.betterarchery.init.ItemInit;
import com.onyxi7.betterarchery.init.CreativeTabInit;
import com.onyxi7.betterarchery.util.interfaces.IHasModel;
import java.util.List;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ItemDrillArrow extends ItemArrow implements IHasModel {
    
    // Bit 4 (value 16) indicates whether it is broken
    private static final int BROKEN_BIT = 4;
    
    public ItemDrillArrow(String name) {
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(CreativeTabInit.BETTER_ARCHERY_TAB);
        setHasSubtypes(true);
        setMaxDamage(0);
        ItemInit.ITEMS.add(this);
    }
    
    // Check whether the arrow is broken using bit 4 of the metadata
    public static boolean isBroken(int damage) {
        return (damage & (1 << BROKEN_BIT)) != 0;
    }
    
    // Sets the "broken" status in the metadata
    public static int setBroken(int damage, boolean broken) {
        if (broken) {
            return damage | (1 << BROKEN_BIT);
        } else {
            return damage & ~(1 << BROKEN_BIT);
        }
    }
    
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            // Intact arrow
            items.add(new ItemStack(this, 1, 0));
            // Broken Arrow
            items.add(new ItemStack(this, 1, setBroken(0, true)));
        }
    }
    
    @Override
    public String getTranslationKey(ItemStack stack) {
        if (isBroken(stack.getMetadata())) {
            return "item.broken_drill_arrow";
        }
        return "item.drill_arrow";
    }
    
    @Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (isBroken(stack.getMetadata())) {
			tooltip.add(net.minecraft.util.text.translation.I18n.translateToLocal("tooltip.drill_arrow.broken"));
		} else {
			tooltip.add(net.minecraft.util.text.translation.I18n.translateToLocal("tooltip.drill_arrow.desc"));
		}
	}
    
    @Override
	public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
		EntityDrillArrow arrow = new EntityDrillArrow(worldIn, shooter);
    
		// If it's broken, it shouldn't be able to fire
		if (isBroken(stack.getMetadata())) {
			arrow.setDamage(0);
		}
    
		return arrow;
	}
    
    @Override
    public boolean isInfinite(ItemStack stack, ItemStack bow, EntityPlayer player) {
        // Drill arrows are NOT infinite, even with the Infinity enchantment
        return false;
    }
    
    @Override
    public void registerModels() {
        // Normal model
        betterarchery.proxy.registerItemRenderer(this, 0, "inventory");
        // Broken model
        betterarchery.proxy.registerItemRenderer(this, setBroken(0, true), "broken");
    }
}
