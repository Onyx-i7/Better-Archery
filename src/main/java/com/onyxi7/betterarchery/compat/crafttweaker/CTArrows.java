package com.onyxi7.betterarchery.compat.crafttweaker;

import com.onyxi7.betterarchery.config.ModpackOverrides;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.betterarchery.Arrows")
@ZenRegister
@ModOnly("betterarchery")
public class CTArrows {
    
    @ZenMethod
    public static void setDamageMultiplier(String arrowRegistryName, float multiplier) {
        CraftTweakerAPI.logInfo("[BetterArchery] Setting damage multiplier for " + arrowRegistryName + " to " + multiplier);
        ModpackOverrides.arrowDamageMultipliers.put(arrowRegistryName, multiplier);
    }
    
    @ZenMethod
    public static void setExplosionRadius(String arrowRegistryName, float radius) {
        CraftTweakerAPI.logInfo("[BetterArchery] Setting explosion radius for " + arrowRegistryName + " to " + radius);
        ModpackOverrides.arrowExplosionRadii.put(arrowRegistryName, radius);
    }
    
    @ZenMethod
    public static void setFireDuration(String arrowRegistryName, int duration) {
        CraftTweakerAPI.logInfo("[BetterArchery] Setting fire duration for " + arrowRegistryName + " to " + duration);
        ModpackOverrides.arrowFireDurations.put(arrowRegistryName, duration);
    }
    
    @ZenMethod
    public static void clearOverrides() {
        CraftTweakerAPI.logInfo("[BetterArchery] Clearing all arrow overrides");
        ModpackOverrides.arrowDamageMultipliers.clear();
        ModpackOverrides.arrowExplosionRadii.clear();
        ModpackOverrides.arrowFireDurations.clear();
    }
}
