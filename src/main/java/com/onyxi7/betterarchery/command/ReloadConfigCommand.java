package com.onyxi7.betterarchery.command;

import com.onyxi7.betterarchery.config.BetterArcheryConfig;
import com.onyxi7.betterarchery.entities.EntityQuiverSkeleton;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ReloadConfigCommand extends CommandBase {
    
    @Override
    public String getName() {
        return "betterarchery";
    }
    
    @Override
    public String getUsage(ICommandSender sender) {
        return "/betterarchery reload - Reloads BetterArchery config";
    }
    
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 0 && args[0].equals("reload")) {
            // Reload settings
            net.minecraftforge.common.config.ConfigManager.sync("betterarchery", net.minecraftforge.common.config.Config.Type.INSTANCE);
            
            // Re-register the Quiver Skeleton spawn
            if (BetterArcheryConfig.mobs.enableQuiverSkeleton) {
                EntityRegistry.addSpawn(
                    EntityQuiverSkeleton.class,
                    BetterArcheryConfig.mobs.quiverSkeletonSpawnWeight,
                    1,
                    1,
                    EnumCreatureType.MONSTER,
                    Biomes.PLAINS,
                    Biomes.FOREST,
                    Biomes.TAIGA,
                    Biomes.DESERT,
                    Biomes.JUNGLE,
                    Biomes.BIRCH_FOREST,
                    Biomes.ROOFED_FOREST,
                    Biomes.EXTREME_HILLS,
                    Biomes.SAVANNA,
                    Biomes.MESA,
                    Biomes.ICE_PLAINS,
                    Biomes.COLD_TAIGA
                );
            }
            
            sender.sendMessage(new TextComponentString("§a[BetterArchery] Config reloaded successfully!"));
            sender.sendMessage(new TextComponentString("§aDrill Max Blocks: " + BetterArcheryConfig.arrows.drillArrowMaxBlocks));
            sender.sendMessage(new TextComponentString("§aQuiver Skeleton Enabled: " + BetterArcheryConfig.mobs.enableQuiverSkeleton));
        } else {
            sender.sendMessage(new TextComponentString("§cUsage: " + getUsage(sender)));
        }
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2; // Requires operator level
    }
}
