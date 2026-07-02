package com.onyxi7.betterarchery.entities;

import com.onyxi7.betterarchery.init.ItemInit;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntitySplittingArrow extends EntityCustomArrow {
    
    public EntitySplittingArrow(World worldIn) {
        super(worldIn);
    }
    
    public EntitySplittingArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }
    
    public EntitySplittingArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }
    
    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        super.onHit(raytraceResultIn);
        
        if (!this.world.isRemote) {
            // Create 3 additional arrows
            for (int i = 0; i < 3; i++) {
                EntitySplittingArrow arrow = new EntitySplittingArrow(this.world, (EntityLivingBase) this.shootingEntity);
                arrow.setPosition(this.posX, this.posY, this.posZ);
                
                // Spread arrows in different directions
                double spreadX = (this.world.rand.nextDouble() - 0.5) * 0.5;
                double spreadY = (this.world.rand.nextDouble() - 0.5) * 0.5;
                double spreadZ = (this.world.rand.nextDouble() - 0.5) * 0.5;
                
                arrow.shoot(
                    this.motionX + spreadX,
                    this.motionY + spreadY,
                    this.motionZ + spreadZ,
                    1.5F,
                    1.0F
                );
                
                this.world.spawnEntity(arrow);
            }
            
            this.setDead();
        }
    }
    
    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(ItemInit.SPLITTING_ARROW);
    }
}
