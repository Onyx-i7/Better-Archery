package com.onyxi7.betterarchery.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores runtime overrides from CraftTweaker scripts.
 * Modpack makers can use these to customize bow and arrow stats.
 */
public class ModpackOverrides {
    
    // Bow overrides: registry name -> stat value
    public static final Map<String, Float> bowDamageMultipliers = new HashMap<>();
    public static final Map<String, Float> bowSpeedMultipliers = new HashMap<>();
    public static final Map<String, Float> bowPullBackMultipliers = new HashMap<>();
    public static final Map<String, Integer> bowDurabilityOverrides = new HashMap<>();
    
    // Arrow overrides
    public static final Map<String, Float> arrowDamageMultipliers = new HashMap<>();
    public static final Map<String, Float> arrowExplosionRadii = new HashMap<>();
    public static final Map<String, Integer> arrowFireDurations = new HashMap<>();
    
    public static float getBowDamageMultiplier(String registryName, float defaultValue) {
        return bowDamageMultipliers.getOrDefault(registryName, defaultValue);
    }
    
    public static float getBowSpeedMultiplier(String registryName, float defaultValue) {
        return bowSpeedMultipliers.getOrDefault(registryName, defaultValue);
    }
    
    public static float getBowPullBackMultiplier(String registryName, float defaultValue) {
        return bowPullBackMultipliers.getOrDefault(registryName, defaultValue);
    }
    
    public static int getBowDurability(String registryName, int defaultValue) {
        return bowDurabilityOverrides.getOrDefault(registryName, defaultValue);
    }
    
    public static float getArrowDamageMultiplier(String registryName, float defaultValue) {
        return arrowDamageMultipliers.getOrDefault(registryName, defaultValue);
    }
    
    public static void clearAll() {
        bowDamageMultipliers.clear();
        bowSpeedMultipliers.clear();
        bowPullBackMultipliers.clear();
        bowDurabilityOverrides.clear();
        arrowDamageMultipliers.clear();
        arrowExplosionRadii.clear();
        arrowFireDurations.clear();
    }
}
