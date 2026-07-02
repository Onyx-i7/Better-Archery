package com.onyxi7.betterarchery.entities;

import com.onyxi7.betterarchery.init.ItemInit;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityPotionArrow extends EntityCustomArrow {
    
    private PotionType potionType;
    
    public EntityPotionArrow(World worldIn) {
        super(worldIn);
    }
    
    public EntityPotionArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }
    
    public EntityPotionArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }
    
    public void setPotionType(PotionType type) {
        this.potionType = type;
    }
    
    @Override
    protected void arrowHit(EntityLivingBase living) {
        super.arrowHit(living);
        
        if (potionType != null && living != this.shootingEntity) {
            for (PotionEffect potioneffect : potionType.getEffects()) {
                living.addPotionEffect(new PotionEffect(potioneffect));
            }
        }
    }
    
    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(ItemInit.POTION_ARROW);
    }
}
