package com.onyxi7.betterarchery.init;

import com.onyxi7.betterarchery.blocks.TargetBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

import java.util.ArrayList;
import java.util.List;

public class BlockInit {
    
    public static final List<Block> BLOCKS = new ArrayList<>();
    
    public static final Block TARGET_BLOCK = new TargetBlock();
    
    static {
        BLOCKS.add(TARGET_BLOCK);
    }
    
    public static void registerItems() {
        for (Block block : BLOCKS) {
            ItemInit.ITEMS.add(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        }
    }
}