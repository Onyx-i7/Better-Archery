package com.onyxi7.betterarchery.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = "betterarchery")
public class BetterArcheryConfig {
    
    public static General general = new General();
    public static Modpack modpack = new Modpack();
    public static Mobs mobs = new Mobs();
    public static Arrows arrows = new Arrows();
    public static Debug debug = new Debug();
    
    public static class General {
        @Config.Comment("Enable quiver rendering on player's back")
        public boolean renderQuiverOnBack = true;
        
        @Config.Comment("Maximum arrows that can be stored in quiver")
        @Config.RangeInt(min = 1, max = 512)
        public int maxQuiverArrows = 128;
    }
    
    public static class Modpack {
		public Map<String, Float> bowDamageOverrides = new HashMap<>();
		public Map<String, Float> bowSpeedOverrides = new HashMap<>();
		public JsonObject arrowOverrides = new JsonObject();
	}

    
    public static class Mobs {
        @Config.Comment("Enable Quiver Skeleton spawning")
        public boolean enableQuiverSkeleton = true;
        
        @Config.Comment("Quiver Skeleton spawn weight")
        @Config.RangeInt(min = 1, max = 100)
        public int quiverSkeletonSpawnWeight = 10;
        
        @Config.Comment("Maximum arrows Quiver Skeleton can have")
        @Config.RangeInt(min = 1, max = 64)
        public int maxArrows = 32;
        
        @Config.Comment("Minimum arrows Quiver Skeleton can have")
        @Config.RangeInt(min = 1, max = 64)
        public int minArrows = 8;
        
        @Config.Comment("Chance for Quiver Skeleton to have special bow (0.0-1.0)")
        @Config.RangeDouble(min = 0.0, max = 1.0)
        public double specialBowChance = 0.2;
    }
    
    public static class Arrows {
        @Config.Comment("Drill Arrow - Maximum blocks it can destroy")
        @Config.RangeInt(min = 1, max = 64)
        public int drillArrowMaxBlocks = 10;
        
        @Config.Comment("Drill Arrow - Power loss per block")
        @Config.RangeDouble(min = 0.0, max = 1.0)
        public double drillArrowPowerLoss = 0.1;
        
        @Config.Comment("Impact Arrow - Explosion radius")
        @Config.RangeDouble(min = 0.1, max = 10.0)
        public double impactArrowExplosionRadius = 2.0;
    }
    
    public static class Debug {
        @Config.Comment("Enable debug logging for quiver and bow events")
        public boolean enableDebug = false;
        
        @Config.Comment("Log ArrowNockEvent details")
        public boolean logArrowNock = true;
        
        @Config.Comment("Log ArrowLooseEvent details")
        public boolean logArrowLoose = true;
        
        @Config.Comment("Log quiver detection")
        public boolean logQuiverDetection = true;
        
        @Config.Comment("Log inventory checks")
        public boolean logInventoryChecks = true;
    }
    
    @Mod.EventBusSubscriber(modid = "betterarchery")
    private static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals("betterarchery")) {
                ConfigManager.sync("betterarchery", Config.Type.INSTANCE);
            }
        }
    }
}
