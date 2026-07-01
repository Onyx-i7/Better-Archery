package com.onyxi7.betterarchery.entities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
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
        // Crear 3 flechas adicionales en diferentes direcciones
        if (!this.world.isRemote && raytraceResultIn.entityHit != null) {
            for (int i = 0; i < 3; i++) {
                EntityArrow splitArrow = new EntityArrow(this.world, this.shootingEntity) {
                    @Override
                    protected ItemStack getArrowStack() {
                        return new ItemStack(Items.ARROW);
                    }
                };
                
                splitArrow.posX = this.posX;
                splitArrow.posY = this.posY;
                splitArrow.posZ = this.posZ;
                
                // Dirección aleatoria dispersa
                double spread = 0.3;
                splitArrow.motionX = this.motionX + (this.world.rand.nextDouble() - 0.5) * spread;
                splitArrow.motionY = this.motionY + (this.world.rand.nextDouble() - 0.5) * spread;
                splitArrow.motionZ = this.motionZ + (this.world.rand.nextDouble() - 0.5) * spread;
                
                splitArrow.setDamage(this.getDamage() * 0.5);
                this.world.spawnEntity(splitArrow);
            }
        }
        
        super.onHit(raytraceResultIn);
    }
}
