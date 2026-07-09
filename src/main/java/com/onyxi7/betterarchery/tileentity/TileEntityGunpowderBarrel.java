package com.onyxi7.betterarchery.tileentity;

import com.onyxi7.betterarchery.blocks.GunpowderBarrel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityGunpowderBarrel extends TileEntity implements ITickable {
    
    private int explosionTimer = 0;
    private float explosionPower = 0;
    private boolean shouldExplode = false;
    private boolean setFire = false;
    
    @Override
    public void update() {
        if (shouldExplode && explosionTimer > 0) {
            explosionTimer--;
            
            if (explosionTimer <= 0) {
                // To explode
                if (world != null && !world.isRemote) {
                    GunpowderBarrel barrel = (GunpowderBarrel) world.getBlockState(pos).getBlock();
                    barrel.explode(world, pos, explosionPower, setFire);
                }
            }
        }
    }
    
    // Instant Explosion (from arrows)
    public void triggerExplosion(float power, boolean setFire) {
        if (world != null && !world.isRemote) {
            GunpowderBarrel barrel = (GunpowderBarrel) world.getBlockState(pos).getBlock();
            barrel.explode(world, pos, power, setFire);
        }
    }
    
    // Scheduled explosion (by fire/lava)
    public void scheduleExplosion(float power, int ticks) {
        this.explosionPower = power;
        this.explosionTimer = ticks;
        this.shouldExplode = true;
        this.setFire = false;
        markDirty();
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("ExplosionTimer", explosionTimer);
        compound.setFloat("ExplosionPower", explosionPower);
        compound.setBoolean("ShouldExplode", shouldExplode);
        compound.setBoolean("SetFire", setFire);
        return compound;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.explosionTimer = compound.getInteger("ExplosionTimer");
        this.explosionPower = compound.getFloat("ExplosionPower");
        this.shouldExplode = compound.getBoolean("ShouldExplode");
        this.setFire = compound.getBoolean("SetFire");
    }
    
    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }
    
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        this.writeToNBT(nbtTagCompound);
        return new SPacketUpdateTileEntity(this.pos, 1, nbtTagCompound);
    }
    
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
    }
}
