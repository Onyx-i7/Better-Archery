package com.onyxi7.betterarchery.items;

import com.onyxi7.betterarchery.betterarchery;
import com.onyxi7.betterarchery.init.ItemInit;
import com.onyxi7.betterarchery.util.interfaces.IHasModel;
import java.util.List;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemQuiverWithArrows extends ItemArrow implements IHasModel {
    
    public static int MAX_SIZE;
    
    public ItemQuiverWithArrows(String name, int size) {
        setTranslationKey(name);
        setRegistryName(name);
        MAX_SIZE = size;
        ItemInit.ITEMS.add(this);
    }
    
    @Override
    public void registerModels() {
        betterarchery.proxy.registerItemRenderer(this, 0, "inventory");
    }
    
    @Override
    public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity) {
        return armorType == EntityEquipmentSlot.CHEST || 
               armorType == EntityEquipmentSlot.LEGS || 
               armorType == EntityEquipmentSlot.MAINHAND || 
               armorType == EntityEquipmentSlot.OFFHAND;
    }
    
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int arrows = getArrowCount(stack);
        if (arrows >= MAX_SIZE) {
            tooltip.add("Arrows: " + arrows + " (Full)");
        } else {
            tooltip.add("Arrows: " + arrows);
        }
    }
    
    // Método para obtener el conteo de flechas
    public static int getArrowCount(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Arrows")) {
            return stack.getTagCompound().getInteger("Arrows");
        }
        return 0;
    }
    
    // Método para establecer el conteo de flechas
    public static void setArrowCount(ItemStack stack, int count) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        stack.getTagCompound().setInteger("Arrows", count);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (handIn == EnumHand.OFF_HAND) {
            return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
        }
        
        ItemStack heldStack = playerIn.getHeldItem(handIn);
        int currentArrows = getArrowCount(heldStack);
        
        // Si está agachado: sacar flechas del carcaj
        if (playerIn.isSneaking()) {
            int arrowsToGive = Math.min(currentArrows, 64);
            if (arrowsToGive > 0) {
                setArrowCount(heldStack, currentArrows - arrowsToGive);
                playerIn.inventory.addItemStackToInventory(new ItemStack(Items.ARROW, arrowsToGive));
            }
            return new ActionResult<>(EnumActionResult.SUCCESS, heldStack);
        }
        
        // 1. Recoger flechas del suelo
        BlockPos pos = playerIn.getPosition();
        BlockPos range1 = pos.add(-1, -1, -1);
        BlockPos range2 = pos.add(1, 3, 1);
        List<EntityArrow> arrows = worldIn.getEntitiesWithinAABB(EntityArrow.class, new AxisAlignedBB(range1, range2));
        
        if (!arrows.isEmpty()) {
            int pickedUp = 0;
            for (EntityArrow a : arrows) {
                if (currentArrows + pickedUp < MAX_SIZE) {
                    worldIn.removeEntity(a);
                    pickedUp++;
                }
            }
            if (pickedUp > 0) {
                setArrowCount(heldStack, currentArrows + pickedUp);
                return new ActionResult<>(EnumActionResult.SUCCESS, heldStack);
            }
        }
        
        // 2. Buscar CUALQUIER flecha en el inventario
        for (int i = 0; i < playerIn.inventory.getSizeInventory(); i++) {
            ItemStack slotStack = playerIn.inventory.getStackInSlot(i);
            if (!slotStack.isEmpty() && slotStack.getItem() instanceof ItemArrow) {
                int arrowCount = slotStack.getCount();
                int spaceLeft = MAX_SIZE - currentArrows;
                
                if (spaceLeft <= 0) {
                    return new ActionResult<>(EnumActionResult.SUCCESS, heldStack);
                }
                
                if (arrowCount <= spaceLeft) {
                    // Cabe todo el stack
                    setArrowCount(heldStack, currentArrows + arrowCount);
                    playerIn.inventory.removeStackFromSlot(i);
                } else {
                    // Solo cabe parte del stack
                    setArrowCount(heldStack, MAX_SIZE);
                    slotStack.shrink(spaceLeft);
                }
                return new ActionResult<>(EnumActionResult.SUCCESS, heldStack);
            }
        }
        
        return new ActionResult<>(EnumActionResult.PASS, heldStack);
    }
    
    @Override
    public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
        EntityTippedArrow entitytippedarrow = new EntityTippedArrow(worldIn, shooter);
        return entitytippedarrow;
    }
    
    @Override
    public boolean isInfinite(ItemStack stack, ItemStack bow, EntityPlayer player) {
        // Verificar que el carcaj tiene flechas
        int arrows = getArrowCount(stack);
        if (arrows <= 0) {
            return false;
        }
        
        int enchant = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, bow);
        if (enchant > 0) {
            return true; // Infinity: no consumir
        }
        
        // Consumir una flecha
        setArrowCount(stack, arrows - 1);
        return true;
    }
    
    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }
    
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        // Solo en servidor
        if (worldIn.isRemote) {
            return;
        }
        
        if (!(entityIn instanceof EntityPlayer)) {
            return;
        }
        
        int arrows = getArrowCount(stack);
        
        // Si se quedó sin flechas, convertir a carcaj vacío
        if (arrows <= 0) {
            EntityPlayer player = (EntityPlayer) entityIn;
            ItemStack emptyQuiver = new ItemStack(ItemInit.QUIVER);
            
            for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                ItemStack slotStack = player.inventory.getStackInSlot(i);
                if (!slotStack.isEmpty() && slotStack.getItem() == this) {
                    int slotArrows = getArrowCount(slotStack);
                    if (slotArrows <= 0) {
                        player.inventory.setInventorySlotContents(i, emptyQuiver);
                        return;
                    }
                }
            }
        }
    }
}
