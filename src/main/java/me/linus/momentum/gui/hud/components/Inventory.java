package me.linus.momentum.gui.hud.components;

import me.linus.momentum.gui.hud.HUDComponentManager;
import me.linus.momentum.gui.hud.HUDComponent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Inventory extends HUDComponent {
    public Inventory() {
        super("Inventory", 400, 2);
        width = 146;
        height = 50;
    }

    @Override
    public void renderComponent() {
        if (mc.player == null || mc.player.inventory == null)
            return;

        GlStateManager.enableAlpha();
        GuiScreen.drawRect(HUDComponentManager.getComponentByName("Inventory").getX(), HUDComponentManager.getComponentByName("Inventory").getY(), HUDComponentManager.getComponentByName("Inventory").getX() + 145, HUDComponentManager.getComponentByName("Inventory").getY() + 48, new Color(0, 0, 0, 125).getRGB());
        GlStateManager.disableAlpha();

        int slotX = 0;
        int slotY = 0;

        RenderHelper.enableGUIStandardItemLighting();
        for (ItemStack stack : mc.player.inventory.mainInventory) {
            if (mc.player.inventory.mainInventory.indexOf(stack) < 9) continue; // hotbar
            mc.getRenderItem().renderItemAndEffectIntoGUI(stack, HUDComponentManager.getComponentByName("Inventory").getX() + slotX, HUDComponentManager.getComponentByName("Inventory").getY() + slotY);
            mc.getRenderItem().renderItemOverlays(mc.fontRenderer, stack, HUDComponentManager.getComponentByName("Inventory").getX() + slotX, HUDComponentManager.getComponentByName("Inventory").getY() + slotY);
            if (slotX < (8 * 16))
                slotX += 16;
            else {
                slotX = 0;
                slotY += 16;
            }
        }

        RenderHelper.disableStandardItemLighting();
    }
}
