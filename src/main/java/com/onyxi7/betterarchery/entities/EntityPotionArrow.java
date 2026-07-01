package com.onyxi7.betterarchery.entities;

import java.util.List;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class EntityPotionArrow extends EntityArrow {
    
    private String potionType = "minecraft:empty";
    
    public EntityPotionArrow(World worldIn) {
        super(worldIn);
    }
    
    public EntityPotionArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }
    
    public EntityPotionArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }
    
    public void setPotionType(String type) {
        this.potionType = type;
    }
    
    public String getPotionType() {
        return this.potionType;
    }
    
    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(Items.ARROW);
    }
    
    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        super.onHit(raytraceResultIn);
        
        if (!this.world.isRemote) {
            if (raytraceResultIn.entityHit != null) {
                // Aplicar efecto de poción a la entidad
                if (raytraceResultIn.entityHit instanceof EntityLivingBase) {
                    EntityLivingBase target = (EntityLivingBase) raytraceResultIn.entityHit;
                    applyPotionEffects(target);
                }
            }
        }
    }
    
    private void applyPotionEffects(EntityLivingBase target) {
        PotionType type = ForgeRegistries.POTION_TYPES.getValue(
            new ResourceLocation(this.potionType)
        );
        
        if (type != null) {
            List<PotionEffect> effects = type.getEffects();
            for (PotionEffect effect : effects) {
                // Aplicar el efecto con duracion reducida (50 porciento del original)
                PotionEffect reducedEffect = new PotionEffect(
                    effect.getPotion(),
                    effect.getDuration() / 2,
                    effect.getAmplifier(),
                    effect.getIsAmbient(),
                    effect.doesShowParticles()
                );
                target.addPotionEffect(reducedEffect);
            }
        }
    }
    
    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setString("PotionType", this.potionType);
    }
    
    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("PotionType")) {
            this.potionType = compound.getString("PotionType");
        }
    }
}
