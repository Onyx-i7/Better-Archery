package com.onyxi7.betterarchery.items.arrows;

import com.onyxi7.betterarchery.betterarchery;
import com.onyxi7.betterarchery.entities.EntitySplittingArrow;
import com.onyxi7.betterarchery.init.ItemInit;
import com.onyxi7.betterarchery.util.interfaces.IHasModel;
import java.util.List;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSplittingArrow extends ItemArrow implements IHasModel {
    
    public ItemSplittingArrow(String name) {
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(CreativeTabInit.BETTER_ARCHERY_TAB);
        ItemInit.ITEMS.add(this);
    }
    
    @Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(net.minecraft.util.text.translation.I18n.translateToLocal("tooltip.splitting_arrow.desc"));
	}
    
    @Override
    public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
        return new EntitySplittingArrow(worldIn, shooter);
    }
    
    @Override
    public boolean isInfinite(ItemStack stack, ItemStack bow, EntityPlayer player) {
        return false;
    }
    
    @Override
    public void registerModels() {
        betterarchery.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
