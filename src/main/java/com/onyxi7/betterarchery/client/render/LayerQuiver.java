package com.onyxi7.betterarchery.client.render;

import com.onyxi7.betterarchery.config.BetterArcheryConfig;
import com.onyxi7.betterarchery.init.ItemInit;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class LayerQuiver implements LayerRenderer<EntityLivingBase> {
    
    private final RenderLivingBase<?> renderPlayer;
    private final ModelBase modelQuiver;
    private final ModelRenderer quiverRenderer;
    
    public LayerQuiver(RenderLivingBase<?> renderPlayer) {
        this.renderPlayer = renderPlayer;
        this.modelQuiver = new ModelQuiver();
        this.quiverRenderer = this.modelQuiver.boxList.get(0);
    }
    
    @Override
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, 
                             float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!BetterArcheryConfig.general.renderQuiverOnBack) {
            return;
        }
        
        if (!(entitylivingbaseIn instanceof EntityPlayer)) {
            return;
        }
        
        EntityPlayer player = (EntityPlayer) entitylivingbaseIn;
        ItemStack quiverStack = findQuiverInInventory(player);
        
        if (quiverStack.isEmpty()) {
            return;
        }
        
        GlStateManager.pushMatrix();
        
        if (player.isSneaking()) {
            GlStateManager.translate(0.0F, 0.2F, 0.0F);
        }
        
        // CORRECCIÓN: Castear a ModelBiped
        if (this.renderPlayer.getMainModel() instanceof ModelBiped) {
            ModelBiped modelBiped = (ModelBiped) this.renderPlayer.getMainModel();
            modelBiped.bipedBody.postRender(0.0625F);
        }
        
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.0F, -0.5F, 0.2F);
        
        renderQuiverItem(quiverStack);
        
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
    
    private void renderQuiverItem(ItemStack stack) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        
        this.renderPlayer.bindTexture(new net.minecraft.util.ResourceLocation("betterarchery:textures/entity/quiver.png"));
        this.quiverRenderer.render(0.0625F);
        
        GlStateManager.popMatrix();
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
    
    private static class ModelQuiver extends ModelBase {
        public ModelQuiver() {
            ModelRenderer box = new ModelRenderer(this, 0, 0);
            box.setTextureSize(16, 16);
            box.addBox(-2.0F, -4.0F, -1.0F, 4, 8, 2, 0.0F);
            this.boxList.add(box);
        }
    }
}
