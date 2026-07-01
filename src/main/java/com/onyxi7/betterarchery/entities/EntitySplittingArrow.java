package com.onyxi7.betterarchery.entities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntitySplittingArrow extends EntityArrow {
    
    public EntitySplittingArrow(World worldIn) {
        super(worldIn);
    }
    
    public EntitySplittingArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }
    
    public EntitySplittingArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }
    
    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(Items.ARROW);
    }
    
    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        if (!this.world.isRemote && raytraceResultIn.entityHit != null) {
            if (this.shootingEntity instanceof EntityLivingBase) {
                EntityLivingBase shooter = (EntityLivingBase) this.shootingEntity;
                
                for (int i = 0; i < 3; i++) {
                    EntityTippedArrow splitArrow = new EntityTippedArrow(this.world, shooter);
                    
                    splitArrow.posX = this.posX;
                    splitArrow.posY = this.posY;
                    splitArrow.posZ = this.posZ;
                    
                    double spread = 0.3;
                    splitArrow.motionX = this.motionX + (this.world.rand.nextDouble() - 0.5) * spread;
                    splitArrow.motionY = this.motionY + (this.world.rand.nextDouble() - 0.5) * spread;
                    splitArrow.motionZ = this.motionZ + (this.world.rand.nextDouble() - 0.5) * spread;
                    
                    splitArrow.setDamage(this.getDamage() * 0.5);
                    this.world.spawnEntity(splitArrow);
                }
            } else {
                for (int i = 0; i < 3; i++) {
                    EntityTippedArrow splitArrow = new EntityTippedArrow(this.world);
                    
                    splitArrow.posX = this.posX;
                    splitArrow.posY = this.posY;
                    splitArrow.posZ = this.posZ;
                    
                    double spread = 0.3;
                    splitArrow.motionX = this.motionX + (this.world.rand.nextDouble() - 0.5) * spread;
                    splitArrow.motionY = this.motionY + (this.world.rand.nextDouble() - 0.5) * spread;
                    splitArrow.motionZ = this.motionZ + (this.world.rand.nextDouble() - 0.5) * spread;
                    
                    splitArrow.setDamage(this.getDamage() * 0.5);
                    this.world.spawnEntity(splitArrow);
                }
            }
        }
        
        super.onHit(raytraceResultIn);
    }
}
