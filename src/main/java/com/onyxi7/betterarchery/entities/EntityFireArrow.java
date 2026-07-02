package com.onyxi7.betterarchery.entities;

import com.onyxi7.betterarchery.init.ItemInit;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityFireArrow extends EntityCustomArrow {
    
    public EntityFireArrow(World worldIn) {
        super(worldIn);
    }
    
    public EntityFireArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }
    
    public EntityFireArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }
    
    @Override
    protected void arrowHit(EntityLivingBase living) {
        super.arrowHit(living);
        if (living != null && living != this.shootingEntity) {
            living.setFire(5);
        }
    }
    
    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(ItemInit.FIRE_ARROW);
    }
}
