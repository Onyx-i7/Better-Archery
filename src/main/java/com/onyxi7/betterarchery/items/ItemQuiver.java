package com.onyxi7.betterarchery.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.onyxi7.betterarchery.betterarchery;
import com.onyxi7.betterarchery.init.CreativeTabInit;
import com.onyxi7.betterarchery.init.ItemInit;
import com.onyxi7.betterarchery.util.interfaces.IHasModel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles")
public class ItemQuiver extends Item implements IHasModel, IBauble {
    
    public static final int MAX_ARROWS = 128;
    
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
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        
        if (!worldIn.isRemote) {
            // Find first arrow type in inventory
            String firstArrowType = findFirstArrowType(playerIn);
            
            // Collect arrows of that type
            int collected = collectArrowsOfType(playerIn, firstArrowType);
            
            if (collected > 0) {
                ItemStack newQuiver = new ItemStack(ItemInit.QUIVER_WITH_ARROWS);
                ItemQuiverWithArrows.setArrowCount(newQuiver, collected);
                ItemQuiverWithArrows.setArrowType(newQuiver, firstArrowType);
                
                playerIn.setHeldItem(handIn, newQuiver);
                playerIn.sendStatusMessage(new TextComponentTranslation(
                    "message.betterarchery.quiver.collected", collected), true);
            }
        }
        
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }
    
    private String findFirstArrowType(EntityPlayer player) {
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack slotStack = player.inventory.getStackInSlot(i);
            if (isArrow(slotStack)) {
                return slotStack.getItem().getRegistryName().toString();
            }
        }
        return "minecraft:arrow";
    }
    
    private int collectArrowsOfType(EntityPlayer player, String arrowType) {
        int totalCollected = 0;
        
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack slotStack = player.inventory.getStackInSlot(i);
            
            if (isArrow(slotStack) && slotStack.getItem().getRegistryName().toString().equals(arrowType)) {
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
    
    private boolean isArrow(ItemStack stack) {
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
    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {}
    
    @Override
    @Optional.Method(modid = "baubles")
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {}
    
    @Override
    @Optional.Method(modid = "baubles")
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {}
    
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
