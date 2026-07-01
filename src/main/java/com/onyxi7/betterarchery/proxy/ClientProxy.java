package com.onyxi7.betterarchery.proxy;

import com.onyxi7.betterarchery.client.render.LayerQuiver;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderPlayer;
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
        for (RenderPlayer renderPlayer : Minecraft.getMinecraft().getRenderManager().getSkinMap().values()) {
            renderPlayer.addLayer(new LayerQuiver(renderPlayer));
        }
    }
    
    @Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		
		// Record property pulls for the bows
		net.minecraft.client.renderer.ItemModelMesher mesher = 
			net.minecraft.client.Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		
		// For each bow, record the property pull
		for (Item item : ItemInit.ITEMS) {
			if (item instanceof CustomBow) {
				mesher.getPropertyManager().getProperty(
					new net.minecraft.util.ResourceLocation("pull")
				);
			}
		}
	}
}
