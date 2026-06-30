package com.onyxi7.betterarchery.items.quiver;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class QuiverInventory implements IInventory {
    
    public ItemStack[] inv;
    public InventoryPlayer playerInv;
    public int playerInvIndex;
    
    public static int size = 4;
    public static int stackLimit = 16;
    
    public QuiverInventory(InventoryPlayer ownerInv, int invIndex) {
        this.playerInv = ownerInv;
        this.playerInvIndex = invIndex;
        this.inv = new ItemStack[size];
        ItemStack stack = ownerInv.mainInventory.get(invIndex);
        getInventory(stack, false);
    }
    
    public QuiverInventory(ItemStack quiverStack) {
        this.inv = new ItemStack[size];
        getInventory(quiverStack, true);
    }
    
    private void getInventory(ItemStack quiverStack, boolean noUpdate) {
        if (!quiverStack.isEmpty()) {
            NBTTagCompound tagCompound = quiverStack.getTagCompound();
            if (tagCompound != null) {
                NBTTagList itemList = tagCompound.getTagList("quiver", 10);
                for (int i = 0; i < itemList.tagCount(); i++) {
                    NBTTagCompound nbtStack = itemList.getCompoundTagAt(i);
                    ItemStack stack = new ItemStack(nbtStack);
                    byte slot = nbtStack.getByte("Slot");
                    if (noUpdate) {
                        setInventorySlotContentsNoUpdate(slot, stack);
                    } else {
                        setInventorySlotContents(slot, stack);
                    }
                }
            }
        }
    }
    
    @Override
    public int getSizeInventory() {
        return this.inv.length;
    }
    
    public int getArrowCount() {
        int count = 0;
        for (ItemStack stack : this.inv) {
            if (!stack.isEmpty() && stack.getItem() instanceof ItemArrow) {
                count += stack.getCount();
            }
        }
        return count;
    }
    
    public static int getMaxArrowCount() {
        return stackLimit * size;
    }
    
    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.inv) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.inv[slot];
    }
    
    public void setInventorySlotContentsNoUpdate(int slot, ItemStack stack) {
        this.inv[slot] = stack;
    }
    
    @Override
    public ItemStack decrStackSize(int slot, int amt) {
        ItemStack stack = getStackInSlot(slot);
        if (!stack.isEmpty()) {
            if (stack.getCount() <= amt) {
                setInventorySlotContents(slot, ItemStack.EMPTY);
            } else {
                stack = stack.splitStack(amt);
                if (stack.getCount() == 0) {
                    setInventorySlotContents(slot, ItemStack.EMPTY);
                }
            }
        }
        return stack;
    }
    
    @Override
    public ItemStack removeStackFromSlot(int slot) {
        ItemStack stack = getStackInSlot(slot);
        if (!stack.isEmpty()) {
            setInventorySlotContents(slot, ItemStack.EMPTY);
        }
        return stack;
    }
    
    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        setInventorySlotContentsNoUpdate(slot, stack);
        markDirty();
    }
    
    @Override
    public int getInventoryStackLimit() {
        return stackLimit;
    }
    
    @Override
    public void markDirty() {
        for (int i = 0; i < size; i++) {
            if (!this.inv[i].isEmpty() && this.inv[i].getCount() <= 0) {
                this.inv[i] = ItemStack.EMPTY;
            }
        }
        
        NBTTagList quiverCompound = new NBTTagList();
        int j = 0;
        for (ItemStack item : this.inv) {
            NBTTagCompound itemCompound = new NBTTagCompound();
            if (!item.isEmpty()) {
                item.writeToNBT(itemCompound);
                itemCompound.setByte("Slot", (byte) j);
                quiverCompound.appendTag(itemCompound);
            }
            j++;
        }
        
        ItemStack quiverStack = this.playerInv.mainInventory.get(this.playerInvIndex);
        if (!quiverStack.hasTagCompound()) {
            quiverStack.setTagCompound(new NBTTagCompound());
        }
        quiverStack.getTagCompound().setTag("quiver", quiverCompound);
        updateDamage(quiverStack);
    }
    
    public void updateDamage() {
        updateDamage(this.playerInv.mainInventory.get(this.playerInvIndex));
    }
    
    public void updateDamage(ItemStack quiverStack) {
        int damage = getArrowCount();
        quiverStack.setItemDamage(damage);
    }
    
    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }
    
    @Override
    public void openInventory(EntityPlayer player) {
    }
    
    @Override
    public void closeInventory(EntityPlayer player) {
        markDirty();
    }
    
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return stack.getItem() instanceof ItemArrow;
    }
    
    @Override
    public int getField(int id) {
        return 0;
    }
    
    @Override
    public void setField(int id, int value) {
    }
    
    @Override
    public int getFieldCount() {
        return 0;
    }
    
    @Override
    public void clear() {
        for (int i = 0; i < this.inv.length; i++) {
            this.inv[i] = ItemStack.EMPTY;
        }
        markDirty();
    }
    
    @Override
    public String getName() {
        return "quiver.inventory";
    }
    
    @Override
    public boolean hasCustomName() {
        return false;
    }
    
    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString(getName());
    }
}
