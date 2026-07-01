package com.onyxi7.betterarchery.entities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityTorchArrow extends EntityArrow {
    
    public EntityTorchArrow(World worldIn) {
        super(worldIn);
    }
    
    public EntityTorchArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }
    
    public EntityTorchArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }
    
    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(Items.ARROW);
    }
    
    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        super.onHit(raytraceResultIn);
        
        if (!this.world.isRemote && raytraceResultIn.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos pos = raytraceResultIn.getBlockPos();
            BlockPos offset = pos.offset(raytraceResultIn.sideHit);
            
            if (this.world.isAirBlock(offset)) {
                if (Blocks.TORCH.canPlaceBlockAt(world, offset)) {
                    this.world.setBlockState(offset, Blocks.TORCH.getDefaultState());
                    this.setDead();
                }
            }
        }
    }
}
