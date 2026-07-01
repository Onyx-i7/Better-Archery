package com.onyxi7.betterarchery.entities;

import com.onyxi7.betterarchery.config.BetterArcheryConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityDrillArrow extends EntityArrow {
    
    private float drillPower = 1.0F;
    private int blocksDestroyed = 0;
    
    public EntityDrillArrow(World worldIn) {
        super(worldIn);
    }
    
    public EntityDrillArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }
    
    public EntityDrillArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
        if (BetterArcheryConfig.general.debugMode) {
            System.out.println("[BetterArchery] EntityDrillArrow created - Config MaxBlocks: " + 
                             BetterArcheryConfig.arrows.drillArrowMaxBlocks);
        }
    }
    
    public void setDrillPower(float power) {
        this.drillPower = power;
    }
    
    public float getDrillPower() {
        return this.drillPower;
    }
    
    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(Items.ARROW);
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        
        if (!this.inGround && !this.world.isRemote) {
            Vec3d currentPos = new Vec3d(this.posX, this.posY, this.posZ);
            Vec3d motion = new Vec3d(this.motionX, this.motionY, this.motionZ);
            Vec3d nextPos = currentPos.add(motion);
            
            RayTraceResult rayTrace = this.world.rayTraceBlocks(currentPos, nextPos, false, true, false);
            
            if (rayTrace != null && rayTrace.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos blockPos = rayTrace.getBlockPos();
                IBlockState blockState = this.world.getBlockState(blockPos);
                
                if (canDestroyBlock(blockState, blockPos)) {
                    destroyBlock(blockPos);
                    
                    this.drillPower -= (float) BetterArcheryConfig.arrows.drillArrowPowerLoss;
                    this.blocksDestroyed++;
                    
                    if (BetterArcheryConfig.general.debugMode) {
                        System.out.println("[BetterArchery] DrillArrow destroyed block - Power: " + this.drillPower + 
                                         ", Blocks: " + this.blocksDestroyed + "/" + BetterArcheryConfig.arrows.drillArrowMaxBlocks);
                    }
                    
                    if (this.drillPower <= 0 || this.blocksDestroyed >= BetterArcheryConfig.arrows.drillArrowMaxBlocks) {
                        if (BetterArcheryConfig.general.debugMode) {
                            System.out.println("[BetterArchery] DrillArrow stopping");
                        }
                        this.setDead();
                        return;
                    }
                    
                    net.minecraft.util.EnumFacing facing = rayTrace.sideHit;
                    this.setPosition(
                        this.posX + facing.getXOffset() * 0.5,
                        this.posY + facing.getYOffset() * 0.5,
                        this.posZ + facing.getZOffset() * 0.5
                    );
                } else {
                    super.onHit(rayTrace);
                }
            }
        }
    }
    
    private boolean canDestroyBlock(IBlockState blockState, BlockPos pos) {
        float hardness = blockState.getBlockHardness(this.world, pos);
        
        if (hardness < 0 || hardness >= 50.0F || this.drillPower <= 0) {
            return false;
        }
        
        float requiredPower = hardness / 10.0F;
        return this.drillPower >= requiredPower;
    }
    
    private void destroyBlock(BlockPos pos) {
        this.world.destroyBlock(pos, false);
    }
    
    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        if (raytraceResultIn.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos blockPos = raytraceResultIn.getBlockPos();
            IBlockState blockState = this.world.getBlockState(blockPos);
            
            if (canDestroyBlock(blockState, blockPos)) {
                destroyBlock(blockPos);
                this.drillPower -= (float) BetterArcheryConfig.arrows.drillArrowPowerLoss;
                this.blocksDestroyed++;
                
                if (this.drillPower > 0 && this.blocksDestroyed < BetterArcheryConfig.arrows.drillArrowMaxBlocks) {
                    return;
                }
            }
        }
        
        super.onHit(raytraceResultIn);
    }
    
    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setFloat("DrillPower", this.drillPower);
        compound.setInteger("BlocksDestroyed", this.blocksDestroyed);
    }
    
    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("DrillPower")) {
            this.drillPower = compound.getFloat("DrillPower");
        }
        if (compound.hasKey("BlocksDestroyed")) {
            this.blocksDestroyed = compound.getInteger("BlocksDestroyed");
        }
    }
}
