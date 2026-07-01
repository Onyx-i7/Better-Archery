package com.onyxi7.betterarchery.init;

import com.onyxi7.betterarchery.entities.*;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class DispenserInit {
    
    public static void init() {
        
        // Fire Arrow
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ItemInit.FIRE_ARROW, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(World worldIn, IBlockSource source) {
                EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockState());
                double x = source.getX() + (double) enumfacing.getXOffset() * 1.125F;
                double y = source.getY() + (double) enumfacing.getYOffset() * 1.125F;
                double z = source.getZ() + (double) enumfacing.getZOffset() * 1.125F;
                EntityFireArrow arrow = new EntityFireArrow(worldIn, x, y, z);
                arrow.pickupStatus = net.minecraft.entity.projectile.EntityArrow.PickupStatus.DISALLOWED;
                return arrow;
            }
        });
        
        // Torch Arrow
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ItemInit.TORCH_ARROW, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(World worldIn, IBlockSource source) {
                EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockState());
                double x = source.getX() + (double) enumfacing.getXOffset() * 1.125F;
                double y = source.getY() + (double) enumfacing.getYOffset() * 1.125F;
                double z = source.getZ() + (double) enumfacing.getZOffset() * 1.125F;
                EntityTorchArrow arrow = new EntityTorchArrow(worldIn, x, y, z);
                arrow.pickupStatus = net.minecraft.entity.projectile.EntityArrow.PickupStatus.DISALLOWED;
                return arrow;
            }
        });
        
        // Impact Arrow
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ItemInit.IMPACT_ARROW, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(World worldIn, IBlockSource source) {
                EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockState());
                double x = source.getX() + (double) enumfacing.getXOffset() * 1.125F;
                double y = source.getY() + (double) enumfacing.getYOffset() * 1.125F;
                double z = source.getZ() + (double) enumfacing.getZOffset() * 1.125F;
                EntityImpactArrow arrow = new EntityImpactArrow(worldIn, x, y, z);
                arrow.pickupStatus = net.minecraft.entity.projectile.EntityArrow.PickupStatus.DISALLOWED;
                return arrow;
            }
        });
        
        // Ender Arrow
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ItemInit.ENDER_ARROW, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(World worldIn, IBlockSource source) {
                EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockState());
                double x = source.getX() + (double) enumfacing.getXOffset() * 1.125F;
                double y = source.getY() + (double) enumfacing.getYOffset() * 1.125F;
                double z = source.getZ() + (double) enumfacing.getZOffset() * 1.125F;
                EntityEnderArrow arrow = new EntityEnderArrow(worldIn, x, y, z);
                arrow.pickupStatus = net.minecraft.entity.projectile.EntityArrow.PickupStatus.DISALLOWED;
                return arrow;
            }
        });
        
        // Splitting Arrow
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ItemInit.SPLITTING_ARROW, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(World worldIn, IBlockSource source) {
                EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockState());
                double x = source.getX() + (double) enumfacing.getXOffset() * 1.125F;
                double y = source.getY() + (double) enumfacing.getYOffset() * 1.125F;
                double z = source.getZ() + (double) enumfacing.getZOffset() * 1.125F;
                EntitySplittingArrow arrow = new EntitySplittingArrow(worldIn, x, y, z);
                arrow.pickupStatus = net.minecraft.entity.projectile.EntityArrow.PickupStatus.DISALLOWED;
                return arrow;
            }
        });
        
        // Drill Arrow
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ItemInit.DRILL_ARROW, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(World worldIn, IBlockSource source) {
                EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockState());
                double x = source.getX() + (double) enumfacing.getXOffset() * 1.125F;
                double y = source.getY() + (double) enumfacing.getYOffset() * 1.125F;
                double z = source.getZ() + (double) enumfacing.getZOffset() * 1.125F;
                EntityDrillArrow arrow = new EntityDrillArrow(worldIn, x, y, z);
                arrow.pickupStatus = net.minecraft.entity.projectile.EntityArrow.PickupStatus.DISALLOWED;
                return arrow;
            }
        });
        
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ItemInit.POTION_ARROW, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(World worldIn, IBlockSource source) {
                EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockState());
                double x = source.getX() + (double) enumfacing.getXOffset() * 1.125F;
                double y = source.getY() + (double) enumfacing.getYOffset() * 1.125F;
                double z = source.getZ() + (double) enumfacing.getZOffset() * 1.125F;
                EntityPotionArrow arrow = new EntityPotionArrow(worldIn, x, y, z);
                
                ItemStack stack = source.getBlockTileEntity().getStackInSlot(0);
                if (!stack.isEmpty()) {
                    String potionType = com.onyxi7.betterarchery.items.arrows.ItemPotionArrow.getPotionType(stack);
                    arrow.setPotionType(potionType);
                }
                
                arrow.pickupStatus = net.minecraft.entity.projectile.EntityArrow.PickupStatus.DISALLOWED;
                return arrow;
            }
        });
    }
}
