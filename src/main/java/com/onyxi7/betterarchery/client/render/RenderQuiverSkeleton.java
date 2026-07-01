package com.onyxi7.betterarchery.client.render;

import com.onyxi7.betterarchery.entities.EntityQuiverSkeleton;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.util.ResourceLocation;

public class RenderQuiverSkeleton extends RenderSkeleton {
    
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/skeleton/skeleton.png");
    
    public RenderQuiverSkeleton(RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.addLayer(new LayerQuiver(this));
    }
    
    @Override
    protected ResourceLocation getEntityTexture(AbstractSkeleton entity) {
        return TEXTURE;
    }
}
