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
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
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
    
    // === NBT METHODS ===
    
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
    
    // === RIGHT CLICK LOGIC ===
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        
        if (!worldIn.isRemote) {
            if (playerIn.isSneaking()) {
                extractArrows(playerIn, stack, handIn);
            } else {
                addArrowsFromInventory(playerIn, stack);
            }
        }
        
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }
    
    // === EXTRACT ARROWS ===
    
    private void extractArrows(EntityPlayer player, ItemStack quiverStack, EnumHand handIn) {
        int arrowCount = getArrowCount(quiverStack);
        
        if (arrowCount <= 0) return;
        
        // Create arrow stack with correct type
        int extractAmount = Math.min(arrowCount, 64);
        ItemStack arrowStack = createArrowStack(quiverStack, extractAmount);
        
        // Try to add to inventory
        boolean added = player.inventory.addItemStackToInventory(arrowStack);
        
        if (added) {
            // Successfully added to inventory - reduce count
            int newCount = arrowCount - extractAmount;
            
            if (newCount <= 0) {
                // Transform back to empty quiver
                ItemStack emptyQuiver = new ItemStack(ItemInit.QUIVER);
                player.setHeldItem(handIn, emptyQuiver);
            } else {
                setArrowCount(quiverStack, newCount);
            }
            
            player.sendStatusMessage(new TextComponentTranslation(
                "message.betterarchery.quiver.extracted", extractAmount), true);
        } else {
            // Inventory full - drop on ground
            player.dropItem(arrowStack, false);
            
            int newCount = arrowCount - extractAmount;
            
            if (newCount <= 0) {
                ItemStack emptyQuiver = new ItemStack(ItemInit.QUIVER);
                player.setHeldItem(handIn, emptyQuiver);
            } else {
                setArrowCount(quiverStack, newCount);
            }
        }
    }
    
    // === ADD ARROWS ===
    
    private void addArrowsFromInventory(EntityPlayer player, ItemStack quiverStack) {
        int currentCount = getArrowCount(quiverStack);
        String currentType = getArrowType(quiverStack);
        int spaceLeft = MAX_ARROWS - currentCount;
        
        if (spaceLeft <= 0) {
            player.sendStatusMessage(new TextComponentTranslation(
                "message.betterarchery.quiver.full"), true);
            return;
        }
        
        int totalAdded = 0;
        
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack slotStack = player.inventory.getStackInSlot(i);
            
            if (isCompatibleArrow(slotStack, currentType)) {
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
            player.sendStatusMessage(new TextComponentTranslation(
                "message.betterarchery.quiver.added", totalAdded), true);
        }
    }
    
	public static ItemStack peekArrow(EntityPlayer player) {
		if (BetterArcheryConfig.debug.logQuiverDetection) {
			System.out.println("[BetterArchery DEBUG] peekArrow() called for player: " + player.getName());
		}
		
		// Check Baubles first
		if (com.onyxi7.betterarchery.compat.BaublesCompat.isBaublesLoaded()) {
			if (BetterArcheryConfig.debug.logQuiverDetection) {
				System.out.println("[BetterArchery DEBUG] Checking Baubles...");
			}
			
			ItemStack baubleQuiver = com.onyxi7.betterarchery.compat.BaublesCompat.getQuiverFromBaubles(player);
			if (!baubleQuiver.isEmpty() && baubleQuiver.getItem() instanceof ItemQuiverWithArrows) {
				int count = getArrowCount(baubleQuiver);
				if (BetterArcheryConfig.debug.logQuiverDetection) {
					System.out.println("[BetterArchery DEBUG] Found quiver in Baubles with " + count + " arrows");
				}
				if (count > 0) {
					return createArrowStack(baubleQuiver, 1);
				}
			}
		}
		
		// Check inventory
		if (BetterArcheryConfig.debug.logQuiverDetection) {
			System.out.println("[BetterArchery DEBUG] Checking inventory...");
		}
		
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if (!stack.isEmpty() && stack.getItem() instanceof ItemQuiverWithArrows) {
				int count = getArrowCount(stack);
				if (BetterArcheryConfig.debug.logQuiverDetection) {
					System.out.println("[BetterArchery DEBUG] Found quiver in inventory slot " + i + " with " + count + " arrows");
				}
				if (count > 0) {
					return createArrowStack(stack, 1);
				}
			}
		}
		
		if (BetterArcheryConfig.debug.logQuiverDetection) {
			System.out.println("[BetterArchery DEBUG] No quiver with arrows found");
		}
		
		return ItemStack.EMPTY;
	}

	public static ItemStack supplyArrow(EntityPlayer player) {
		if (BetterArcheryConfig.debug.logQuiverDetection) {
			System.out.println("[BetterArchery DEBUG] supplyArrow() called for player: " + player.getName());
		}
		
		// Check Baubles first
		if (com.onyxi7.betterarchery.compat.BaublesCompat.isBaublesLoaded()) {
			ItemStack baubleQuiver = com.onyxi7.betterarchery.compat.BaublesCompat.getQuiverFromBaubles(player);
			if (!baubleQuiver.isEmpty() && baubleQuiver.getItem() instanceof ItemQuiverWithArrows) {
				if (BetterArcheryConfig.debug.logQuiverDetection) {
					System.out.println("[BetterArchery DEBUG] Consuming arrow from Baubles quiver");
				}
				return consumeArrowFromQuiver(baubleQuiver, player);
			}
		}
		
		// Check inventory
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if (!stack.isEmpty() && stack.getItem() instanceof ItemQuiverWithArrows) {
				if (BetterArcheryConfig.debug.logQuiverDetection) {
					System.out.println("[BetterArchery DEBUG] Consuming arrow from inventory quiver slot " + i);
				}
				return consumeArrowFromQuiver(stack, player);
			}
		}
		
		if (BetterArcheryConfig.debug.logQuiverDetection) {
			System.out.println("[BetterArchery DEBUG] No quiver found to consume from");
		}
		
		return ItemStack.EMPTY;
	}
    
    // === ARROW COMPATIBILITY ===
    
    private boolean isCompatibleArrow(ItemStack stack, String quiverArrowType) {
        if (stack.isEmpty()) return false;
        if (!(stack.getItem() instanceof ItemArrow) && stack.getItem() != Items.ARROW) return false;
        
        String stackArrowType = stack.getItem().getRegistryName().toString();
        return quiverArrowType.equals(stackArrowType);
    }
    
    // === CREATE ARROW STACK ===
    
    public static ItemStack createArrowStack(ItemStack quiverStack, int count) {
        String arrowType = getArrowType(quiverStack);
        
        // Try to get the arrow item from registry
        Item arrowItem = Item.getByNameOrId(arrowType);
        if (arrowItem != null && (arrowItem instanceof ItemArrow || arrowItem == Items.ARROW)) {
            return new ItemStack(arrowItem, count);
        }
        
        // Fallback to regular arrow
        return new ItemStack(Items.ARROW, count);
    }
    
    // === SUPPLY ARROW FOR BOW ===
    
    public static ItemStack supplyArrow(EntityPlayer player) {
        // Check Baubles first
        if (com.onyxi7.betterarchery.compat.BaublesCompat.isBaublesLoaded()) {
            ItemStack baubleQuiver = com.onyxi7.betterarchery.compat.BaublesCompat.getQuiverFromBaubles(player);
            if (!baubleQuiver.isEmpty() && baubleQuiver.getItem() instanceof ItemQuiverWithArrows) {
                return consumeArrowFromQuiver(baubleQuiver, player);
            }
        }
        
        // Check inventory
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof ItemQuiverWithArrows) {
                return consumeArrowFromQuiver(stack, player);
            }
        }
        
        return ItemStack.EMPTY;
    }
    
    private static ItemStack consumeArrowFromQuiver(ItemStack quiverStack, EntityPlayer player) {
        int count = getArrowCount(quiverStack);
        
        if (count <= 0) {
            return ItemStack.EMPTY;
        }
        
        // Create single arrow
        ItemStack arrow = createArrowStack(quiverStack, 1);
        
        // Reduce count
        int newCount = count - 1;
        
        if (newCount <= 0) {
            // Transform back to empty quiver
            ItemStack emptyQuiver = new ItemStack(ItemInit.QUIVER);
            
            // Find quiver location and replace
            replaceQuiverWithEmpty(quiverStack, emptyQuiver, player);
        } else {
            setArrowCount(quiverStack, newCount);
        }
        
        return arrow;
    }
    
    private static void replaceQuiverWithEmpty(ItemStack oldQuiver, ItemStack emptyQuiver, EntityPlayer player) {
        // Check Baubles first
        if (com.onyxi7.betterarchery.compat.BaublesCompat.isBaublesLoaded()) {
            int slot = com.onyxi7.betterarchery.compat.BaublesCompat.findQuiverSlot(player);
            if (slot != -1) {
                com.onyxi7.betterarchery.compat.BaublesCompat.setBaubleSlot(player, slot, emptyQuiver);
                return;
            }
        }
        
        // Check regular inventory
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            if (ItemStack.areItemStacksEqual(player.inventory.getStackInSlot(i), oldQuiver)) {
                player.inventory.setInventorySlotContents(i, emptyQuiver);
                return;
            }
        }
    }
    
    // === TOOLTIP ===
    
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int arrowCount = getArrowCount(stack);
        String arrowType = getArrowType(stack);
        
        tooltip.add(I18n.translateToLocalFormatted(
            "tooltip.betterarchery.quiver.arrows", arrowCount, MAX_ARROWS));
        
        if (!arrowType.isEmpty() && !arrowType.equals("minecraft:arrow")) {
            String arrowName = arrowType.substring(arrowType.lastIndexOf(':') + 1);
            tooltip.add(I18n.translateToLocalFormatted(
                "tooltip.betterarchery.quiver.type", capitalizeFirstLetter(arrowName)));
        }
        
        tooltip.add(I18n.translateToLocal("tooltip.betterarchery.quiver.extract_hint"));
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
