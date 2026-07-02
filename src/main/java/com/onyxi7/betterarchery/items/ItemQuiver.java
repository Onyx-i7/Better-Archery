package com.onyxi7.betterarchery.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.onyxi7.betterarchery.betterarchery;
import com.onyxi7.betterarchery.init.CreativeTabInit;
import com.onyxi7.betterarchery.init.ItemInit;
import com.onyxi7.betterarchery.util.interfaces.IHasModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles")
public class ItemQuiver extends Item implements IHasModel, IBauble {
    
    public static final int MAX_ARROWS = 64;
    
    public ItemQuiver(String name) {
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(CreativeTabInit.BETTER_ARCHERY_TAB);
        setMaxStackSize(1);
        ItemInit.ITEMS.add(this);
    }
    
    @Override
    public void registerModels() {
        betterarchery.proxy.registerItemRenderer(this, 0, "inventory");
    }
    
    @Override
    public boolean isFull3D() {
        return true;
    }
        
    @Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, net.minecraft.util.EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		
		if (!worldIn.isRemote) {
			int collected = collectArrowsFromInventory(playerIn, stack);
			
			if (collected > 0) {
				ItemStack newQuiver = new ItemStack(ItemInit.QUIVER_WITH_ARROWS);
				ItemQuiverWithArrows.setArrowCount(newQuiver, collected);
				
				if (stack.hasTagCompound()) {
					newQuiver.setTagCompound(stack.getTagCompound());
				}
				
				playerIn.setHeldItem(handIn, newQuiver);
				playerIn.sendStatusMessage(new net.minecraft.util.text.TextComponentTranslation(
					"message.betterarchery.quiver.collected", collected), true);
			}
		}
		
		return ActionResult.newResult(net.minecraft.util.EnumActionResult.SUCCESS, stack);
	}
        
    public int collectArrowsFromInventory(EntityPlayer player, ItemStack quiverStack) {
        int totalCollected = 0;
        
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack slotStack = player.inventory.getStackInSlot(i);
            
            if (isArrow(slotStack)) {
                int spaceLeft = MAX_ARROWS - totalCollected;
                int toCollect = Math.min(spaceLeft, slotStack.getCount());
                
                if (toCollect > 0) {
                    totalCollected += toCollect;
                    slotStack.shrink(toCollect);
                    
                    if (slotStack.isEmpty()) {
                        player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                    }
                    
                    if (totalCollected >= MAX_ARROWS) {
                        break;
                    }
                }
            }
        }
        
        return totalCollected;
    }
    
    public boolean isArrow(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return stack.getItem() instanceof ItemArrow || stack.getItem() == Items.ARROW;
    }
    
    // === BAUBLES IMPLEMENTATION ===
    
    @Override
    @Optional.Method(modid = "baubles")
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.BODY;
    }
    
    @Override
    @Optional.Method(modid = "baubles")
    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
    }
    
    @Override
    @Optional.Method(modid = "baubles")
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
    }
    
    @Override
    @Optional.Method(modid = "baubles")
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
    }
    
    @Override
    @Optional.Method(modid = "baubles")
    public boolean canEquip(ItemStack itemstack, EntityLivingBase entity) {
        return true;
    }
    
    @Override
    @Optional.Method(modid = "baubles")
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }
    
    @Override
    @Optional.Method(modid = "baubles")
    public boolean willAutoSync(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }
}
