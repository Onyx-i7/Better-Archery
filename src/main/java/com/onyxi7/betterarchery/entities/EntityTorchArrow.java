package com.onyxi7.betterarchery.entities;

import com.onyxi7.betterarchery.init.ItemInit;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityTorchArrow extends EntityCustomArrow {
    
    public EntityTorchArrow(World worldIn) {
        super(worldIn);
    }
    
    public EntityTorchArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }
    
    public EntityTorchArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }
    
    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        if (!this.world.isRemote && raytraceResultIn.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos blockpos = raytraceResultIn.getBlockPos().offset(raytraceResultIn.sideHit);
            
            if (this.world.isAirBlock(blockpos)) {
                this.world.setBlockState(blockpos, Blocks.TORCH.getDefaultState());
                this.setDead();
            }
        }
        
        super.onHit(raytraceResultIn);
    }
    
    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(ItemInit.TORCH_ARROW);
    }
}
