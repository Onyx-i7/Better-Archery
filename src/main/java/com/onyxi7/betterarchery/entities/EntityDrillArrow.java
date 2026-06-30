package com.onyxi7.betterarchery.entities;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityDrillArrow extends EntityArrow {
    
    private int ticksInGround;
    private boolean inGround;
    
    public EntityDrillArrow(World worldIn) {
        super(worldIn);
    }
    
    public EntityDrillArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }
    
    public EntityDrillArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }
    
    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(Items.ARROW);
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        
        if (this.inGround) {
            this.ticksInGround++;
        } else {
            this.ticksInGround = 0;
        }
        
        // Si está en el aire, verificar colisiones con bloques
        if (!this.inGround && !this.world.isRemote) {
            // Obtener la posición actual y la siguiente
            Vec3d currentPos = new Vec3d(this.posX, this.posY, this.posZ);
            Vec3d motion = new Vec3d(this.motionX, this.motionY, this.motionZ);
            Vec3d nextPos = currentPos.add(motion);
            
            // Raytrace para detectar bloques en el camino
            RayTraceResult rayTrace = this.world.rayTraceBlocks(currentPos, nextPos, false, true, false);
            
            if (rayTrace != null && rayTrace.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos blockPos = rayTrace.getBlockPos();
                IBlockState blockState = this.world.getBlockState(blockPos);
                
                // Si el bloque no es muy duro, destruirlo
                if (blockState.getBlockHardness(this.world, blockPos) < 5.0F) {
                    this.world.destroyBlock(blockPos, false);
                    
                    // Reducir el daño de la flecha por cada bloque destruido
                    this.setDamage(this.getDamage() - 0.5);
                    
                    // Si el daño es muy bajo, romper la flecha
                    if (this.getDamage() <= 0) {
                        this.setDead();
                    }
                }
            }
        }
    }
    
    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        // Si golpea un bloque, no se queda clavado inmediatamente
        if (raytraceResultIn.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos blockPos = raytraceResultIn.getBlockPos();
            IBlockState blockState = this.world.getBlockState(blockPos);
            
            // Si el bloque es muy duro, quedarse clavado
            if (blockState.getBlockHardness(this.world, blockPos) >= 5.0F) {
                super.onHit(raytraceResultIn);
            }
            // Si es blando, continuar a través de él (no hacer nada)(no hacer nada)
        } else {
            // Si golpea una entidad, comportamiento normal
            super.onHit(raytraceResultIn);
        }
    }
}
