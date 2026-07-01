package com.onyxi7.betterarchery.entities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityEnderArrow extends EntityArrow {
    
    public EntityEnderArrow(World worldIn) {
        super(worldIn);
    }
    
    public EntityEnderArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }
    
    public EntityEnderArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }
    
    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(Items.ARROW);
    }
    
    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        super.onHit(raytraceResultIn);
        
        if (!this.world.isRemote && this.shootingEntity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) this.shootingEntity;
            
            double x = this.posX;
            double y = this.posY;
            double z = this.posZ;
            
            y = Math.floor(y) + 0.5;
            
            player.setPositionAndUpdate(x, y, z);
            
            this.world.playSound(null, player.posX, player.posY, player.posZ,
                SoundEvents.ENTITY_ENDERMEN_TELEPORT,
                SoundCategory.PLAYERS, 1.0F, 1.0F);
            
            this.setDead();
        }
    }
}
