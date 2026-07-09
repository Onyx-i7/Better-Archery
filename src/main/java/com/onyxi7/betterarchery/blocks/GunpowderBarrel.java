package com.onyxi7.betterarchery.blocks;

import com.onyxi7.betterarchery.tileentity.TileEntityGunpowderBarrel;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class GunpowderBarrel extends Block {
    
    public GunpowderBarrel() {
        super(Material.WOOD);
        setTranslationKey("gunpowder_barrel");
        setRegistryName("gunpowder_barrel");
        setHardness(1.5F);
        setResistance(2.0F);
        setSoundType(SoundType.WOOD);
        setCreativeTab(com.onyxi7.betterarchery.init.CreativeTabInit.BETTER_ARCHERY_TAB);
        
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
    
    // Detect fire/lava
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
            if (worldIn.getBlockState(pos.up()).getBlock() == Blocks.FIRE ||
                worldIn.getBlockState(pos.up()).getBlock() == Blocks.LAVA ||
                worldIn.getBlockState(pos.up()).getBlock() == Blocks.FLOWING_LAVA) {
                
                TileEntity tileentity = worldIn.getTileEntity(pos);
                if (tileentity instanceof TileEntityGunpowderBarrel) {
                    TileEntityGunpowderBarrel barrel = (TileEntityGunpowderBarrel) tileentity;
                    barrel.scheduleExplosion(3.0F, 40);
                }
            }
        }
    }
    
    // Public method for exploitation
    public void explode(World worldIn, BlockPos pos, float power, boolean setFire) {
        if (!worldIn.isRemote) {
            worldIn.createExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, power, setFire);
            
            worldIn.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 
                4.0F, (1.0F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.2F) * 0.7F);
            
            worldIn.setBlockToAir(pos);
            checkNearbyBarrels(worldIn, pos, power);
        }
    }
    
    private void checkNearbyBarrels(World worldIn, BlockPos pos, float power) {
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
                            nearbyBarrel.triggerExplosion(power, false);
                        }
                    }
                }
            }
        }
    }
}
