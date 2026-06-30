package com.onyxi7.betterarchery.items.quiver;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class QuiverContainer extends Container {
    
    private ItemStack heldItem = null;
    private QuiverInventory quiverInventory;
    private InventoryPlayer playerInventory;
    
    public QuiverContainer(InventoryPlayer playerInv, QuiverInventory quiverInv) {
        this.heldItem = playerInv.getCurrentItem();
        this.playerInventory = playerInv;
        
        if (this.heldItem.isEmpty()) {
            this.heldItem = ItemStack.EMPTY;
        } else if (!this.heldItem.hasTagCompound()) {
            this.heldItem.setTagCompound(new NBTTagCompound());
        }
        
        int x = 89 - 18 * QuiverInventory.size / 2;
        
        // Slots del carcaj
        for (int i = 0; i < QuiverInventory.size; i++) {
            addSlotToContainer(new Slot(quiverInv, i, x, 16) {
                @Override
                public boolean isItemValid(ItemStack stack) {
                    return stack.getItem() instanceof ItemArrow;
                }
            });
            x += 18;
        }
        
        // Inventario del jugador (3x9)
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                int slotIndex = i * 9 + j + 9;
                int slotPosX = 8 + j * 18;
                int slotPosY = 66 + i * 18;
                addSlotToContainer(new Slot(playerInv, slotIndex, slotPosX, slotPosY));
            }
        }
        
        // Hotbar del jugador (9 slots)
        for (int i = 0; i < 9; i++) {
            int slotPosX = 8 + i * 18;
            int slotPosY = 124;
            addSlotToContainer(new Slot(playerInv, i, slotPosX, slotPosY));
        }
        
        this.quiverInventory = quiverInv;
    }
    
    @Override
    public boolean canInteractWith(EntityPlayer player) {
        ItemStack curItem = player.inventory.getCurrentItem();
        if (!curItem.isEmpty() && !this.heldItem.isEmpty() && 
            curItem.getTagCompound() != null && this.heldItem.getTagCompound() != null) {
            return curItem.getTagCompound().getInteger("uniqueID") == 
                   this.heldItem.getTagCompound().getInteger("uniqueID");
        }
        return false;
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        ItemStack stackCopy = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(slotIndex);
        
        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            stackCopy = stack.copy();
            
            if (slotIndex < QuiverInventory.size) {
                // Del carcaj al inventario
                if (!mergeItemStack(stack, QuiverInventory.size, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChanged();
            } else {
                // Del inventario al carcaj
                if (!(stack.getItem() instanceof ItemArrow)) {
                    return ItemStack.EMPTY;
                }
                
                int itemsLeft = stack.getCount();
                
                // Primero intentar combinar con stacks existentes
                int i = 0;
                while (itemsLeft > 0 && i < QuiverInventory.size) {
                    ItemStack stackInSlot = this.quiverInventory.getStackInSlot(i);
                    if (!stackInSlot.isEmpty() && 
                        stackInSlot.getCount() < QuiverInventory.stackLimit &&
                        ItemStack.areItemsEqual(stackInSlot, stack) &&
                        ItemStack.areItemStackTagsEqual(stackInSlot, stack)) {
                        int amount = QuiverInventory.stackLimit - stackInSlot.getCount();
                        if (amount > itemsLeft) {
                            amount = itemsLeft;
                        }
                        stackInSlot.grow(amount);
                        itemsLeft -= amount;
                    }
                    i++;
                }
                
                // Luego poner en slots vacíos
                i = 0;
                while (itemsLeft > 0 && i < QuiverInventory.size) {
                    ItemStack stackInSlot = this.quiverInventory.getStackInSlot(i);
                    if (stackInSlot.isEmpty()) {
                        ItemStack newStack = stack.copy();
                        if (itemsLeft > QuiverInventory.stackLimit) {
                            newStack.setCount(QuiverInventory.stackLimit);
                        } else {
                            newStack.setCount(itemsLeft);
                        }
                        itemsLeft -= newStack.getCount();
                        this.quiverInventory.setInventorySlotContents(i, newStack);
                    }
                    i++;
                }
                
                this.quiverInventory.markDirty();
                
                if (itemsLeft <= 0) {
                    slot.putStack(ItemStack.EMPTY);
                } else {
                    stack.setCount(itemsLeft);
                }
                
                return ItemStack.EMPTY;
            }
            
            if (stack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        
        return stackCopy;
    }
    
    public void transferAllStacks() {
        for (int i = 0; i < QuiverInventory.size; i++) {
            ItemStack stack = quiverInventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                transferStackInSlot(null, i);
            }
        }
    }
}
