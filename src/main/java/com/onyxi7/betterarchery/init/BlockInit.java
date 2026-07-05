package com.onyxi7.betterarchery.init;

import com.onyxi7.betterarchery.blocks.TargetBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

import java.util.ArrayList;
import java.util.List;

public class BlockInit {
    public static final List<Block> BLOCKS = new ArrayList<>();
    public static final Block TARGET_BLOCK = register(new TargetBlock(), "target_block");

    private static Block registerBlock(Block block) {
        BLOCKS.add(block);
        block.setRegistryName(block.getTranslationKey().substring(5));
        block.setTranslationKey(block.getTranslationKey().substring(5));
        return block;
    }

    public static void registerItems() {
        for (Block block : BLOCKS) {
            ItemBlock itemBlock = new ItemBlock(block);
            itemBlock.setRegistryName(block.getRegistryName());
            itemBlock.setTranslationKey(block.getTranslationKey());
        }
    }
}
