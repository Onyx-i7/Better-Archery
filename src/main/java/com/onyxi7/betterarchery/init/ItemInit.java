package com.onyxi7.betterarchery.init;

import com.onyxi7.betterarchery.objects.items.ItemQuiver;
import com.onyxi7.betterarchery.objects.items.ItemQuiverWithArrows;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.Item;

public class ItemInit {
    
    public static final List<Item> ITEMS = new ArrayList<>();
    
    public static final Item QUIVER = new ItemQuiver("quiver");
    
    public static final Item QUIVER_WITH_ARROWS = new ItemQuiverWithArrows("quiver_with_arrows", 576);
}
