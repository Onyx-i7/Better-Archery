package com.onyxi7.betterarchery.client.render;

import com.onyxi7.betterarchery.config.BetterArcheryConfig;
import com.onyxi7.betterarchery.init.ItemInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class LayerQuiver implements LayerRenderer<EntityPlayer> {
    
    private final RenderPlayer renderPlayer;
    private static final ResourceLocation QUIVER_TEXTURE = new ResourceLocation("betterarchery:textures/entity/quiver.png");
    
    public LayerQuiver(RenderPlayer renderPlayer) {
        this.renderPlayer = renderPlayer;
    }
    
    @Override
    public void doRenderLayer(EntityPlayer player, float limbSwing, float limbSwingAmount, 
                             float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        
        // Check if rendering is enabled
        if (!BetterArcheryConfig.general.renderQuiverOnBack) {
            return;
        }
        
        // Do not render if it has a layer, is invisible, or is asleep
        if ((player.isWearing(EnumPlayerModelParts.CAPE) && ((AbstractClientPlayer)player).getLocationCape() != null) 
            || player.isInvisible() || player.isPlayerSleeping()) {
            return;
        }
        
        // Find the quiver in your inventory
        ItemStack quiverStack = findQuiverInInventory(player);
        
        if (quiverStack.isEmpty()) {
            return;
        }
        
        // Do not render if the quiver is in the primary hand
        ItemStack heldItem = player.getHeldItemMainhand();
        if (!heldItem.isEmpty() && isQuiver(heldItem)) {
            return;
        }
        
        GlStateManager.pushMatrix();
        
        // Basic Back Position
        GlStateManager.translate(0.0F, 0.35F, 0.16F);
        
        // Adjust if you are wearing chest armor
        if (!player.inventory.armorItemInSlot(2).isEmpty()) {
            GlStateManager.translate(0.0F, player.isSneaking() ? -0.1F : 0.0F, player.isSneaking() ? 0.025F : 0.06F);
        }
        
        // Adjust if you are crouching
        if (player.isSneaking()) {
            GlStateManager.translate(0.0F, 0.08F, 0.13F);
            GlStateManager.rotate(28.8F, 1.0F, 0.0F, 0.0F);
        }
        
        // Rotate it so it faces outward
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        
        // Enable blend for transparency
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        // Render the quiver model
        renderQuiverModel(quiverStack);
        
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableAlpha();
        
        GlStateManager.popMatrix();
    }
    
    private void renderQuiverModel(ItemStack stack) {
        // Scale the quiver model
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5F, 0.8F, 0.3F);
        
        // Quiver texture
        Minecraft.getMinecraft().renderEngine.bindTexture(QUIVER_TEXTURE);
        
        // Rendering a simple cube as a quiver
        // In the future, I'll replace this with a 3D model
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        
        // Draw a simple cube
        drawQuiverBox();
        
        GlStateManager.popMatrix();
    }
    
    private void drawQuiverBox() {
        // Draw a simple cube for the quiver
        // This is temporary, until I create a real 3D model
        GlStateManager.pushMatrix();
        
        // Create a simple cube using OpenGL
        GL11.glBegin(GL11.GL_QUADS);
        
        // Front
        GL11.glNormal3f(0.0F, 0.0F, 1.0F);
        GL11.glTexCoord2f(0.0F, 0.0F); GL11.glVertex3f(-0.2F, -0.4F, 0.15F);
        GL11.glTexCoord2f(1.0F, 0.0F); GL11.glVertex3f(0.2F, -0.4F, 0.15F);
        GL11.glTexCoord2f(1.0F, 1.0F); GL11.glVertex3f(0.2F, 0.4F, 0.15F);
        GL11.glTexCoord2f(0.0F, 1.0F); GL11.glVertex3f(-0.2F, 0.4F, 0.15F);
        
        // Back
        GL11.glNormal3f(0.0F, 0.0F, -1.0F);
        GL11.glTexCoord2f(0.0F, 0.0F); GL11.glVertex3f(-0.2F, -0.4F, -0.15F);
        GL11.glTexCoord2f(0.0F, 1.0F); GL11.glVertex3f(-0.2F, 0.4F, -0.15F);
        GL11.glTexCoord2f(1.0F, 1.0F); GL11.glVertex3f(0.2F, 0.4F, -0.15F);
        GL11.glTexCoord2f(1.0F, 0.0F); GL11.glVertex3f(0.2F, -0.4F, -0.15F);
        
        // Left
        GL11.glNormal3f(-1.0F, 0.0F, 0.0F);
        GL11.glTexCoord2f(0.0F, 0.0F); GL11.glVertex3f(-0.2F, -0.4F, -0.15F);
        GL11.glTexCoord2f(1.0F, 0.0F); GL11.glVertex3f(-0.2F, -0.4F, 0.15F);
        GL11.glTexCoord2f(1.0F, 1.0F); GL11.glVertex3f(-0.2F, 0.4F, 0.15F);
        GL11.glTexCoord2f(0.0F, 1.0F); GL11.glVertex3f(-0.2F, 0.4F, -0.15F);
        
        // Right
        GL11.glNormal3f(1.0F, 0.0F, 0.0F);
        GL11.glTexCoord2f(0.0F, 0.0F); GL11.glVertex3f(0.2F, -0.4F, -0.15F);
        GL11.glTexCoord2f(0.0F, 1.0F); GL11.glVertex3f(0.2F, 0.4F, -0.15F);
        GL11.glTexCoord2f(1.0F, 1.0F); GL11.glVertex3f(0.2F, 0.4F, 0.15F);
        GL11.glTexCoord2f(1.0F, 0.0F); GL11.glVertex3f(0.2F, -0.4F, 0.15F);
        
        // Top
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glTexCoord2f(0.0F, 0.0F); GL11.glVertex3f(-0.2F, 0.4F, -0.15F);
        GL11.glTexCoord2f(0.0F, 1.0F); GL11.glVertex3f(-0.2F, 0.4F, 0.15F);
        GL11.glTexCoord2f(1.0F, 1.0F); GL11.glVertex3f(0.2F, 0.4F, 0.15F);
        GL11.glTexCoord2f(1.0F, 0.0F); GL11.glVertex3f(0.2F, 0.4F, -0.15F);
        
        // Below
        GL11.glNormal3f(0.0F, -1.0F, 0.0F);
        GL11.glTexCoord2f(0.0F, 0.0F); GL11.glVertex3f(-0.2F, -0.4F, -0.15F);
        GL11.glTexCoord2f(1.0F, 0.0F); GL11.glVertex3f(0.2F, -0.4F, -0.15F);
        GL11.glTexCoord2f(1.0F, 1.0F); GL11.glVertex3f(0.2F, -0.4F, 0.15F);
        GL11.glTexCoord2f(0.0F, 1.0F); GL11.glVertex3f(-0.2F, -0.4F, 0.15F);
        
        GL11.glEnd();
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
