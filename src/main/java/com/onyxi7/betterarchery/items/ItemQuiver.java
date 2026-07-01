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
        if (handIn != EnumHand.OFF_HAND) {
            BlockPos pos = playerIn.getPosition();
            BlockPos range1 = pos.add(-1, -1, -1);
            BlockPos range2 = pos.add(1, 3, 1);
            List<EntityArrow> arrows = worldIn.getEntitiesWithinAABB(EntityArrow.class, new AxisAlignedBB(range1, range2));
            
            if (!arrows.isEmpty()) {
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setInteger("Arrows", 0);
                ItemStack quiverWithArrowsStack = new ItemStack(ItemInit.QUIVER_WITH_ARROWS);
                
                for (EntityArrow a : arrows) {
                    if (nbt.getInteger("Arrows") < ItemQuiverWithArrows.MAX_SIZE) {
                        worldIn.removeEntity(a);
                        nbt.setInteger("Arrows", nbt.getInteger("Arrows") + 1);
                        continue;
                    }
                    quiverWithArrowsStack.setTagCompound(nbt);
                    return new ActionResult<>(EnumActionResult.SUCCESS, quiverWithArrowsStack);
                }
                quiverWithArrowsStack.setTagCompound(nbt);
                return new ActionResult<>(EnumActionResult.SUCCESS, quiverWithArrowsStack);
            }
            
            // Verificar si hay flechas en el inventario
            int arrowsSlot = playerIn.inventory.getSlotFor(new ItemStack(Items.ARROW));
            
            // Verificar que el slot sea válido (no -1)
            if (arrowsSlot >= 0) {
                NBTTagCompound nbt = new NBTTagCompound();
                ItemStack arrowStack = playerIn.inventory.getStackInSlot(arrowsSlot);
                int arrowStackSize = arrowStack.getCount();
                nbt.setInteger("Arrows", arrowStackSize);
                
                ItemStack quiverWithArrowsStack = new ItemStack(ItemInit.QUIVER_WITH_ARROWS);
                quiverWithArrowsStack.setTagCompound(nbt);
                playerIn.inventory.removeStackFromSlot(arrowsSlot);
                return new ActionResult<>(EnumActionResult.SUCCESS, quiverWithArrowsStack);
            }
            
            return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
