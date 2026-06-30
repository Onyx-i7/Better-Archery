package com.onyxi7.betterarchery.items;

import com.onyxi7.betterarchery.betterarchery;
import com.onyxi7.betterarchery.init.ItemInit;
import com.onyxi7.betterarchery.util.interfaces.IHasModel;
import java.util.List;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemQuiver extends Item implements IHasModel {
    
    public ItemQuiver(String name) {
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(CreativeTabs.COMBAT);
        ItemInit.ITEMS.add(this);
    }
    
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add("Arrows: 0");
    }
    
    @Override
    public void registerModels() {
        betterarchery.proxy.registerItemRenderer(this, 0, "inventory");
    }
    
    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }
    
    @Override
    public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity) {
        if (armorType == EntityEquipmentSlot.CHEST || 
            armorType == EntityEquipmentSlot.LEGS || 
            armorType == EntityEquipmentSlot.MAINHAND || 
            armorType == EntityEquipmentSlot.OFFHAND) {
            return true;
        }
        return false;
    }
    
    @Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (handIn == EnumHand.MAIN_HAND) {
			ItemStack stack = playerIn.getHeldItem(handIn);
        
			if (!stack.hasTagCompound()) {
				stack.setTagCompound(new NBTTagCompound());
			}
			if (!stack.getTagCompound().hasKey("uniqueID")) {
				stack.getTagCompound().setInteger("uniqueID", stack.hashCode());
			}
        
			if (!worldIn.isRemote) {
				playerIn.openGui(com.onyxi7.betterarchery.betterarchery.instance, 
							com.onyxi7.betterarchery.handler.GuiHandler.QUIVER_GUI, 
							worldIn, 0, 0, 0);
			}
			return new ActionResult<>(EnumActionResult.SUCCESS, stack);
		}
    return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
}
