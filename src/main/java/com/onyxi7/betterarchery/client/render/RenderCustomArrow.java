package com.onyxi7.betterarchery.client.render;

import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.ResourceLocation;

public class RenderCustomArrow extends RenderArrow<EntityArrow> {
    
    private final ResourceLocation texture;
    
    public RenderCustomArrow(RenderManager renderManager, ResourceLocation texture) {
        super(renderManager);
        this.texture = texture;
    }
    
    @Override
    protected ResourceLocation getEntityTexture(EntityArrow entity) {
        return texture;
    }
    
    @Override
    public void doRender(EntityArrow entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
