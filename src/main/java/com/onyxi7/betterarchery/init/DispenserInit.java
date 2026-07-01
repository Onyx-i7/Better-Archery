package com.onyxi7.betterarchery.init;

import com.onyxi7.betterarchery.entities.*;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class DispenserInit {
    
    public static void init() {
        // Fire Arrow
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ItemInit.FIRE_ARROW, new BehaviorDefaultDispenseItem() {
            @Override
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                EnumFacing enumfacing = source.getBlockState().getValue(BlockDispenser.FACING);
                IPosition iposition = BlockDispenser.getDispensePosition(source);
                World world = source.getWorld();
                
                EntityFireArrow arrow = new EntityFireArrow(world, iposition.getX(), iposition.getY(), iposition.getZ());
                arrow.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
                arrow.shoot(enumfacing.getXOffset(), enumfacing.getYOffset() + 0.1F, enumfacing.getZOffset(), 1.1F, 6.0F);
                world.spawnEntity(arrow);
                
                stack.shrink(1);
                return stack;
            }
        });
        
        // Torch Arrow
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ItemInit.TORCH_ARROW, new BehaviorDefaultDispenseItem() {
            @Override
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                EnumFacing enumfacing = source.getBlockState().getValue(BlockDispenser.FACING);
                IPosition iposition = BlockDispenser.getDispensePosition(source);
                World world = source.getWorld();
                
                EntityTorchArrow arrow = new EntityTorchArrow(world, iposition.getX(), iposition.getY(), iposition.getZ());
                arrow.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
                arrow.shoot(enumfacing.getXOffset(), enumfacing.getYOffset() + 0.1F, enumfacing.getZOffset(), 1.1F, 6.0F);
                world.spawnEntity(arrow);
                
                stack.shrink(1);
                return stack;
            }
        });
        
        // Impact Arrow
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ItemInit.IMPACT_ARROW, new BehaviorDefaultDispenseItem() {
            @Override
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                EnumFacing enumfacing = source.getBlockState().getValue(BlockDispenser.FACING);
                IPosition iposition = BlockDispenser.getDispensePosition(source);
                World world = source.getWorld();
                
                EntityImpactArrow arrow = new EntityImpactArrow(world, iposition.getX(), iposition.getY(), iposition.getZ());
                arrow.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
                arrow.shoot(enumfacing.getXOffset(), enumfacing.getYOffset() + 0.1F, enumfacing.getZOffset(), 1.1F, 6.0F);
                world.spawnEntity(arrow);
                
                stack.shrink(1);
                return stack;
            }
        });
        
        // Ender Arrow
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ItemInit.ENDER_ARROW, new BehaviorDefaultDispenseItem() {
            @Override
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                EnumFacing enumfacing = source.getBlockState().getValue(BlockDispenser.FACING);
                IPosition iposition = BlockDispenser.getDispensePosition(source);
                World world = source.getWorld();
                
                EntityEnderArrow arrow = new EntityEnderArrow(world, iposition.getX(), iposition.getY(), iposition.getZ());
                arrow.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
                arrow.shoot(enumfacing.getXOffset(), enumfacing.getYOffset() + 0.1F, enumfacing.getZOffset(), 1.1F, 6.0F);
                world.spawnEntity(arrow);
                
                stack.shrink(1);
                return stack;
            }
        });
        
        // Splitting Arrow
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ItemInit.SPLITTING_ARROW, new BehaviorDefaultDispenseItem() {
            @Override
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                EnumFacing enumfacing = source.getBlockState().getValue(BlockDispenser.FACING);
                IPosition iposition = BlockDispenser.getDispensePosition(source);
                World world = source.getWorld();
                
                EntitySplittingArrow arrow = new EntitySplittingArrow(world, iposition.getX(), iposition.getY(), iposition.getZ());
                arrow.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
                arrow.shoot(enumfacing.getXOffset(), enumfacing.getYOffset() + 0.1F, enumfacing.getZOffset(), 1.1F, 6.0F);
                world.spawnEntity(arrow);
                
                stack.shrink(1);
                return stack;
            }
        });
        
        // Drill Arrow
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ItemInit.DRILL_ARROW, new BehaviorDefaultDispenseItem() {
            @Override
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                EnumFacing enumfacing = source.getBlockState().getValue(BlockDispenser.FACING);
                IPosition iposition = BlockDispenser.getDispensePosition(source);
                World world = source.getWorld();
                
                EntityDrillArrow arrow = new EntityDrillArrow(world, iposition.getX(), iposition.getY(), iposition.getZ());
                arrow.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
                arrow.shoot(enumfacing.getXOffset(), enumfacing.getYOffset() + 0.1F, enumfacing.getZOffset(), 1.1F, 6.0F);
                world.spawnEntity(arrow);
                
                stack.shrink(1);
                return stack;
            }
        });
        
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ItemInit.POTION_ARROW, new BehaviorDefaultDispenseItem() {
            @Override
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                EnumFacing enumfacing = source.getBlockState().getValue(BlockDispenser.FACING);
                IPosition iposition = BlockDispenser.getDispensePosition(source);
                World world = source.getWorld();
                
                EntityPotionArrow arrow = new EntityPotionArrow(world, iposition.getX(), iposition.getY(), iposition.getZ());
                
                String potionType = com.onyxi7.betterarchery.items.arrows.ItemPotionArrow.getPotionType(stack);
                arrow.setPotionType(potionType);
                
                arrow.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
                arrow.shoot(enumfacing.getXOffset(), enumfacing.getYOffset() + 0.1F, enumfacing.getZOffset(), 1.1F, 6.0F);
                world.spawnEntity(arrow);
                
                stack.shrink(1);
                return stack;
            }
        });
    }
}
