package com.onyxi7.betterarchery.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = "betterarchery", name = "BetterArchery")
public class BetterArcheryConfig {
    
    @Config.Comment("General Settings")
    @Config.Name("General")
    public static GeneralConfig general = new GeneralConfig();
    
    @Config.Comment("Arrow Settings")
    @Config.Name("Arrows")
    public static ArrowConfig arrows = new ArrowConfig();
    
    @Config.Comment("Bow Settings")
    @Config.Name("Bows")
    public static BowConfig bows = new BowConfig();
    
    @Config.Comment("Mob Settings")
    @Config.Name("Mobs")
    public static MobConfig mobs = new MobConfig();
    
    public static class GeneralConfig {
        @Config.Comment("Enable debug mode (shows extra info in console)")
        @Config.Name("Debug Mode")
        public boolean debugMode = false;
        
        @Config.Comment("Enable quiver rendering on player's back")
        @Config.Name("Render Quiver on Back")
        public boolean renderQuiverOnBack = true;
    }
    
    public static class ArrowConfig {
        @Config.Comment("Enable Fire Arrow")
        @Config.Name("Enable Fire Arrow")
        public boolean enableFireArrow = true;
        
        @Config.Comment("Enable Torch Arrow")
        @Config.Name("Enable Torch Arrow")
        public boolean enableTorchArrow = true;
        
        @Config.Comment("Enable Impact Arrow")
        @Config.Name("Enable Impact Arrow")
        public boolean enableImpactArrow = true;
        
        @Config.Comment("Enable Ender Arrow")
        @Config.Name("Enable Ender Arrow")
        public boolean enableEnderArrow = true;
        
        @Config.Comment("Enable Splitting Arrow")
        @Config.Name("Enable Splitting Arrow")
        public boolean enableSplittingArrow = true;
        
        @Config.Comment("Enable Drill Arrow")
        @Config.Name("Enable Drill Arrow")
        public boolean enableDrillArrow = true;
        
        @Config.Comment("Enable Potion Arrow")
        @Config.Name("Enable Potion Arrow")
        public boolean enablePotionArrow = true;
        
        @Config.Comment("Impact Arrow explosion radius (default: 4.0)")
        @Config.Name("Impact Arrow Explosion Radius")
        @Config.RangeDouble(min = 1.0, max = 10.0)
        public double impactArrowExplosionRadius = 4.0;
        
        @Config.Comment("Drill Arrow max blocks to destroy (default: 10)")
        @Config.Name("Drill Arrow Max Blocks")
        @Config.RangeInt(min = 1, max = 50)
        public int drillArrowMaxBlocks = 10;
        
        @Config.Comment("Drill Arrow power loss per block (default: 0.2)")
        @Config.Name("Drill Arrow Power Loss")
        @Config.RangeDouble(min = 0.05, max = 1.0)
        public double drillArrowPowerLoss = 0.2;
        
        @Config.Comment("Splitting Arrow number of splits (default: 3)")
        @Config.Name("Splitting Arrow Splits")
        @Config.RangeInt(min = 1, max = 10)
        public int splittingArrowSplits = 3;
        
        @Config.Comment("Fire Arrow fire duration in seconds (default: 5)")
        @Config.Name("Fire Arrow Duration")
        @Config.RangeInt(min = 1, max = 30)
        public int fireArrowDuration = 5;
    }
    
    public static class BowConfig {
        @Config.Comment("Long Bow durability (default: 384)")
        @Config.Name("Long Bow Durability")
        @Config.RangeInt(min = 1, max = 5000)
        public int longBowDurability = 384;
        
        @Config.Comment("Recurve Bow durability (default: 256)")
        @Config.Name("Recurve Bow Durability")
        @Config.RangeInt(min = 1, max = 5000)
        public int recurveBowDurability = 256;
        
        @Config.Comment("Composite Bow durability (default: 1024)")
        @Config.Name("Composite Bow Durability")
        @Config.RangeInt(min = 1, max = 5000)
        public int compositeBowDurability = 1024;
        
        @Config.Comment("Yumi Bow durability (default: 384)")
        @Config.Name("Yumi Bow Durability")
        @Config.RangeInt(min = 1, max = 5000)
        public int yumiBowDurability = 384;
        
        @Config.Comment("Long Bow damage multiplier (default: 1.0)")
        @Config.Name("Long Bow Damage Mult")
        @Config.RangeDouble(min = 0.5, max = 3.0)
        public double longBowDamageMult = 1.0;
        
        @Config.Comment("Recurve Bow damage multiplier (default: 1.1)")
        @Config.Name("Recurve Bow Damage Mult")
        @Config.RangeDouble(min = 0.5, max = 3.0)
        public double recurveBowDamageMult = 1.1;
        
        @Config.Comment("Composite Bow damage multiplier (default: 1.25)")
        @Config.Name("Composite Bow Damage Mult")
        @Config.RangeDouble(min = 0.5, max = 3.0)
        public double compositeBowDamageMult = 1.25;
        
        @Config.Comment("Yumi Bow damage multiplier (default: 1.0)")
        @Config.Name("Yumi Bow Damage Mult")
        @Config.RangeDouble(min = 0.5, max = 3.0)
        public double yumiBowDamageMult = 1.0;
    }
    
    public static class MobConfig {
        @Config.Comment("Enable Quiver Skeleton spawning")
        @Config.Name("Enable Quiver Skeleton")
        public boolean enableQuiverSkeleton = true;
        
        @Config.Comment("Quiver Skeleton spawn weight (default: 3, higher = more common)")
        @Config.Name("Quiver Skeleton Spawn Weight")
        @Config.RangeInt(min = 1, max = 100)
        public int quiverSkeletonSpawnWeight = 3;
        
        @Config.Comment("Minimum arrows in Quiver Skeleton's quiver (default: 10)")
        @Config.Name("Min Arrows")
        @Config.RangeInt(min = 1, max = 64)
        public int minArrows = 10;
        
        @Config.Comment("Maximum arrows in Quiver Skeleton's quiver (default: 30)")
        @Config.Name("Max Arrows")
        @Config.RangeInt(min = 1, max = 64)
        public int maxArrows = 30;
        
        @Config.Comment("Chance for Quiver Skeleton to have special bow (default: 0.3 = 30%)")
        @Config.Name("Special Bow Chance")
        @Config.RangeDouble(min = 0.0, max = 1.0)
        public double specialBowChance = 0.3;
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
