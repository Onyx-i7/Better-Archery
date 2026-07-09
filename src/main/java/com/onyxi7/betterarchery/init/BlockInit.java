package com.onyxi7.betterarchery.init;

import com.onyxi7.betterarchery.blocks.TargetBlock;
import com.onyxi7.betterarchery.blocks.GunpowderBarrel;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

import java.util.ArrayList;
import java.util.List;

public class BlockInit {
    public static final List<Block> BLOCKS = new ArrayList<>();
    
    public static final Block TARGET_BLOCK = new TargetBlock();
    public static final Block GUNPOWDER_BARREL = new GunpowderBarrel();
    
    static {
        BLOCKS.add(TARGET_BLOCK);
        BLOCKS.add(GUNPOWDER_BARREL);
    }
    
    public static void registerItemBlocks() {
        for (Block block : BLOCKS) {
            ItemBlock itemBlock = new ItemBlock(block);
            itemBlock.setRegistryName(block.getRegistryName());
            ItemInit.ITEMS.add(itemBlock);
        }
    }
}
