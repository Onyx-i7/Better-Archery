package com.onyxi7.betterarchery.client.render;

import com.onyxi7.betterarchery.config.BetterArcheryConfig;
import com.onyxi7.betterarchery.init.ItemInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerQuiver implements LayerRenderer<EntityLivingBase> {
    
    private final RenderLivingBase<?> renderEntity;
    
    private static final ModelResourceLocation QUIVER_MODEL = 
        new ModelResourceLocation("betterarchery:quiver_3d", "inventory");
    
    public LayerQuiver(RenderLivingBase<?> renderEntity) {
        this.renderEntity = renderEntity;
    }
    
    @Override
    public void doRenderLayer(EntityLivingBase entity, float limbSwing, float limbSwingAmount, 
                             float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        
        if (!BetterArcheryConfig.general.renderQuiverOnBack) {
            return;
        }
        
        if (entity.isInvisible() || entity.isPlayerSleeping()) {
            return;
        }
        
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            if (player.isWearing(EnumPlayerModelParts.CAPE) && ((AbstractClientPlayer)player).getLocationCape() != null) {
                return;
            }
        }
        
        ItemStack quiverStack = findQuiverInEntity(entity);
        
        if (quiverStack.isEmpty()) {
            return;
        }
        
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            ItemStack heldItem = player.getHeldItemMainhand();
            if (!heldItem.isEmpty() && isQuiver(heldItem)) {
                return;
            }
        }
        
        GlStateManager.pushMatrix();
        
        IBakedModel model = Minecraft.getMinecraft().getModelManager().getModel(QUIVER_MODEL);
        
        Minecraft.getMinecraft().getRenderItem().renderItem(quiverStack, model);
        
        GlStateManager.popMatrix();
    }
    
    private ItemStack findQuiverInEntity(EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if (isQuiver(stack)) {
                    return stack;
                }
            }
        } else {
            ItemStack chestItem = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            if (isQuiver(chestItem)) {
                return chestItem;
            }
        }
        return ItemStack.EMPTY;
    }
    
    private boolean isQuiver(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        Item item = stack.getItem();
        return item == ItemInit.QUIVER || item == ItemInit.QUIVER_WITH_ARROWS;
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}
