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
        if (armorType == EntityEquipmentSlot.CHEST || 
            armorType == EntityEquipmentSlot.LEGS || 
            armorType == EntityEquipmentSlot.MAINHAND || 
            armorType == EntityEquipmentSlot.OFFHAND) {
            return true;
        }
        return false;
    }
    
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Arrows")) {
            if (stack.getTagCompound().getInteger("Arrows") < MAX_SIZE) {
                tooltip.add("Arrows: " + stack.getTagCompound().getInteger("Arrows"));
            } else {
                tooltip.add("Arrows: " + stack.getTagCompound().getInteger("Arrows") + " (Full)");
            }
        }
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (handIn != EnumHand.OFF_HAND) {
            if (!playerIn.isSneaking()) {
                BlockPos pos = playerIn.getPosition();
                BlockPos range1 = pos.add(-1, -1, -1);
                BlockPos range2 = pos.add(1, 3, 1);
                List<EntityArrow> arrows = worldIn.getEntitiesWithinAABB(EntityArrow.class, new AxisAlignedBB(range1, range2));
                
                if (!arrows.isEmpty()) {
                    ItemStack itemStack = playerIn.getHeldItem(handIn);
                    NBTTagCompound nbt = itemStack.getTagCompound();
                    if (nbt == null) {
                        nbt = new NBTTagCompound();
                    }
                    
                    for (EntityArrow a : arrows) {
                        if (nbt.getInteger("Arrows") < MAX_SIZE) {
                            worldIn.removeEntity(a);
                            nbt.setInteger("Arrows", nbt.getInteger("Arrows") + 1);
                            continue;
                        }
                        itemStack.setTagCompound(nbt);
                        return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
                    }
                    itemStack.setTagCompound(nbt);
                    return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
                }
                
                ItemStack itemStack = playerIn.getHeldItem(handIn);
                if (playerIn.inventory.hasItemStack(new ItemStack(Items.ARROW))) {
                    NBTTagCompound nbt = itemStack.getTagCompound();
                    if (nbt == null) {
                        nbt = new NBTTagCompound();
                    }
                    
                    int arrowsSlot = playerIn.inventory.getSlotFor(new ItemStack(Items.ARROW));
                    ItemStack arrowStack = playerIn.inventory.getStackInSlot(arrowsSlot);
                    int arrowStackSize = arrowStack.getCount();
                    int quiverArrowsCount = nbt.getInteger("Arrows");
                    
                    if (quiverArrowsCount + arrowStackSize > MAX_SIZE) {
                        if (quiverArrowsCount != MAX_SIZE) {
                            int arrowsSet = arrowStackSize + quiverArrowsCount - MAX_SIZE;
                            int inQuiver = arrowStackSize - arrowsSet;
                            playerIn.inventory.setInventorySlotContents(arrowsSlot, new ItemStack(Items.ARROW, arrowsSet));
                            nbt.setInteger("Arrows", nbt.getInteger("Arrows") + inQuiver);
                            itemStack.setTagCompound(nbt);
                            return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
                        }
                        return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
                    }
                    
                    nbt.setInteger("Arrows", nbt.getInteger("Arrows") + arrowStackSize);
                    itemStack.setTagCompound(nbt);
                    playerIn.inventory.removeStackFromSlot(arrowsSlot);
                }
                return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
            }
            
            ItemStack quiverWithArrowsStack = playerIn.getHeldItem(handIn);
            NBTTagCompound nbt = quiverWithArrowsStack.getTagCompound();
            if (nbt == null) {
                nbt = new NBTTagCompound();
            }
            
            int arrowsCount = nbt.getInteger("Arrows");
            if (arrowsCount >= 64) {
                nbt.setInteger("Arrows", nbt.getInteger("Arrows") - 64);
                playerIn.inventory.addItemStackToInventory(new ItemStack(Items.ARROW, 64));
            } else {
                nbt.setInteger("Arrows", 0);
                playerIn.inventory.addItemStackToInventory(new ItemStack(Items.ARROW, arrowsCount));
            }
            quiverWithArrowsStack.setTagCompound(nbt);
            return new ActionResult<>(EnumActionResult.SUCCESS, quiverWithArrowsStack);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
    
    @Override
    public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
        EntityTippedArrow entitytippedarrow = new EntityTippedArrow(worldIn, shooter);
        return entitytippedarrow;
    }
    
    @Override
    public boolean isInfinite(ItemStack stack, ItemStack bow, EntityPlayer player) {
        int enchant = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, bow);
        if (enchant <= 0) {
            NBTTagCompound nbt = stack.getTagCompound();
            if (nbt == null) {
                nbt = new NBTTagCompound();
            }
            nbt.setInteger("Arrows", nbt.getInteger("Arrows") - 1);
            stack.setTagCompound(nbt);
            return true;
        }
        return true;
    }
    
    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }
    
    @Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// Solo ejecutar en el servidor evitando la duplicación
		if (worldIn.isRemote) {
			return;
		}
    
		if (!(entityIn instanceof EntityPlayer)) {
			return;
		}
    
		EntityPlayer player = (EntityPlayer) entityIn;
    
		// Verificar si este stack tiene flechas
		if (stack.isEmpty() || !stack.hasTagCompound()) {
			return;
		}
    
		NBTTagCompound nbt = stack.getTagCompound();
		if (!nbt.hasKey("Arrows")) {
			return;
		}
		
		int arrows = nbt.getInteger("Arrows");
		
		// Si no hay flechas, convertir a carcaj vacío
		if (arrows <= 0) {
			ItemStack emptyQuiver = new ItemStack(ItemInit.QUIVER);
			
			// Determinar dónde está el carcaj y reemplazarlo una sola vez
			if (player.getHeldItemMainhand() == stack) {
				player.setHeldItem(EnumHand.MAIN_HAND, emptyQuiver);
			} else if (player.getHeldItemOffhand() == stack) {
				player.setHeldItem(EnumHand.OFF_HAND, emptyQuiver);
			} else {
				// Buscar en el inventario
				for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
					if (player.inventory.getStackInSlot(i) == stack) {
						player.inventory.setInventorySlotContents(i, emptyQuiver);
						break;
					}
				}
			}
		}
	}
    
    private EntityEquipmentSlot getSlotForItemSlot(int itemSlot) {
        if (itemSlot >= 36 && itemSlot <= 39) {
            return EntityEquipmentSlot.values()[39 - itemSlot]; // ARMOR slots
        } else if (itemSlot == 40) {
            return EntityEquipmentSlot.OFFHAND;
        } else if (itemSlot >= 0 && itemSlot <= 8) {
            return EntityEquipmentSlot.MAINHAND;
        }
        return null;
    }
}
