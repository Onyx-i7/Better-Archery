package com.onyxi7.betterarchery.entities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityImpactArrow extends EntityArrow {
    
    public EntityImpactArrow(World worldIn) {
        super(worldIn);
    }
    
    public EntityImpactArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }
    
    public EntityImpactArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }
    
    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(Items.ARROW);
    }
    
    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        super.onHit(raytraceResultIn);
        
        if (!this.world.isRemote) {
            EntityTNTPrimed tnt = new EntityTNTPrimed(this.world, this.posX, this.posY, this.posZ, 
                this.shootingEntity instanceof EntityLivingBase ? (EntityLivingBase) this.shootingEntity : null);
            
            tnt.setFuse(0);
            
            this.world.spawnEntity(tnt);
            
            this.setDead();
        }
    }
}
