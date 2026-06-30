package com.onyxi7.betterarchery.items.arrows;

import com.onyxi7.betterarchery.betterarchery;
import com.onyxi7.betterarchery.init.ItemInit;
import com.onyxi7.betterarchery.util.interfaces.IHasModel;
import com.onyxi7.betterarchery.entities.EntityDrillArrow;
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
    
    // Bit 4 (valor 16) indica si está rota
    private static final int BROKEN_BIT = 4;
    
    public ItemDrillArrow(String name) {
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(CreativeTabs.COMBAT);
        setHasSubtypes(true);
        setMaxDamage(0);
        ItemInit.ITEMS.add(this);
    }
    
    // Verifica si la flecha está rota usando el bit 4 del metadata
    public static boolean isBroken(int damage) {
        return (damage & (1 << BROKEN_BIT)) != 0;
    }
    
    // Establece el estado de "roto" en el metadata
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
            // Flecha intacta
            items.add(new ItemStack(this, 1, 0));
            // Flecha rota
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
            tooltip.add("§7Rota - No puede usarse");
        } else {
            tooltip.add("§bAtraviesa bloques y enemigos");
        }
    }
    
    @Override
	public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
		EntityDrillArrow arrow = new EntityDrillArrow(worldIn, shooter);
    
		// Si está rota, no debería poder dispararse
		if (isBroken(stack.getMetadata())) {
			arrow.setDamage(0);
		}
    
		return arrow;
	}
    
    @Override
    public boolean isInfinite(ItemStack stack, ItemStack bow, EntityPlayer player) {
        // Las flechas drill NO son infinitas, incluso con encantamiento Infinity
        return false;
    }
    
    @Override
    public void registerModels() {
        // Modelo normal (intacta)
        betterarchery.proxy.registerItemRenderer(this, 0, "inventory");
        // Modelo roto
        betterarchery.proxy.registerItemRenderer(this, setBroken(0, true), "broken");
    }
}
