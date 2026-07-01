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
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
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
		if (isBroken(stack.getMetadata())) {
			tooltip.add(net.minecraft.util.text.translation.I18n.translateToLocal("tooltip.drill_arrow.broken"));
		} else {
			tooltip.add(net.minecraft.util.text.translation.I18n.translateToLocal("tooltip.drill_arrow.desc"));
		}
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
        return armorType == EntityEquipmentSlot.CHEST || 
               armorType == EntityEquipmentSlot.LEGS || 
               armorType == EntityEquipmentSlot.MAINHAND || 
               armorType == EntityEquipmentSlot.OFFHAND;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (handIn == EnumHand.OFF_HAND) {
            return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
        }
        
        ItemStack heldStack = playerIn.getHeldItem(handIn);
        
        BlockPos pos = playerIn.getPosition();
        BlockPos range1 = pos.add(-1, -1, -1);
        BlockPos range2 = pos.add(1, 3, 1);
        List<EntityArrow> arrows = worldIn.getEntitiesWithinAABB(EntityArrow.class, new AxisAlignedBB(range1, range2));
        
        if (!arrows.isEmpty()) {
            int count = 0;
            for (EntityArrow a : arrows) {
                if (count < ItemQuiverWithArrows.MAX_SIZE) {
                    worldIn.removeEntity(a);
                    count++;
                }
            }
            
            if (count > 0) {
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setInteger("Arrows", count);
                ItemStack quiverWithArrows = new ItemStack(ItemInit.QUIVER_WITH_ARROWS);
                quiverWithArrows.setTagCompound(nbt);
                return new ActionResult<>(EnumActionResult.SUCCESS, quiverWithArrows);
            }
        }
        
        for (int i = 0; i < playerIn.inventory.getSizeInventory(); i++) {
            ItemStack slotStack = playerIn.inventory.getStackInSlot(i);
            if (!slotStack.isEmpty() && slotStack.getItem() instanceof ItemArrow) {
                int arrowCount = slotStack.getCount();
                
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setInteger("Arrows", arrowCount);
                ItemStack quiverWithArrows = new ItemStack(ItemInit.QUIVER_WITH_ARROWS);
                quiverWithArrows.setTagCompound(nbt);
                
                playerIn.inventory.removeStackFromSlot(i);
                return new ActionResult<>(EnumActionResult.SUCCESS, quiverWithArrows);
            }
        }
        
        return new ActionResult<>(EnumActionResult.SUCCESS, heldStack);
    }
}
