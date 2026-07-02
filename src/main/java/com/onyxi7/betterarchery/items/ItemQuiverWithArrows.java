package com.onyxi7.betterarchery.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.onyxi7.betterarchery.betterarchery;
import com.onyxi7.betterarchery.init.CreativeTabInit;
import com.onyxi7.betterarchery.init.ItemInit;
import com.onyxi7.betterarchery.util.interfaces.IHasModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles")
public class ItemQuiverWithArrows extends Item implements IHasModel, IBauble {
    
    public static final int MAX_ARROWS = 64;
    
    public ItemQuiverWithArrows(String name) {
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
        
    public static int getArrowCount(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Arrows")) {
            return stack.getTagCompound().getInteger("Arrows");
        }
        return 0;
    }
    
    public static void setArrowCount(ItemStack stack, int count) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        stack.getTagCompound().setInteger("Arrows", count);
    }
    
    public static String getArrowType(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("ArrowType")) {
            return stack.getTagCompound().getString("ArrowType");
        }
        return "minecraft:arrow";
    }
    
    public static void setArrowType(ItemStack stack, String arrowType) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        stack.getTagCompound().setString("ArrowType", arrowType);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, net.minecraft.util.EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        
        if (!worldIn.isRemote) {
            // If sneaking, extract arrows
            if (playerIn.isSneaking()) {
                extractArrows(playerIn, stack, handIn);
            } else {
                // Try to add more arrows
                addArrowsFromInventory(playerIn, stack);
            }
        }
        
        return ActionResult.newResult(net.minecraft.util.EnumActionResult.SUCCESS, stack);
    }
        
    private void extractArrows(EntityPlayer player, ItemStack quiverStack, net.minecraft.util.EnumHand handIn) {
		int arrowCount = getArrowCount(quiverStack);
		
		if (arrowCount > 0) {
			ItemStack arrowStack = createArrowStack(quiverStack, Math.min(arrowCount, 64));
			
			if (player.inventory.addItemStackToInventory(arrowStack)) {
				int newCount = arrowCount - arrowStack.getCount();
				
				if (newCount <= 0) {
					ItemStack emptyQuiver = new ItemStack(ItemInit.QUIVER);
					if (quiverStack.hasTagCompound()) {
						NBTTagCompound nbt = quiverStack.getTagCompound();
						nbt.removeTag("Arrows");
						nbt.removeTag("ArrowType");
						if (!nbt.hasNoTags()) {
							emptyQuiver.setTagCompound(nbt);
						}
					}
					player.setHeldItem(handIn, emptyQuiver);
				} else {
					setArrowCount(quiverStack, newCount);
				}
				
				player.sendStatusMessage(new net.minecraft.util.text.TextComponentTranslation(
					"message.betterarchery.quiver.extracted", arrowStack.getCount()), true);
			} else {
				player.dropItem(arrowStack, false);
				int newCount = arrowCount - arrowStack.getCount();
				
				if (newCount <= 0) {
					ItemStack emptyQuiver = new ItemStack(ItemInit.QUIVER);
					player.setHeldItem(handIn, emptyQuiver);
				} else {
					setArrowCount(quiverStack, newCount);
				}
			}
		}
	}

	private void addArrowsFromInventory(EntityPlayer player, ItemStack quiverStack) {
		int currentCount = getArrowCount(quiverStack);
		int spaceLeft = MAX_ARROWS - currentCount;
		
		if (spaceLeft <= 0) {
			player.sendStatusMessage(new net.minecraft.util.text.TextComponentTranslation(
				"message.betterarchery.quiver.full"), true);
			return;
		}
		
		int totalAdded = 0;
		
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack slotStack = player.inventory.getStackInSlot(i);
			
			if (isCompatibleArrow(slotStack, quiverStack)) {
				int toAdd = Math.min(spaceLeft - totalAdded, slotStack.getCount());
				
				if (toAdd > 0) {
					totalAdded += toAdd;
					slotStack.shrink(toAdd);
					
					if (slotStack.isEmpty()) {
						player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
					}
					
					if (totalAdded >= spaceLeft) {
						break;
					}
				}
			}
		}
		
		if (totalAdded > 0) {
			setArrowCount(quiverStack, currentCount + totalAdded);
			player.sendStatusMessage(new net.minecraft.util.text.TextComponentTranslation(
				"message.betterarchery.quiver.added", totalAdded), true);
		}
	}
    
    private boolean isCompatibleArrow(ItemStack stack, ItemStack quiverStack) {
        if (stack.isEmpty()) return false;
        if (!(stack.getItem() instanceof ItemArrow) && stack.getItem() != Items.ARROW) return false;
        
        // Check if quiver already has arrows of different type
        String quiverArrowType = getArrowType(quiverStack);
        if (quiverArrowType.isEmpty()) {
            return true; // Empty quiver can accept any arrow
        }
        
        String stackArrowType = stack.getItem().getRegistryName().toString();
        return quiverArrowType.equals(stackArrowType);
    }
    
    private boolean isArrow(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return stack.getItem() instanceof ItemArrow || stack.getItem() == Items.ARROW;
    }
        
    public static ItemStack createArrowStack(ItemStack quiverStack, int count) {
        String arrowType = getArrowType(quiverStack);
        
        if (arrowType.isEmpty() || arrowType.equals("minecraft:arrow")) {
            return new ItemStack(Items.ARROW, count);
        }
        
        // Try to get the arrow item from registry
        Item arrowItem = Item.getByNameOrId(arrowType);
        if (arrowItem != null && arrowItem instanceof ItemArrow) {
            return new ItemStack(arrowItem, count);
        }
        
        // Fallback to regular arrow
        return new ItemStack(Items.ARROW, count);
    }
        
    public static ItemStack supplyArrow(EntityPlayer player) {
        // Check Baubles first
        if (com.onyxi7.betterarchery.compat.BaublesCompat.isBaublesLoaded()) {
            ItemStack baubleQuiver = com.onyxi7.betterarchery.compat.BaublesCompat.getQuiverFromBaubles(player);
            if (!baubleQuiver.isEmpty() && baubleQuiver.getItem() instanceof ItemQuiverWithArrows) {
                return getArrowFromQuiver(baubleQuiver, player);
            }
        }
        
        // Check inventory
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof ItemQuiverWithArrows) {
                return getArrowFromQuiver(stack, player);
            }
        }
        
        return ItemStack.EMPTY;
    }
    
    private static ItemStack getArrowFromQuiver(ItemStack quiverStack, EntityPlayer player) {
        int count = getArrowCount(quiverStack);
        
        if (count > 0) {
            ItemStack arrow = createArrowStack(quiverStack, 1);
            setArrowCount(quiverStack, count - 1);
            
            // If empty, transform back
            if (count - 1 <= 0) {
                ItemStack emptyQuiver = new ItemStack(ItemInit.QUIVER);
                
                // Find quiver in inventory and replace
                for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                    if (player.inventory.getStackInSlot(i) == quiverStack) {
                        player.inventory.setInventorySlotContents(i, emptyQuiver);
                        break;
                    }
                }
            }
            
            return arrow;
        }
        
        return ItemStack.EMPTY;
    }
    
    // === TOOLTIP ===
    
    @Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		int arrowCount = getArrowCount(stack);
		String arrowType = getArrowType(stack);
		
		// Use translation keys
		tooltip.add(net.minecraft.util.text.translation.I18n.translateToLocalFormatted(
			"tooltip.betterarchery.quiver.arrows", arrowCount, MAX_ARROWS));
		
		if (!arrowType.isEmpty() && !arrowType.equals("minecraft:arrow")) {
			String arrowName = arrowType.substring(arrowType.lastIndexOf(':') + 1);
			tooltip.add(net.minecraft.util.text.translation.I18n.translateToLocalFormatted(
				"tooltip.betterarchery.quiver.type", capitalizeFirstLetter(arrowName)));
		}
		
		tooltip.add(net.minecraft.util.text.translation.I18n.translateToLocal(
			"tooltip.betterarchery.quiver.extract_hint"));
	}

	private String capitalizeFirstLetter(String str) {
		if (str.isEmpty()) return str;
		return str.substring(0, 1).toUpperCase() + str.substring(1);
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
