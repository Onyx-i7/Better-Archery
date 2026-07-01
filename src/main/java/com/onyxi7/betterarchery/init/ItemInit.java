package com.onyxi7.betterarchery.init;

import com.onyxi7.betterarchery.items.ItemQuiver;
import com.onyxi7.betterarchery.items.ItemQuiverWithArrows;
import com.onyxi7.betterarchery.items.arrows.ItemDrillArrow;
import com.onyxi7.betterarchery.items.arrows.ItemPotionArrow;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.Item;

public class ItemInit {
    
    public static final List<Item> ITEMS = new ArrayList<>();
    
    // Carcaj
    public static final Item QUIVER = new ItemQuiver("quiver");
    public static final Item QUIVER_WITH_ARROWS = new ItemQuiverWithArrows("quiver_with_arrows", 576);
    
    // Flechas especiales
    public static final Item DRILL_ARROW = new ItemDrillArrow("drill_arrow");
    
    public static final Item POTION_ARROW = new ItemPotionArrow("potion_arrow");
}
