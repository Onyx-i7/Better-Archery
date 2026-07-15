package com.onyxi7.betterarchery.proxy;

import com.onyxi7.betterarchery.client.render.LayerQuiver;
import com.onyxi7.betterarchery.client.render.RenderQuiverSkeleton;
import com.onyxi7.betterarchery.client.render.RenderCustomArrow;
import com.onyxi7.betterarchery.entities.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
    
    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
    }
    
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        
        RenderingRegistry.registerEntityRenderingHandler(
            EntityFireArrow.class,
            manager -> new RenderCustomArrow(manager, new ResourceLocation("betterarchery:textures/entity/arrow.png"))
        );
        
        RenderingRegistry.registerEntityRenderingHandler(
            EntityTorchArrow.class,
            manager -> new RenderCustomArrow(manager, new ResourceLocation("betterarchery:textures/entity/arrow.png"))
        );
        
        RenderingRegistry.registerEntityRenderingHandler(
            EntityImpactArrow.class,
            manager -> new RenderCustomArrow(manager, new ResourceLocation("betterarchery:textures/entity/arrow.png"))
        );
        
        RenderingRegistry.registerEntityRenderingHandler(
            EntityEnderArrow.class,
            manager -> new RenderCustomArrow(manager, new ResourceLocation("betterarchery:textures/entity/arrow.png"))
        );
        
        RenderingRegistry.registerEntityRenderingHandler(
            EntitySplittingArrow.class,
            manager -> new RenderCustomArrow(manager, new ResourceLocation("betterarchery:textures/entity/arrow.png"))
        );
        
        RenderingRegistry.registerEntityRenderingHandler(
            EntityDrillArrow.class,
            manager -> new RenderCustomArrow(manager, new ResourceLocation("betterarchery:textures/entity/arrow.png"))
        );
    }
    
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        registerPlayerLayers();
    }
    
    private void registerPlayerLayers() {
        for (RenderPlayer renderPlayer : Minecraft.getMinecraft().getRenderManager().getSkinMap().values()) {
            renderPlayer.addLayer(new LayerQuiver(renderPlayer));
        }
    }
}
