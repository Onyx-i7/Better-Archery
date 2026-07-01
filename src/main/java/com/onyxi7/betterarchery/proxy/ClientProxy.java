package com.onyxi7.betterarchery.proxy;

import com.onyxi7.betterarchery.client.render.LayerQuiver;
import com.onyxi7.betterarchery.client.render.RenderQuiverSkeleton;
import com.onyxi7.betterarchery.entities.EntityQuiverSkeleton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
    
    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
    }
    
    @Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		
		// Record a render of the Quiver Skeleton
		RenderingRegistry.registerEntityRenderingHandler(
			com.onyxi7.betterarchery.entities.EntityQuiverSkeleton.class,
			RenderQuiverSkeleton::new
		);
		
		// Register arrow renderers
		RenderingRegistry.registerEntityRenderingHandler(
			com.onyxi7.betterarchery.entities.EntityFireArrow.class,
			manager -> new RenderCustomArrow(manager, new ResourceLocation("betterarchery:textures/entity/firearrow.png"))
		);
		
		RenderingRegistry.registerEntityRenderingHandler(
			com.onyxi7.betterarchery.entities.EntityTorchArrow.class,
			manager -> new RenderCustomArrow(manager, new ResourceLocation("betterarchery:textures/entity/torcharrow.png"))
		);
		
		RenderingRegistry.registerEntityRenderingHandler(
			com.onyxi7.betterarchery.entities.EntityImpactArrow.class,
			manager -> new RenderCustomArrow(manager, new ResourceLocation("betterarchery:textures/entity/impactarrow.png"))
		);
		
		RenderingRegistry.registerEntityRenderingHandler(
			com.onyxi7.betterarchery.entities.EntityEnderArrow.class,
			manager -> new RenderCustomArrow(manager, new ResourceLocation("betterarchery:textures/entity/enderarrow.png"))
		);
		
		RenderingRegistry.registerEntityRenderingHandler(
			com.onyxi7.betterarchery.entities.EntitySplittingArrow.class,
			manager -> new RenderCustomArrow(manager, new ResourceLocation("betterarchery:textures/entity/splittingarrow.png"))
		);
		
		RenderingRegistry.registerEntityRenderingHandler(
			com.onyxi7.betterarchery.entities.EntityDrillArrow.class,
			manager -> new RenderCustomArrow(manager, new ResourceLocation("betterarchery:textures/entity/drillarrow.png"))
		);
		
		RenderingRegistry.registerEntityRenderingHandler(
			com.onyxi7.betterarchery.entities.EntityPotionArrow.class,
			manager -> new RenderCustomArrow(manager, new ResourceLocation("betterarchery:textures/entity/potionarrow.png"))
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
