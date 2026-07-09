package com.onyxi7.betterarchery.handler;

import com.onyxi7.betterarchery.blocks.GunpowderBarrel;
import com.onyxi7.betterarchery.blocks.TargetBlock;
import com.onyxi7.betterarchery.tileentity.TileEntityGunpowderBarrel;
import com.onyxi7.betterarchery.tileentity.TileEntityTarget;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BarrelEventHandler {
    
    @SubscribeEvent
    public void onProjectileImpact(ProjectileImpactEvent event) {
        if (!(event.getEntity() instanceof EntityArrow)) return;
        
        RayTraceResult trace = event.getRayTraceResult();
        if (trace == null || trace.typeOfHit != RayTraceResult.Type.BLOCK) return;
        
        EntityArrow arrow = (EntityArrow) event.getEntity();
        World world = arrow.world;
        BlockPos pos = trace.getBlockPos();
        IBlockState state = world.getBlockState(pos);
        
        if (!world.isRemote) {
            // === GUNPOWDER BARREL ===
            if (state.getBlock() instanceof GunpowderBarrel) {
                TileEntity tileentity = world.getTileEntity(pos);
                if (tileentity instanceof TileEntityGunpowderBarrel) {
                    TileEntityGunpowderBarrel barrel = (TileEntityGunpowderBarrel) tileentity;
                    
                    String arrowClass = arrow.getClass().getSimpleName();
                    
                    if (arrowClass.contains("FireArrow")) {
                        barrel.triggerExplosion(4.0F, true);
                    } else if (arrowClass.contains("ImpactArrow")) {
                        barrel.triggerExplosion(6.0F, false);
                    } else {
                        barrel.triggerExplosion(2.0F, false);
                    }
                }
            }
            
            // === TARGET BLOCK ===
            if (state.getBlock() instanceof TargetBlock) {
                TileEntity tileentity = world.getTileEntity(pos);
                if (tileentity instanceof TileEntityTarget) {
                    TileEntityTarget target = (TileEntityTarget) tileentity;
                    
                    double hitX = trace.hitVec.x - pos.getX();
                    double hitZ = trace.hitVec.z - pos.getZ();
                    double distanceFromCenter = Math.sqrt(Math.pow(hitX - 0.5D, 2) + Math.pow(hitZ - 0.5D, 2));
                    
                    int signalStrength = 15 - (int) (distanceFromCenter * 15.0D);
                    signalStrength = MathHelper.clamp(signalStrength, 1, 15);
                    
                    target.setSignalStrength(signalStrength);
                    target.activate();
                }
            }
        }
    }
}
