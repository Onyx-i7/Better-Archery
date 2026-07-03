package com.onyxi7.betterarchery.compat.crafttweaker;

import com.onyxi7.betterarchery.config.ModpackOverrides;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * CraftTweaker integration for Better Archery bows.
 * Allows modpack makers to customize bow stats.
 * 
 * Example usage in .zs script:
 *   mods.betterarchery.Bows.setDamageMultiplier("betterarchery:long_bow", 1.5);
 *   mods.betterarchery.Bows.setSpeedMultiplier("betterarchery:long_bow", 0.8);
 */
@ZenClass("mods.betterarchery.Bows")
@ZenRegister
@ModOnly("betterarchery")
public class CTBows {
    
    @ZenMethod
    public static void setDamageMultiplier(String bowRegistryName, float multiplier) {
        CraftTweakerAPI.logInfo("[BetterArchery] Setting damage multiplier for " + bowRegistryName + " to " + multiplier);
        ModpackOverrides.bowDamageMultipliers.put(bowRegistryName, multiplier);
    }
    
    @ZenMethod
    public static void setSpeedMultiplier(String bowRegistryName, float multiplier) {
        CraftTweakerAPI.logInfo("[BetterArchery] Setting speed multiplier for " + bowRegistryName + " to " + multiplier);
        ModpackOverrides.bowSpeedMultipliers.put(bowRegistryName, multiplier);
    }
    
    @ZenMethod
    public static void setPullBackMultiplier(String bowRegistryName, float multiplier) {
        CraftTweakerAPI.logInfo("[BetterArchery] Setting pull back multiplier for " + bowRegistryName + " to " + multiplier);
        ModpackOverrides.bowPullBackMultipliers.put(bowRegistryName, multiplier);
    }
    
    @ZenMethod
    public static void setDurability(String bowRegistryName, int durability) {
        CraftTweakerAPI.logInfo("[BetterArchery] Setting durability for " + bowRegistryName + " to " + durability);
        ModpackOverrides.bowDurabilityOverrides.put(bowRegistryName, durability);
    }
    
    @ZenMethod
    public static void setAllStats(String bowRegistryName, float damageMult, float speedMult, float pullBackMult) {
        CraftTweakerAPI.logInfo("[BetterArchery] Setting all stats for " + bowRegistryName);
        ModpackOverrides.bowDamageMultipliers.put(bowRegistryName, damageMult);
        ModpackOverrides.bowSpeedMultipliers.put(bowRegistryName, speedMult);
        ModpackOverrides.bowPullBackMultipliers.put(bowRegistryName, pullBackMult);
    }
    
    @ZenMethod
    public static void clearOverrides() {
        CraftTweakerAPI.logInfo("[BetterArchery] Clearing all bow overrides");
        ModpackOverrides.bowDamageMultipliers.clear();
        ModpackOverrides.bowSpeedMultipliers.clear();
        ModpackOverrides.bowPullBackMultipliers.clear();
        ModpackOverrides.bowDurabilityOverrides.clear();
    }
}
