package com.onyxi7.betterarchery.client.render;

import com.onyxi7.betterarchery.config.BetterArcheryConfig;
import com.onyxi7.betterarchery.init.ItemInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class LayerQuiver implements LayerRenderer<EntityLivingBase> {
    
    private final RenderLivingBase<?> renderEntity;
    private static final ResourceLocation QUIVER_TEXTURE = new ResourceLocation("betterarchery:textures/entity/quiver.png");
    
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
        
        GlStateManager.translate(0.0F, 0.35F, 0.16F);
        
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            if (!player.inventory.armorItemInSlot(2).isEmpty()) {
                GlStateManager.translate(0.0F, player.isSneaking() ? -0.1F : 0.0F, player.isSneaking() ? 0.025F : 0.06F);
            }
            
            if (player.isSneaking()) {
                GlStateManager.translate(0.0F, 0.08F, 0.13F);
                GlStateManager.rotate(28.8F, 1.0F, 0.0F, 0.0F);
            }
        }
        
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        renderQuiverModel(quiverStack);
        
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableAlpha();
        
        GlStateManager.popMatrix();
    }
    
    private void renderQuiverModel(ItemStack stack) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5F, 0.8F, 0.3F);
        
        Minecraft.getMinecraft().renderEngine.bindTexture(QUIVER_TEXTURE);
        
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        
        drawQuiverBox();
        
        GlStateManager.popMatrix();
    }
    
    private void drawQuiverBox() {
        GlStateManager.pushMatrix();
        
        GL11.glBegin(GL11.GL_QUADS);
        
        GL11.glNormal3f(0.0F, 0.0F, 1.0F);
        GL11.glTexCoord2f(0.0F, 0.0F); GL11.glVertex3f(-0.2F, -0.4F, 0.15F);
        GL11.glTexCoord2f(1.0F, 0.0F); GL11.glVertex3f(0.2F, -0.4F, 0.15F);
        GL11.glTexCoord2f(1.0F, 1.0F); GL11.glVertex3f(0.2F, 0.4F, 0.15F);
        GL11.glTexCoord2f(0.0F, 1.0F); GL11.glVertex3f(-0.2F, 0.4F, 0.15F);
        
        GL11.glNormal3f(0.0F, 0.0F, -1.0F);
        GL11.glTexCoord2f(0.0F, 0.0F); GL11.glVertex3f(-0.2F, -0.4F, -0.15F);
        GL11.glTexCoord2f(0.0F, 1.0F); GL11.glVertex3f(-0.2F, 0.4F, -0.15F);
        GL11.glTexCoord2f(1.0F, 1.0F); GL11.glVertex3f(0.2F, 0.4F, -0.15F);
        GL11.glTexCoord2f(1.0F, 0.0F); GL11.glVertex3f(0.2F, -0.4F, -0.15F);
        
        GL11.glNormal3f(-1.0F, 0.0F, 0.0F);
        GL11.glTexCoord2f(0.0F, 0.0F); GL11.glVertex3f(-0.2F, -0.4F, -0.15F);
        GL11.glTexCoord2f(1.0F, 0.0F); GL11.glVertex3f(-0.2F, -0.4F, 0.15F);
        GL11.glTexCoord2f(1.0F, 1.0F); GL11.glVertex3f(-0.2F, 0.4F, 0.15F);
        GL11.glTexCoord2f(0.0F, 1.0F); GL11.glVertex3f(-0.2F, 0.4F, -0.15F);
        
        GL11.glNormal3f(1.0F, 0.0F, 0.0F);
        GL11.glTexCoord2f(0.0F, 0.0F); GL11.glVertex3f(0.2F, -0.4F, -0.15F);
        GL11.glTexCoord2f(0.0F, 1.0F); GL11.glVertex3f(0.2F, 0.4F, -0.15F);
        GL11.glTexCoord2f(1.0F, 1.0F); GL11.glVertex3f(0.2F, 0.4F, 0.15F);
        GL11.glTexCoord2f(1.0F, 0.0F); GL11.glVertex3f(0.2F, -0.4F, 0.15F);
        
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glTexCoord2f(0.0F, 0.0F); GL11.glVertex3f(-0.2F, 0.4F, -0.15F);
        GL11.glTexCoord2f(0.0F, 1.0F); GL11.glVertex3f(-0.2F, 0.4F, 0.15F);
        GL11.glTexCoord2f(1.0F, 1.0F); GL11.glVertex3f(0.2F, 0.4F, 0.15F);
        GL11.glTexCoord2f(1.0F, 0.0F); GL11.glVertex3f(0.2F, 0.4F, -0.15F);
        
        GL11.glNormal3f(0.0F, -1.0F, 0.0F);
        GL11.glTexCoord2f(0.0F, 0.0F); GL11.glVertex3f(-0.2F, -0.4F, -0.15F);
        GL11.glTexCoord2f(1.0F, 0.0F); GL11.glVertex3f(0.2F, -0.4F, -0.15F);
        GL11.glTexCoord2f(1.0F, 1.0F); GL11.glVertex3f(0.2F, -0.4F, 0.15F);
        GL11.glTexCoord2f(0.0F, 1.0F); GL11.glVertex3f(-0.2F, -0.4F, 0.15F);
        
        GL11.glEnd();
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
            ItemStack chestItem = entity.getItemStackFromSlot(net.minecraft.inventory.EntityEquipmentSlot.CHEST);
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
