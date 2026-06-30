package com.onyxi7.betterarchery.items.quiver;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiQuiver extends GuiContainer {
    
    private static final ResourceLocation TEXTURE = new ResourceLocation("betterarchery:textures/gui/quivergui.png");
    private QuiverInventory inv;
    private GuiButton removeAll;
    
    public GuiQuiver(InventoryPlayer playerInv, QuiverInventory quiverInv) {
        super(new QuiverContainer(playerInv, quiverInv));
        this.inv = quiverInv;
        this.xSize = 176;
        this.ySize = 148;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.removeAll = new GuiButton(0, (this.width - 20) / 2, this.guiTop + 36, 20, 20, "V");
        this.buttonList.add(this.removeAll);
    }
    
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == this.removeAll.id) {
            ((QuiverContainer) this.inventorySlots).transferAllStacks();
        }
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(TEXTURE);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        
        mc.getTextureManager().bindTexture(TEXTURE);
        x = (width - QuiverInventory.size * 18) / 2;
        y += 15;
        for (int i = 0; i < QuiverInventory.size; i++) {
            drawTexturedModalRect(x, y, 0, 0, 18, 18);
            x += 18;
        }
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString("Carcaj", 8, 6, 4210752);
        fontRenderer.drawString("Inventario", 8, ySize - 96 + 2, 4210752);
    }
}
