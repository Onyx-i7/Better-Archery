package com.onyxi7.betterarchery.entities;

import com.onyxi7.betterarchery.init.EnchantmentInit;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public abstract class EntityCustomArrow extends EntityArrow {
    
    protected int homingTicks = 0;
    protected static final int MAX_HOMING_TICKS = 40; // 2 seconds
    
    public EntityCustomArrow(World worldIn) {
        super(worldIn);
    }
    
    public EntityCustomArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }
    
    public EntityCustomArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        
        // Apply homing if enchantment is present
        if (!this.world.isRemote && this.shootingEntity instanceof EntityLivingBase) {
            EntityLivingBase shooter = (EntityLivingBase) this.shootingEntity;
            ItemStack bowStack = shooter.getHeldItemMainhand();
            
            if (bowStack.isEmpty() || !(bowStack.getItem() instanceof ItemBow)) {
                bowStack = shooter.getHeldItemOffhand();
            }
            
            if (!bowStack.isEmpty()) {
                int homingLevel = EnchantmentHelper.getEnchantmentLevel(EnchantmentInit.HOMING, bowStack);
                
                if (homingLevel > 0 && homingTicks < MAX_HOMING_TICKS) {
                    applyHoming(homingLevel);
                    homingTicks++;
                }
            }
        }
    }
    
    protected void applyHoming(int level) {
        // Detection radius based on level: 8, 12, 16 blocks
        double radius = 8.0D + (level * 4.0D);
        
        // Get all entities in radius
        List<Entity> allEntities = this.world.getEntitiesWithinAABBExcludingEntity(
            this,
            new AxisAlignedBB(
                this.posX - radius, this.posY - radius, this.posZ - radius,
                this.posX + radius, this.posY + radius, this.posZ + radius
            )
        );
        
        EntityLivingBase target = null;
        double minDistance = Double.MAX_VALUE;
        
        for (Entity entity : allEntities) {
            // Filter: must be living entity and not the shooter
            if (!(entity instanceof EntityLivingBase)) continue;
            if (entity == this.shootingEntity) continue;
            
            EntityLivingBase living = (EntityLivingBase) entity;
            
            // Don't target dead entities
            if (living.getHealth() <= 0 || living.isDead) {
                continue;
            }
            
            // Don't target player's tamed pets
            if (this.shootingEntity instanceof EntityPlayer) {
                if (living instanceof EntityTameable) {
                    EntityTameable tameable = (EntityTameable) living;
                    if (tameable.isTamed() && tameable.getOwner() == this.shootingEntity) {
                        continue;
                    }
                }
            }
            
            double distance = this.getDistance(living);
            if (distance < minDistance) {
                minDistance = distance;
                target = living;
            }
        }
        
        // If target found, adjust trajectory
        if (target != null) {
            Vec3d targetPos = new Vec3d(
                target.posX,
                target.posY + target.height / 2.0D,
                target.posZ
            );
            
            Vec3d arrowPos = new Vec3d(this.posX, this.posY, this.posZ);
            Vec3d direction = targetPos.subtract(arrowPos).normalize();
            
            // Get current velocity
            Vec3d currentVelocity = new Vec3d(this.motionX, this.motionY, this.motionZ);
            double speed = currentVelocity.length();
            
            // Steering strength based on level: 0.05, 0.08, 0.12
            float steeringStrength = 0.05F + (level * 0.03F);
            
            // Interpolate between current direction and target direction
            Vec3d newDirection = currentVelocity.normalize()
                .scale(1.0D - steeringStrength)
                .add(direction.scale(steeringStrength))
                .normalize();
            
            // Apply new velocity
            this.motionX = newDirection.x * speed;
            this.motionY = newDirection.y * speed;
            this.motionZ = newDirection.z * speed;
            
            // Update rotation to match new direction
            float yaw = (float) (MathHelper.atan2(newDirection.z, newDirection.x) * (180D / Math.PI)) - 90.0F;
            float pitch = (float) (-(MathHelper.atan2(newDirection.y, MathHelper.sqrt(newDirection.x * newDirection.x + newDirection.z * newDirection.z)) * (180D / Math.PI)));
            
            this.rotationYaw = yaw;
            this.rotationPitch = pitch;
        }
    }
}
