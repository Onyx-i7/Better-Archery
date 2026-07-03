package com.onyxi7.betterarchery.config;

import java.util.HashMap;
import java.util.Map;

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
    
    /**
     * Get damage multiplier for a bow, or default if not overridden
     */
    public static float getBowDamageMultiplier(String registryName, float defaultValue) {
        return bowDamageMultipliers.getOrDefault(registryName, defaultValue);
    }
    
    /**
     * Get speed multiplier for a bow, or default if not overridden
     */
    public static float getBowSpeedMultiplier(String registryName, float defaultValue) {
        return bowSpeedMultipliers.getOrDefault(registryName, defaultValue);
    }
    
    /**
     * Get pull back multiplier for a bow, or default if not overridden
     */
    public static float getBowPullBackMultiplier(String registryName, float defaultValue) {
        return bowPullBackMultipliers.getOrDefault(registryName, defaultValue);
    }
    
    /**
     * Get durability for a bow, or default if not overridden
     */
    public static int getBowDurability(String registryName, int defaultValue) {
        return bowDurabilityOverrides.getOrDefault(registryName, defaultValue);
    }
    
    /**
     * Get damage multiplier for an arrow, or default if not overridden
     */
    public static float getArrowDamageMultiplier(String registryName, float defaultValue) {
        return arrowDamageMultipliers.getOrDefault(registryName, defaultValue);
    }
    
    /**
     * Clear all overrides (useful for hot-reload)
     */
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
