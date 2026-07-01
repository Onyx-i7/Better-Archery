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
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

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
		Item arrowType = getArrowType(stack);
		String arrowName = arrowType != null ? arrowType.getItemStackDisplayName(new ItemStack(arrowType)) : "Arrow";
		
		if (arrows >= MAX_SIZE) {
			tooltip.add(net.minecraft.util.text.translation.I18n.translateToLocalFormatted("tooltip.quiver.arrows.full", arrows));
		} else {
			tooltip.add(net.minecraft.util.text.translation.I18n.translateToLocalFormatted("tooltip.quiver.arrows", arrows));
		}
	}
    
    // Obtener cantidad de flechas
    public static int getArrowCount(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Arrows")) {
            return stack.getTagCompound().getInteger("Arrows");
        }
        return 0;
    }
    
    // Establecer cantidad de flechas
    public static void setArrowCount(ItemStack stack, int count) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        stack.getTagCompound().setInteger("Arrows", count);
    }
    
    // Obtener tipo de flecha guardada
    public static Item getArrowType(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("ArrowType")) {
            String typeName = stack.getTagCompound().getString("ArrowType");
            ResourceLocation loc = new ResourceLocation(typeName);
            if (ForgeRegistries.ITEMS.containsKey(loc)) {
                return ForgeRegistries.ITEMS.getValue(loc);
            }
        }
        return Items.ARROW; // Por defecto, flecha normal
    }
    
    // Guardar tipo de flecha
    public static void setArrowType(ItemStack stack, Item arrowItem) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        ResourceLocation regName = arrowItem.getRegistryName();
        if (regName != null) {
            stack.getTagCompound().setString("ArrowType", regName.toString());
        }
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (handIn == EnumHand.OFF_HAND) {
            return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
        }
        
        ItemStack heldStack = playerIn.getHeldItem(handIn);
        int currentArrows = getArrowCount(heldStack);
        Item currentType = getArrowType(heldStack);
        
        // Si está agachado: sacar flechas del carcaj
        if (playerIn.isSneaking()) {
            int arrowsToGive = Math.min(currentArrows, 64);
            if (arrowsToGive > 0) {
                setArrowCount(heldStack, currentArrows - arrowsToGive);
                ItemStack arrowStack = new ItemStack(currentType, arrowsToGive);
                playerIn.inventory.addItemStackToInventory(arrowStack);
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
                // Las flechas del suelo siempre son flechas normales
                if (currentArrows == 0) {
                    setArrowType(heldStack, Items.ARROW);
                }
                return new ActionResult<>(EnumActionResult.SUCCESS, heldStack);
            }
        }
        
        // 2. Buscar flechas en el inventario
        for (int i = 0; i < playerIn.inventory.getSizeInventory(); i++) {
            ItemStack slotStack = playerIn.inventory.getStackInSlot(i);
            if (!slotStack.isEmpty() && slotStack.getItem() instanceof ItemArrow) {
                Item slotArrowType = slotStack.getItem();
                
                // Verificar si el tipo coincide o el carcaj está vacío
                boolean typeMatch = (currentArrows == 0) || (slotArrowType == currentType);
                
                if (typeMatch) {
                    int arrowCount = slotStack.getCount();
                    int spaceLeft = MAX_SIZE - currentArrows;
                    
                    if (spaceLeft <= 0) {
                        return new ActionResult<>(EnumActionResult.SUCCESS, heldStack);
                    }
                    
                    if (arrowCount <= spaceLeft) {
                        // Cabe todo el stack
                        setArrowCount(heldStack, currentArrows + arrowCount);
                        if (currentArrows == 0) {
                            setArrowType(heldStack, slotArrowType);
                        }
                        playerIn.inventory.removeStackFromSlot(i);
                    } else {
                        // Solo cabe parte del stack
                        setArrowCount(heldStack, MAX_SIZE);
                        if (currentArrows == 0) {
                            setArrowType(heldStack, slotArrowType);
                        }
                        slotStack.shrink(spaceLeft);
                    }
                    return new ActionResult<>(EnumActionResult.SUCCESS, heldStack);
                }
            }
        }
        
        // No hay nada que hacer, devolver el mismo stack
        return new ActionResult<>(EnumActionResult.SUCCESS, heldStack);
    }
    
    @Override
    public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
        EntityTippedArrow entitytippedarrow = new EntityTippedArrow(worldIn, shooter);
        return entitytippedarrow;
    }
    
    @Override
    public boolean isInfinite(ItemStack stack, ItemStack bow, EntityPlayer player) {
        int arrows = getArrowCount(stack);
        if (arrows <= 0) {
            return false;
        }
        
        int enchant = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, bow);
        if (enchant > 0) {
            return true;
        }
        
        setArrowCount(stack, arrows - 1);
        return true;
    }
    
    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }
    
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (worldIn.isRemote) {
            return;
        }
        
        if (!(entityIn instanceof EntityPlayer)) {
            return;
        }
        
        int arrows = getArrowCount(stack);
        
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
