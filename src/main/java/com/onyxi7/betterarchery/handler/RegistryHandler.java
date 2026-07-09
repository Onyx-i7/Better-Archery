package com.onyxi7.betterarchery.handler;

import com.onyxi7.betterarchery.init.BlockInit;
import com.onyxi7.betterarchery.init.ItemInit;
import com.onyxi7.betterarchery.util.interfaces.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = "betterarchery")
public class RegistryHandler {
    
    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        // Enter regular items
        event.getRegistry().registerAll(ItemInit.ITEMS.toArray(new Item[0]));
    }
    
    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        // Register blocks
        event.getRegistry().registerAll(BlockInit.BLOCKS.toArray(new Block[0]));
        
        // Register the ItemBlocks
        BlockInit.registerItemBlocks();
    }
    
    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event) {
        // Register Item Models
        for (Item item : ItemInit.ITEMS) {
            if (item instanceof IHasModel) {
                ((IHasModel) item).registerModels();
            }
        }
    }
    
    @SubscribeEvent
    public static void onRecipeRegister(RegistryEvent.Register<IRecipe> event) {
        // Recipe registration
    }
}
