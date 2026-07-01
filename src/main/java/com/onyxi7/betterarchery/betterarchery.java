package com.onyxi7.betterarchery;

import com.onyxi7.betterarchery.proxy.CommonProxy;
import com.onyxi7.betterarchery.entities.EntityDrillArrow;
import com.onyxi7.betterarchery.entities.EntityPotionArrow;
import com.onyxi7.betterarchery.entities.EntityFireArrow;
import com.onyxi7.betterarchery.entities.EntityTorchArrow;
import com.onyxi7.betterarchery.entities.EntityImpactArrow;
import com.onyxi7.betterarchery.entities.EntityEnderArrow;
import com.onyxi7.betterarchery.entities.EntitySplittingArrow;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "betterarchery", name = "Better Archery", version = "1.0", acceptedMinecraftVersions = "[1.12.2]")
public class betterarchery {
    
    public static final String MODID = "betterarchery";
    public static final String NAME = "Better Archery";
    public static final String VERSION = "1.0";
    
    @Instance
    public static betterarchery instance;
    
    @SidedProxy(clientSide = "com.onyxi7.betterarchery.proxy.ClientProxy", serverSide = "com.onyxi7.betterarchery.proxy.CommonProxy")
    public static CommonProxy proxy;
    
    @Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		EntityRegistry.registerModEntity(
			new ResourceLocation("betterarchery", "drill_arrow"),
			EntityDrillArrow.class,
			"drill_arrow",
			0,
			this,
			64,
			1,
			true
		);
		
		EntityRegistry.registerModEntity(
			new ResourceLocation("betterarchery", "fire_arrow"),
			EntityFireArrow.class,
			"fire_arrow",
			2,
			this,
			64,
			1,
			true
		);

		EntityRegistry.registerModEntity(
			new ResourceLocation("betterarchery", "torch_arrow"),
			EntityTorchArrow.class,
			"torch_arrow",
			3,
			this,
			64,
			1,
			true
		);

		EntityRegistry.registerModEntity(
			new ResourceLocation("betterarchery", "impact_arrow"),
			EntityImpactArrow.class,
			"impact_arrow",
			4,
			this,
			64,
			1,
			true
		);

		EntityRegistry.registerModEntity(
			new ResourceLocation("betterarchery", "ender_arrow"),
			EntityEnderArrow.class,
			"ender_arrow",
			5,
			this,
			64,
			1,
			true
		);

		EntityRegistry.registerModEntity(
			new ResourceLocation("betterarchery", "splitting_arrow"),
			EntitySplittingArrow.class,
			"splitting_arrow",
			6,
			this,
			64,
			1,
			true
		);
    
		proxy.preInit(event);
	}
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
