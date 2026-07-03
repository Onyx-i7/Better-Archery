package com.onyxi7.betterarchery.entities;

import com.onyxi7.betterarchery.init.EnchantmentInit;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public abstract class EntityCustomArrow extends EntityArrow {
    
    protected int homingTicks = 0;
    protected static final int MAX_HOMING_TICKS = 40; // 2 seconds
    
    // Store the bow that shot this arrow
    protected ItemStack bowStack = ItemStack.EMPTY;
    
    public EntityCustomArrow(World worldIn) {
        super(worldIn);
    }
    
    public EntityCustomArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }
    
    public EntityCustomArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }
    
    // Set the bow that shot this arrow
    public void setBowStack(ItemStack bow) {
        this.bowStack = bow.copy();
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
    }
    
    
    // Save bow stack to NBT
    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        if (!bowStack.isEmpty()) {
            NBTTagCompound bowNBT = new NBTTagCompound();
            bowStack.writeToNBT(bowNBT);
            compound.setTag("BowStack", bowNBT);
        }
    }
    
    // Load bow stack from NBT
    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("BowStack")) {
            this.bowStack = new ItemStack(compound.getCompoundTag("BowStack"));
        }
    }
}
