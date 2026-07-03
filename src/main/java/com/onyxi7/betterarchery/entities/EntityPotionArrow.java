package com.onyxi7.betterarchery.entities;

import com.onyxi7.betterarchery.init.ItemInit;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class EntityPotionArrow extends EntityCustomArrow {
    
    private String potionTypeName = "minecraft:empty";
    
    public EntityPotionArrow(World worldIn) {
        super(worldIn);
    }
    
    public EntityPotionArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }
    
    public EntityPotionArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }
    
    // Accept String parameter
    public void setPotionType(String potionType) {
        this.potionTypeName = potionType;
    }
    
    public String getPotionTypeName() {
        return this.potionTypeName;
    }
    
    @Override
    protected void arrowHit(EntityLivingBase living) {
        super.arrowHit(living);
        
        if (living != this.shootingEntity && potionTypeName != null) {
            // Get PotionType from registry
            PotionType type = ForgeRegistries.POTION_TYPES.getValue(new ResourceLocation(potionTypeName));
            
            if (type != null && type != PotionTypes.EMPTY) {
                for (PotionEffect effect : type.getEffects()) {
                    // Apply effect with 75% duration (like splash potions)
                    PotionEffect reducedEffect = new PotionEffect(
                        effect.getPotion(),
                        (int)(effect.getDuration() * 0.75),
                        effect.getAmplifier(),
                        effect.getIsAmbient(),
                        effect.doesShowParticles()
                    );
                    living.addPotionEffect(reducedEffect);
                }
            }
        }
    }
    
    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(ItemInit.POTION_ARROW);
    }
}
