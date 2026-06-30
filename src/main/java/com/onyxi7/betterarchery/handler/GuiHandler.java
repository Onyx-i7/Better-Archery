package com.onyxi7.betterarchery.handler;

import com.onyxi7.betterarchery.items.quiver.GuiQuiver;
import com.onyxi7.betterarchery.items.quiver.QuiverContainer;
import com.onyxi7.betterarchery.items.quiver.QuiverInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    
    public static final int QUIVER_GUI = 0;
    
    private QuiverInventory getQuiverInventory(EntityPlayer player) {
        QuiverInventory inv = new QuiverInventory(player.inventory, player.inventory.currentItem);
        return inv;
    }
    
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case QUIVER_GUI:
                return new QuiverContainer(player.inventory, getQuiverInventory(player));
        }
        return null;
    }
    
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case QUIVER_GUI:
                return new GuiQuiver(player.inventory, getQuiverInventory(player));
        }
        return null;
    }
}
