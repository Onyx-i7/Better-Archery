package com.onyxi7.betterarchery.proxy;

import com.onyxi7.betterarchery.client.render.LayerQuiver;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
    
    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
    }
    
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        registerPlayerLayers();
    }
    
    private void registerPlayerLayers() {
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        
        for (Render<? extends EntityPlayer> render : renderManager.getEntityRenderMap().values()) {
            if (render instanceof RenderPlayer) {
                RenderPlayer renderPlayer = (RenderPlayer) render;
                renderPlayer.addLayer(new LayerQuiver(renderPlayer));
            }
        }
    }
}
