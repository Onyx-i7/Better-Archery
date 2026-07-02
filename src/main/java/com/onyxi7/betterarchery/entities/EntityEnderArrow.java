package com.onyxi7.betterarchery.entities;

import com.onyxi7.betterarchery.init.ItemInit;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityEnderArrow extends EntityCustomArrow {
    
    public EntityEnderArrow(World worldIn) {
        super(worldIn);
    }
    
    public EntityEnderArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }
    
    public EntityEnderArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }
    
    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        super.onHit(raytraceResultIn);
        
        if (!this.world.isRemote && this.shootingEntity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) this.shootingEntity;
            player.attemptTeleport(this.posX, this.posY, this.posZ);
            this.setDead();
        }
    }
    
    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(ItemInit.ENDER_ARROW);
    }
}
