package com.onyxi7.betterarchery.entities;

import com.onyxi7.betterarchery.init.ItemInit;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityDrillArrow extends EntityCustomArrow {
    
    private int blocksDestroyed = 0;
    private float drillPower = 1.0F;
    private static final int MAX_BLOCKS = 10;
    
    public EntityDrillArrow(World worldIn) {
        super(worldIn);
    }
    
    public EntityDrillArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }
    
    public EntityDrillArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }
    
    public void setDrillPower(float power) {
        this.drillPower = power;
    }
    
    public float getDrillPower() {
        return this.drillPower;
    }
    
    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        if (!this.world.isRemote && raytraceResultIn.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos blockpos = raytraceResultIn.getBlockPos();
            IBlockState state = this.world.getBlockState(blockpos);
            
            // Don't break unbreakable blocks
            if (state.getBlockHardness(this.world, blockpos) >= 0 && 
                state.getBlock() != Blocks.BEDROCK &&
                blocksDestroyed < MAX_BLOCKS &&
                drillPower > 0) {
                
                this.world.destroyBlock(blockpos, true);
                blocksDestroyed++;
                drillPower -= 0.1F; // Reduce power per block
                
                // Continue through the block
                Vec3d motion = new Vec3d(this.motionX, this.motionY, this.motionZ).normalize();
                BlockPos nextBlock = blockpos.add(
                    (int) Math.round(motion.x),
                    (int) Math.round(motion.y),
                    (int) Math.round(motion.z)
                );
                
                if (this.world.getBlockState(nextBlock).getBlock() != Blocks.AIR) {
                    this.onHit(new RayTraceResult(RayTraceResult.Type.BLOCK, motion, raytraceResultIn.sideHit, nextBlock));
                } else {
                    super.onHit(raytraceResultIn);
                }
            } else {
                super.onHit(raytraceResultIn);
            }
        } else {
            super.onHit(raytraceResultIn);
        }
    }
    
    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(ItemInit.DRILL_ARROW);
    }
}
