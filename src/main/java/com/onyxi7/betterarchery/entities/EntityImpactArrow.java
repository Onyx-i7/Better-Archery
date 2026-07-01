package com.onyxi7.betterarchery.entities;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityImpactArrow extends EntityArrow {
    
    private static final float EXPLOSION_RADIUS = 2.0F;
    
    public EntityImpactArrow(World worldIn) {
        super(worldIn);
    }
    
    public EntityImpactArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }
    
    public EntityImpactArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }
    
    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(Items.ARROW);
    }
    
    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        super.onHit(raytraceResultIn);
        
        if (!this.world.isRemote) {
            // Crear una explosión
            Vec3d pos = new Vec3d(this.posX, this.posY, this.posZ);
            
            // Dañar entidades cercanas
            List<EntityLivingBase> entities = this.world.getEntitiesWithinAABB(
                EntityLivingBase.class,
                new AxisAlignedBB(
                    pos.x - EXPLOSION_RADIUS, pos.y - EXPLOSION_RADIUS, pos.z - EXPLOSION_RADIUS,
                    pos.x + EXPLOSION_RADIUS, pos.y + EXPLOSION_RADIUS, pos.z + EXPLOSION_RADIUS
                )
            );
            
            for (EntityLivingBase entity : entities) {
                if (entity != this.shootingEntity) {
                    double distance = entity.getDistance(pos.x, pos.y, pos.z);
                    float damage = (float) (6.0F * (1.0 - distance / EXPLOSION_RADIUS));
                    if (damage > 0) {
                        entity.attackEntityFrom(DamageSource.causeIndirectDamage(this, this.shootingEntity), damage);
                        // Knockback
                        double dx = entity.posX - pos.x;
                        double dy = entity.posY - pos.y;
                        double dz = entity.posZ - pos.z;
                        double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
                        if (len > 0) {
                            entity.motionX += (dx / len) * 0.5;
                            entity.motionY += (dy / len) * 0.3;
                            entity.motionZ += (dz / len) * 0.5;
                        }
                    }
                }
            }
            
            // Partículas de explosión
            this.world.newExplosion(this, this.posX, this.posY, this.posZ, 0.5F, false, false);
            
            this.setDead();
        }
    }
}
