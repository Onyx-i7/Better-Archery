package com.onyxi7.betterarchery.client.render;

import com.onyxi7.betterarchery.config.BetterArcheryConfig;
import com.onyxi7.betterarchery.init.ItemInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class LayerQuiver implements LayerRenderer<EntityLivingBase> {
    
    private final RenderLivingBase<?> renderEntity;
    
    public LayerQuiver(RenderLivingBase<?> renderEntity) {
        this.renderEntity = renderEntity;
    }
    
    @Override
    public void doRenderLayer(EntityLivingBase entity, float limbSwing, float limbSwingAmount, 
                             float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        
        if (!BetterArcheryConfig.general.renderQuiverOnBack) {
            return;
        }
        
        if (!(entity instanceof EntityPlayer)) {
            return;
        }
        
        EntityPlayer player = (EntityPlayer) entity;
        
        // Check if player is wearing cape, invisible, or sleeping
        if ((player.isWearing(EnumPlayerModelParts.CAPE) && ((AbstractClientPlayer)player).getLocationCape() != null) 
            || player.isInvisible() || player.isPlayerSleeping()) {
            return;
        }
        
        // Find quiver in inventory
        ItemStack quiverStack = findQuiverInInventory(player);
        
        if (quiverStack.isEmpty()) {
            return;
        }
        
        // Don't render if quiver is in main hand
        ItemStack heldItem = player.getHeldItemMainhand();
        if (!heldItem.isEmpty() && isQuiver(heldItem)) {
            return;
        }
        
        GlStateManager.pushMatrix();
        
        // === BACKTOOLS COPY & PASTE ===
        GlStateManager.translate(0.0F, 0.35F, 0.16F);
        
        // Adjust for armor
        if (!player.inventory.armorItemInSlot(2).isEmpty()) {
            GlStateManager.translate(0.0F, player.isSneaking() ? -0.1F : 0.0F, player.isSneaking() ? 0.025F : 0.06F);
        }
        
        // Adjust for sneaking
        if (player.isSneaking()) {
            GlStateManager.translate(0.0F, 0.08F, 0.13F);
            GlStateManager.rotate(28.8F, 1.0F, 0.0F, 0.0F);
        }
        
        // Quiver orientation (0 = default orientation)
        GlStateManager.rotate((float)(0 - 1) * -90F, 0.0F, 0.0F, 1.0F);
        
        // Face backwards
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        
        // Enable blend for transparency
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        // Get and render the model
        IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(quiverStack);
        
        // Render the quiver
        Minecraft.getMinecraft().getRenderItem().renderItem(quiverStack, model);
        
        // Restore state
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableAlpha();
        
        GlStateManager.popMatrix();
    }
    
    private ItemStack findQuiverInInventory(EntityPlayer player) {
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (isQuiver(stack)) {
                return stack;
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
