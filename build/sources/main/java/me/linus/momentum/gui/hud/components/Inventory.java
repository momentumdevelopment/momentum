package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.module.modules.hud.InventoryModule;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class Inventory extends HUDComponent<InventoryModule> {
    public Inventory() {
        super("Inventory", 400, 2, InventoryModule.INSTANCE);
        width = 146;
        height = 50;
    }

    @Override
    public void render() {
        if (mc.player == null || mc.player.inventory == null)
            return;

        GlStateManager.enableAlpha();
        GuiScreen.drawRect(Momentum.componentManager.getComponentByName("Inventory").getX(), Momentum.componentManager.getComponentByName("Inventory").getY(), Momentum.componentManager.getComponentByName("Inventory").getX() + 145, Momentum.componentManager.getComponentByName("Inventory").getY() + 48, new Color(0, 0, 0, 125).getRGB());
        GlStateManager.disableAlpha();

        int slotX = 0;
        int slotY = 0;

        RenderHelper.enableGUIStandardItemLighting();
        for (ItemStack stack : mc.player.inventory.mainInventory) {
            if (mc.player.inventory.mainInventory.indexOf(stack) < 9) continue; // hotbar
            mc.getRenderItem().renderItemAndEffectIntoGUI(stack, Momentum.componentManager.getComponentByName("Inventory").getX() + slotX, Momentum.componentManager.getComponentByName("Inventory").getY() + slotY);
            mc.getRenderItem().renderItemOverlays(mc.fontRenderer, stack, Momentum.componentManager.getComponentByName("Inventory").getX() + slotX, Momentum.componentManager.getComponentByName("Inventory").getY() + slotY);
            if (slotX < (8 * 16))
                slotX += 16;
            else {
                slotX = 0;
                slotY += 16;
            }
        }

        RenderHelper.disableStandardItemLighting();
    }

    @Override
    public void mouseHovered(int mouseX, int mouseY) {
        if (isMouseOnComponent(mouseX, mouseY)) colors = new Color(82, 81, 77, 125).getRGB();
        else colors = new Color(117, 116, 110, 125).getRGB();
    }

    public boolean isMouseOnComponent(int x, int y) {
        if (x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height) {
            return true;
        }
        return false;
    }
}
