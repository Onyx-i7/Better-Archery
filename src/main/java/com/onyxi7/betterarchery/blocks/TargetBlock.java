package com.onyxi7.betterarchery.blocks;

import com.onyxi7.betterarchery.init.BlockInit;
import com.onyxi7.betterarchery.tileentity.TileEntityTarget;
import net.minecraft.block.IBlockState;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.init.SoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.block.property.PropertyDirection;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;

public class TargetBlock extends Block {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    public TargetBlock() {
        super(Material.ROCK);
        setTranslationKey("target_block");
        setRegistryName("target_block");
        setHardness(0.5F);
        setSoundType(SoundType.STONE);
        setCreativeTab(com.onyxi7.betterarchery.init.CreativeTabInit.BETTER_ARCHERY_TAB);

        // Register TileEntity
        GameRegistry.registerTileEntity(TileEntityTarget.class, "target_block_tileentity");
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityTarget();
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof TileEntityTarget) {
                TileEntityTarget targetTile = (TileEntityTarget) tileEntity;
                targetTile.activate();
                worldIn.playSound(null, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }
        return true;
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        TileEntity tileEntity = blockAccess.getTileEntity(pos);
        if (tileEntity instanceof TileEntityTarget) {
            TileEntityTarget targetTile = (TileEntityTarget) tileEntity;
            return targetTile.getRedstonePower();
        }
        return 0;
    }

    @Override
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return getWeakPower(blockState, blockAccess, pos, side);
    }

    @Override public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.getHorizontal(meta);
        if (facing.getAxis() == EnumFacing.Axis.Y) {
            facing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(FACING, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    public void onProjectileHit(World worldIn, BlockPos pos, EntityArrow arrow, IBlockState state, RayTraceResult trace) {
        if (!worldIn.isRemote) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof TileEntityTarget) {
                TileEntityTarget target = (TileEntityTarget) tileEntity;
                double hitX = trace.hitVec.x - pos.getX();
                double hitZ = trace.hitVec.z - pos.getZ();
                double DistanceFromCenter = Math.sqrt(Math.pow(hitX - 0.5D, 2) + Math.pow(hitZ - 0.5D, 2));
                int signalStrength = 15 - (int) (DistanceFromCenter * 15.0D);
                signalStrength = MathHelper.clamp(signalStrength, 1, 15);

                target.setRedstonePower(signalStrength);
                target.activate();

                worldIn.playSound(null, pos, SoundEvents.BLOCK_WOOD_HIT, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }
    }
}
