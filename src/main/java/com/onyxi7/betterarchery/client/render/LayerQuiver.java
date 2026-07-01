package com.onyxi7.betterarchery.client.render;

import com.onyxi7.betterarchery.init.ItemInit;
import com.onyxi7.betterarchery.items.ItemQuiver;
import com.onyxi7.betterarchery.items.ItemQuiverWithArrows;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class LayerQuiver implements LayerRenderer<EntityLivingBase> {
    
    private static final ResourceLocation QUIVER_TEXTURE = new ResourceLocation("betterarchery:textures/wornquiver/backquiver.png");
    private final RenderLivingBase<?> renderer;
    
    public LayerQuiver(RenderLivingBase<?> rendererIn) {
        this.renderer = rendererIn;
    }
    
    @Override
    public void doRenderLayer(EntityLivingBase entity, float limbSwing, float limbSwingAmount, 
                              float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        
        if (!(entity instanceof EntityPlayer)) {
            return;
        }
        
        EntityPlayer player = (EntityPlayer) entity;
        
        // Buscar si el jugador tiene un carcaj en el inventario
        ItemStack quiverStack = findQuiverInInventory(player);
        
        if (quiverStack.isEmpty()) {
            return; // No tiene carcaj, no renderizar nada
        }
        
        // Si está en la offhand, no renderizar en la espalda
        if (player.getHeldItemMainhand() == quiverStack || player.getHeldItemOffhand() == quiverStack) {
            return;
        }
        
        GlStateManager.pushMatrix();
        
        // Renderizar en la espalda del jugador
        if (renderer.getMainModel() instanceof ModelBiped) {
            ((ModelBiped) renderer.getMainModel()).bipedBody.postRender(0.0625F);
        }
        
        // Posicionar en la espalda
        GlStateManager.translate(0.0F, 0.0F, 0.15F); // Mover hacia atrás
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F); // Rotar para que mire hacia atrás
        GlStateManager.scale(0.6F, 0.6F, 0.6F); // Hacerlo más pequeño
        GlStateManager.translate(-0.25F, 0.25F, 0.0F); // Centrar en la espalda
        
        // Renderizar el item del carcaj
        renderQuiverItem(quiverStack);
        
        GlStateManager.popMatrix();
    }
    
    private ItemStack findQuiverInInventory(EntityPlayer player) {
        for (int i = 0; i < player.inventory.armorInventory.size(); i++) {
            ItemStack stack = player.inventory.armorInventory.get(i);
            if (isQuiver(stack)) {
                return stack;
            }
        }
        
        for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
            ItemStack stack = player.inventory.mainInventory.get(i);
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
    
    private void renderQuiverItem(ItemStack stack) {
        net.minecraft.client.renderer.RenderItem itemRenderer = net.minecraft.client.Minecraft.getMinecraft().getRenderItem();
        
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        
        itemRenderer.renderItem(stack, ItemCameraTransforms.TransformType.NONE);
        
        GlStateManager.popMatrix();
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
