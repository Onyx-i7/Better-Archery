package com.onyxi7.betterarchery.entities;

import com.onyxi7.betterarchery.init.ItemInit;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityImpactArrow extends EntityCustomArrow {
    
    public EntityImpactArrow(World worldIn) {
        super(worldIn);
    }
    
    public EntityImpactArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }
    
    public EntityImpactArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }
    
    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        super.onHit(raytraceResultIn);
        
        if (!this.world.isRemote) {
            this.world.createExplosion(
                this,
                this.posX,
                this.posY,
                this.posZ,
                2.0F,
                true
            );
            this.setDead();
        }
    }
    
    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(ItemInit.IMPACT_ARROW);
    }
}
