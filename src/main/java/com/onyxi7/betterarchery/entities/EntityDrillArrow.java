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
    
    private float drillPower = 1.0F; // Drilling force (based on bow load)
    private int blocksDestroyed = 0;
    private static final int MAX_BLOCKS_DESTROYED = 10; // Maximum of 10 blocks
    
    public EntityDrillArrow(World worldIn) {
        super(worldIn);
    }
    
    public EntityDrillArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }
    
    public EntityDrillArrow(World worldIn, EntityLivingBase shooter) {
		super(worldIn, shooter);
		System.out.println("[BetterArchery] EntityDrillArrow created - Config MaxBlocks: " + 
						 BetterArcheryConfig.arrows.drillArrowMaxBlocks);
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
			if (this.ticksExisted % 20 == 0) {
				System.out.println("[BetterArchery] DrillArrow onUpdate - Power: " + this.drillPower + 
								 ", BlocksDestroyed: " + this.blocksDestroyed + 
								 ", MaxBlocks: " + BetterArcheryConfig.arrows.drillArrowMaxBlocks +
								 ", PowerLoss: " + BetterArcheryConfig.arrows.drillArrowPowerLoss);
			}
			
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
					
					System.out.println("[BetterArchery] DrillArrow destroyed block - New power: " + this.drillPower + 
									 ", Blocks: " + this.blocksDestroyed);
					
					if (this.drillPower <= 0 || this.blocksDestroyed >= BetterArcheryConfig.arrows.drillArrowMaxBlocks) {
						System.out.println("[BetterArchery] DrillArrow stopping - Power: " + this.drillPower + 
										 ", Max reached: " + (this.blocksDestroyed >= BetterArcheryConfig.arrows.drillArrowMaxBlocks));
						this.setDead();
					}
				} else {
					this.setDead();
				}
			}
		}
	}
    
    private boolean canDestroyBlock(IBlockState blockState, BlockPos pos) {
        float hardness = blockState.getBlockHardness(this.world, pos);
        
        // Do NOT destroy:
        // - Blocks with a hardness of -1 (bedrock, portals, etc.)
        // - Very hard blocks (hardness >= 50, such as obsidian)
        // - If you no longer have the strength
        if (hardness < 0 || hardness >= 50.0F || this.drillPower <= 0) {
			return false;
		}
        
        // The force required to destroy it depends on the hardness of the block
        // Harder blocks require more force
        float requiredPower = hardness / 10.0F;
        return this.drillPower >= requiredPower;
    }
    
    private void destroyBlock(BlockPos pos) {
        // Destroy the block without dropping any items 
        this.world.destroyBlock(pos, false);
        this.drillPower -= (float) BetterArcheryConfig.arrows.drillArrowPowerLoss;
		this.blocksDestroyed++;
    }
    
    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        if (raytraceResultIn.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos blockPos = raytraceResultIn.getBlockPos();
            IBlockState blockState = this.world.getBlockState(blockPos);
            
            // If you don't destroy the block, do it
            if (canDestroyBlock(blockState, blockPos)) {
                destroyBlock(blockPos);
                this.drillPower -= 0.2F;
                this.blocksDestroyed++;
                
                // If you still have strength, continue (do not call super.onHit)
                if (this.drillPower > 0 && this.blocksDestroyed < MAX_BLOCKS_DESTROYED) {
                    return; // The arrow continues
                }
            }
        }
        
        // If you cannot continue, this is normal arrow behavior
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
