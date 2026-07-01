package com.onyxi7.betterarchery;

import com.onyxi7.betterarchery.proxy.CommonProxy;
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
			new ResourceLocation("betterarchery", "potion_arrow"),
			EntityPotionArrow.class,
			"potion_arrow",
			1,
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
