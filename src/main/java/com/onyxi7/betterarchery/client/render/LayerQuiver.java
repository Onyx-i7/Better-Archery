package com.onyxi7.betterarchery.client.render;

import com.onyxi7.betterarchery.config.BetterArcheryConfig;
import com.onyxi7.betterarchery.init.ItemInit;
import com.onyxi7.betterarchery.items.ItemQuiverWithArrows;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerQuiver implements LayerRenderer<EntityLivingBase> {
    
    private final RenderLivingBase<?> renderEntity;
    
    public LayerQuiver(RenderLivingBase<?> renderEntity) {
        this.renderEntity = renderEntity;
    }
    
    @Override
    public void doRenderLayer(EntityLivingBase entity, float limbSwing, float limbSwingAmount, 
                             float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        
        // Verificar si el render está habilitado
        if (!BetterArcheryConfig.general.renderQuiverOnBack) {
            return;
        }
        
        // No renderizar si es invisible o está durmiendo
        if (entity.isInvisible() || entity.isPlayerSleeping()) {
            return;
        }
        
        // Si es un jugador, verificar capa
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            if (player.isWearing(EnumPlayerModelParts.CAPE) && ((AbstractClientPlayer)player).getLocationCape() != null) {
                return;
            }
        }
        
        // Buscar el carcaj
        ItemStack quiverStack = findQuiverInEntity(entity);
        
        if (quiverStack.isEmpty()) {
            return;
        }
        
        // No renderizar si el carcaj está en la mano principal
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            ItemStack heldItem = player.getHeldItemMainhand();
            if (!heldItem.isEmpty() && isQuiver(heldItem)) {
                return;
            }
        }
        
        GlStateManager.pushMatrix();
        
        // === POSICIONES DE BACKTOOLS ===
        // Posición base en la espalda
        GlStateManager.translate(0.0F, 0.35F, 0.16F);
        
        // Ajustar si tiene armadura en el pecho
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            if (!player.inventory.armorItemInSlot(2).isEmpty()) {
                GlStateManager.translate(0.0F, player.isSneaking() ? -0.1F : 0.0F, player.isSneaking() ? 0.025F : 0.06F);
            }
            
            // Ajustar si está agachado
            if (player.isSneaking()) {
                GlStateManager.translate(0.0F, 0.08F, 0.13F);
                GlStateManager.rotate(28.8F, 1.0F, 0.0F, 0.0F);
            }
        }
        
        // Rotar para que mire hacia afuera (como BackTools)
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        
        // Habilitar blend para transparencia
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        
        // === RENDERIZAR EL MODELO DEL ITEM ===
        // Escalar y posicionar el carcaj
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.6F, 0.6F, 0.6F);
        GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
        
        // Obtener el modelo del item y renderizarlo
        IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(quiverStack);
        Minecraft.getMinecraft().getRenderItem().renderItem(quiverStack, model);
        
        GlStateManager.popMatrix();
        
        // === RENDERIZAR FLECHAS SI TIENE ===
        if (quiverStack.getItem() instanceof ItemQuiverWithArrows) {
            int arrowCount = ItemQuiverWithArrows.getArrowCount(quiverStack);
            if (arrowCount > 0) {
                renderArrowsInQuiver(arrowCount);
            }
        }
        
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableAlpha();
        
        GlStateManager.popMatrix();
    }
    
    private void renderArrowsInQuiver(int arrowCount) {
        // Calcular cuántas flechas mostrar visualmente (máximo 5 para no saturar)
        int arrowsToShow = Math.min(5, (arrowCount / 10) + 1);
        
        ItemStack arrowStack = new ItemStack(Items.ARROW);
        IBakedModel arrowModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(arrowStack);
        
        GlStateManager.pushMatrix();
        
        // Posicionar las flechas saliendo del carcaj
        GlStateManager.translate(0.0F, 0.35F, 0.0F);
        GlStateManager.scale(0.3F, 0.3F, 0.3F);
        
        for (int i = 0; i < arrowsToShow; i++) {
            GlStateManager.pushMatrix();
            
            // Distribuir las flechas en un círculo
            float angle = (float)(i * 360 / arrowsToShow) * (float)Math.PI / 180.0F;
            float offsetX = (float)Math.cos(angle) * 0.15F;
            float offsetZ = (float)Math.sin(angle) * 0.15F;
            
            GlStateManager.translate(offsetX, 0.0F, offsetZ);
            GlStateManager.rotate((float)(i * 360 / arrowsToShow) + 45.0F, 0.0F, 1.0F, 0.0F);
            
            // Renderizar la flecha
            Minecraft.getMinecraft().getRenderItem().renderItem(arrowStack, arrowModel);
            
            GlStateManager.popMatrix();
        }
        
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
