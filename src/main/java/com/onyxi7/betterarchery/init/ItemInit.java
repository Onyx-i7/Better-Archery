package com.onyxi7.betterarchery.init;

import com.onyxi7.betterarchery.items.ItemQuiver;
import com.onyxi7.betterarchery.items.ItemQuiverWithArrows;
import com.onyxi7.betterarchery.items.arrows.ItemDrillArrow;
import com.onyxi7.betterarchery.items.arrows.ItemFireArrow;
import com.onyxi7.betterarchery.items.arrows.ItemTorchArrow;
import com.onyxi7.betterarchery.items.arrows.ItemImpactArrow;
import com.onyxi7.betterarchery.items.arrows.ItemEnderArrow;
import com.onyxi7.betterarchery.items.arrows.ItemSplittingArrow;
import com.onyxi7.betterarchery.items.bows.LongBow;
import com.onyxi7.betterarchery.items.bows.RecurveBow;
import com.onyxi7.betterarchery.items.bows.CompositeBow;
import com.onyxi7.betterarchery.items.bows.YumiBow;
import com.onyxi7.betterarchery.init.CreativeTabInit;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.Item;

public class ItemInit {
    
    private static final CreativeTabInit TAB_INIT = null;
    
    public static final List<Item> ITEMS = new ArrayList<>();
    
    // Quiver
    public static final Item QUIVER = new ItemQuiver("quiver");
    public static final Item QUIVER_WITH_ARROWS = new ItemQuiverWithArrows("quiver_with_arrows");
    
	// Special Arrows
	public static final Item DRILL_ARROW = new ItemDrillArrow("drill_arrow");
	public static final Item FIRE_ARROW = new ItemFireArrow("fire_arrow");
	public static final Item TORCH_ARROW = new ItemTorchArrow("torch_arrow");
	public static final Item IMPACT_ARROW = new ItemImpactArrow("impact_arrow");
	public static final Item ENDER_ARROW = new ItemEnderArrow("ender_arrow");
	public static final Item SPLITTING_ARROW = new ItemSplittingArrow("splitting_arrow");
	
	// Custom Bows
	public static final Item LONG_BOW = new LongBow("long_bow");
	public static final Item RECURVE_BOW = new RecurveBow("recurve_bow");
	public static final Item COMPOSITE_BOW = new CompositeBow("composite_bow");
	public static final Item YUMI_BOW = new YumiBow("yumi_bow");
}
