package com.onyxi7.betterarchery.entities;

import com.onyxi7.betterarchery.init.ItemInit;
import com.onyxi7.betterarchery.items.ItemQuiverWithArrows;
import com.onyxi7.betterarchery.config.BetterArcheryConfig;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityQuiverSkeleton extends EntitySkeleton {
    
    public EntityQuiverSkeleton(World worldIn) {
        super(worldIn);
    }
    
    @Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		livingdata = super.onInitialSpawn(difficulty, livingdata);
		
		if (!BetterArcheryConfig.mobs.enableQuiverSkeleton) {
			return livingdata;
		}
		
		ItemStack quiverStack = new ItemStack(ItemInit.QUIVER_WITH_ARROWS);
		NBTTagCompound nbt = new NBTTagCompound();
		
		int arrowCount = this.rand.nextInt(
			BetterArcheryConfig.mobs.maxArrows - BetterArcheryConfig.mobs.minArrows + 1
		) + BetterArcheryConfig.mobs.minArrows;
		
		nbt.setInteger("Arrows", arrowCount);
		quiverStack.setTagCompound(nbt);
		
		this.setItemStackToSlot(EntityEquipmentSlot.CHEST, quiverStack);
		
		if (this.rand.nextFloat() < BetterArcheryConfig.mobs.specialBowChance) {
			int bowType = this.rand.nextInt(4);
			ItemStack specialBow;
			
			switch (bowType) {
				case 0:
					specialBow = new ItemStack(ItemInit.LONG_BOW);
					break;
				case 1:
					specialBow = new ItemStack(ItemInit.RECURVE_BOW);
					break;
				case 2:
					specialBow = new ItemStack(ItemInit.COMPOSITE_BOW);
					break;
				case 3:
					specialBow = new ItemStack(ItemInit.YUMI_BOW);
					break;
				default:
					specialBow = new ItemStack(net.minecraft.init.Items.BOW);
			}
			
			this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, specialBow);
		}
		
		return livingdata;
	}
    
    @Override
    protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
        super.dropLoot(wasRecentlyHit, lootingModifier, source);
        
        ItemStack chestItem = this.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (!chestItem.isEmpty() && chestItem.getItem() instanceof ItemQuiverWithArrows) {
            this.entityDropItem(chestItem, 0.0F);
        }
    }
}
