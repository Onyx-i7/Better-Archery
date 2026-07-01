package com.onyxi7.betterarchery.entities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityFireArrow extends EntityArrow {
    
    public EntityFireArrow(World worldIn) {
        super(worldIn);
        setFire(100);
    }
    
    public EntityFireArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        setFire(100);
    }
    
    public EntityFireArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
        setFire(100);
    }
    
    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(Items.ARROW);
    }
    
    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        super.onHit(raytraceResultIn);
        
        if (!this.world.isRemote) {
            // Prender fuego a la entidad
            if (raytraceResultIn.entityHit != null) {
                raytraceResultIn.entityHit.setFire(5);
            }
            
            // Prender fuego al bloque
            if (raytraceResultIn.typeOfHit == RayTraceResult.Type.BLOCK) {
                net.minecraft.util.math.BlockPos pos = raytraceResultIn.getBlockPos();
                net.minecraft.util.math.BlockPos offset = pos.offset(raytraceResultIn.sideHit);
                
                if (this.world.isAirBlock(offset)) {
                    this.world.setBlockState(offset, net.minecraft.init.Blocks.FIRE.getDefaultState());
                }
            }
        }
    }
}
