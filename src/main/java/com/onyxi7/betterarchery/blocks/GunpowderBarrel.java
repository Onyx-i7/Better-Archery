package com.onyxi7.betterarchery.blocks;

import com.onyxi7.betterarchery.init.BlockInit;
import com.onyxi7.betterarchery.tileentity.TileEntityGunpowderBarrel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;

public class GunpowderBarrel extends Block {
    public GunpowderBarrel() {
        super(Material.WOOD);
        setTranslationKey("gunpowder_barrel");
        setRegistryName("gunpowder_barrel");
        setHardness(1.5F);
        setResistance(2.0F);
        setSoundType(SoundType.WOOD);
        setCreativeTab(com.onyxi7.betterarchery.init.CreativeTabInit.BETTER_ARCHERY_TAB);
        setLightLevel(0.0F);
        
        // Register TileEntity
        GameRegistry.registerTileEntity(TileEntityGunpowderBarrel.class, "gunpowder_barrel_tileentity");
    }
    
    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }
    
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityGunpowderBarrel();
    }
    
    @Override
    public boolean isFullCube(IBlockState state) {
        return true;
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return true;
    }
    
    // Method for the arrows to trigger the barrel
    @Override
    public void onProjectileHit(World worldIn, BlockPos pos, IBlockState state, RayTraceResult trace) {
        if (!worldIn.isRemote && trace.entityHit instanceof EntityArrow) {
            EntityArrow arrow = (EntityArrow) trace.entityHit;
            TileEntity tileentity = worldIn.getTileEntity(pos);
            
            if (tileentity instanceof TileEntityGunpowderBarrel) {
                TileEntityGunpowderBarrel barrel = (TileEntityGunpowderBarrel) tileentity;
                
                // Detect arrow type and explode
                String arrowType = arrow.getClass().getSimpleName();
                
                if (arrowType.contains("FireArrow")) {
                    // Fire Arrow: 4.0F + sets on fire
                    barrel.triggerExplosion(4.0F, true);
                } else if (arrowType.contains("ImpactArrow")) {
                    // Impact Arrow: 6.0F + area damage
                    barrel.triggerExplosion(6.0F, false);
                } else if (arrow instanceof EntityArrow) {
                    // Normal Arrow: 2.0F
                    barrel.triggerExplosion(2.0F, false);
                }
            }
        }
    }
    
    // Method for Detecting Direct Fire
    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        checkForFire(worldIn, pos);
    }
    
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        checkForFire(worldIn, pos);
    }
    
    private void checkForFire(World worldIn, BlockPos pos) {
        if (!worldIn.isRemote) {
            // Check to see if there is a fire or lava nearby
            if (worldIn.getBlockState(pos.up()).getBlock() == Blocks.FIRE ||
                worldIn.getBlockState(pos.up()).getBlock() == Blocks.LAVA ||
                worldIn.getBlockState(pos.up()).getBlock() == Blocks.FLOWING_LAVA) {
                
                TileEntity tileentity = worldIn.getTileEntity(pos);
                if (tileentity instanceof TileEntityGunpowderBarrel) {
                    TileEntityGunpowderBarrel barrel = (TileEntityGunpowderBarrel) tileentity;
                    // Explode after 2 seconds (40 ticks)
                    barrel.scheduleExplosion(3.0F, 40);
                }
            }
        }
    }
    
    // Method for Extracting Oil from a Barrel
    public void explode(World worldIn, BlockPos pos, float power, boolean setFire) {
        if (!worldIn.isRemote) {
            // Create an explosion
            Explosion explosion = worldIn.createExplosion(
                null,
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5,
                power,
                setFire
            );
            
            // Play an explosion sound
            worldIn.playSound(
                null,
                pos,
                SoundEvents.ENTITY_GENERIC_EXPLODE,
                SoundCategory.BLOCKS,
                4.0F,
                (1.0F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.2F) * 0.7F
            );
            
            // Destroy the block
            worldIn.setBlockToAir(pos);
            
            // Chain of explosions: Check nearby barrels
            checkNearbyBarrels(worldIn, pos, power);
        }
    }
    
    private void checkNearbyBarrels(World worldIn, BlockPos pos, float power) {
        // Chain radius: 5 blocks
        int radius = 5;
        
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos nearbyPos = pos.add(x, y, z);
                    IBlockState nearbyState = worldIn.getBlockState(nearbyPos);
                    
                    if (nearbyState.getBlock() instanceof GunpowderBarrel) {
                        TileEntity nearbyTile = worldIn.getTileEntity(nearbyPos);
                        if (nearbyTile instanceof TileEntityGunpowderBarrel) {
                            TileEntityGunpowderBarrel nearbyBarrel = (TileEntityGunpowderBarrel) nearbyTile;
                            // Blow up the nearby barrel with the same force
                            nearbyBarrel.triggerExplosion(power, false);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
}
