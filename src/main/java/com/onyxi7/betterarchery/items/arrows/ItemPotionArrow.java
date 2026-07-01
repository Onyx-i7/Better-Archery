package com.onyxi7.betterarchery.items.arrows;

import com.onyxi7.betterarchery.betterarchery;
import com.onyxi7.betterarchery.entities.EntityPotionArrow;
import com.onyxi7.betterarchery.init.ItemInit;
import com.onyxi7.betterarchery.util.interfaces.IHasModel;
import java.util.List;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ItemPotionArrow extends ItemArrow implements IHasModel {
    
    public ItemPotionArrow(String name) {
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(CreativeTabInit.BETTER_ARCHERY_TAB);
        setHasSubtypes(true);
        ItemInit.ITEMS.add(this);
    }
    
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            // Algunos efectos de pociones comunes
            items.add(createPotionArrowStack("minecraft:poison"));
            items.add(createPotionArrowStack("minecraft:slowness"));
            items.add(createPotionArrowStack("minecraft:weakness"));
            items.add(createPotionArrowStack("minecraft:strength"));
            items.add(createPotionArrowStack("minecraft:swiftness"));
        }
    }
    
    // Crear un stack de flecha con poción
    public static ItemStack createPotionArrowStack(String potionType) {
        ItemStack stack = new ItemStack(ItemInit.POTION_ARROW);
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("PotionType", potionType);
        stack.setTagCompound(nbt);
        return stack;
    }
    
    // Obtener el tipo de poción guardada
    public static String getPotionType(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("PotionType")) {
            return stack.getTagCompound().getString("PotionType");
        }
        return "minecraft:empty";
    }
    
    // Obtener el color de la poción
	public static int getPotionColor(ItemStack stack) {
		String potionType = getPotionType(stack);
		PotionType type = ForgeRegistries.POTION_TYPES.getValue(
			new net.minecraft.util.ResourceLocation(potionType)
		);
		
		if (type != null && !type.getEffects().isEmpty()) {
			// Calcula el color promedio de todos los efectos
			int r = 0, g = 0, b = 0;
			int count = type.getEffects().size();
			
			for (PotionEffect effect : type.getEffects()) {
				int color = effect.getPotion().getLiquidColor();
				r += (color >> 16) & 0xFF;
				g += (color >> 8) & 0xFF;
				b += color & 0xFF;
			}
			
			r /= count;
			g /= count;
			b /= count;
			
			return (r << 16) | (g << 8) | b;
		}
		
		return 0x385DC6; // Color azul por defecto
	}
    
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String potionType = getPotionType(stack);
        PotionType type = ForgeRegistries.POTION_TYPES.getValue(
            new net.minecraft.util.ResourceLocation(potionType)
        );
        
        if (type != null && !type.getEffects().isEmpty()) {
            PotionEffect effect = type.getEffects().get(0);
            String effectName = effect.getEffectName();
            return effectName + " Arrow";
        }
        
        return "Potion Arrow";
    }
    
    @Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		String potionType = getPotionType(stack);
		PotionType type = ForgeRegistries.POTION_TYPES.getValue(
			new net.minecraft.util.ResourceLocation(potionType)
		);
		
		if (type != null && !type.getEffects().isEmpty()) {
			for (PotionEffect effect : type.getEffects()) {
				String effectName = net.minecraft.util.text.translation.I18n.translateToLocal(effect.getEffectName()).trim();
				tooltip.add(effectName);
			}
		}
	}
    
    @Override
    public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
        EntityPotionArrow arrow = new EntityPotionArrow(worldIn, shooter);
        
        // Guardar el tipo de poción en la entidad
        String potionType = getPotionType(stack);
        arrow.setPotionType(potionType);
        
        return arrow;
    }
    
    @Override
    public boolean isInfinite(ItemStack stack, ItemStack bow, EntityPlayer player) {
        // Las flechas de poción NO son infinitas obviamente
        return false;
    }
    
    @Override
    public void registerModels() {
        betterarchery.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
