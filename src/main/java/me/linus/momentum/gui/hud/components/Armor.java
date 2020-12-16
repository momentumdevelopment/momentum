package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class Armor extends HUDComponent {
    public Armor() {
        super("Armor", 200, 200, null);
    }

    static final RenderItem itemRender = mc.getRenderItem();

    @Override
    public void render() {
        GlStateManager.enableTexture2D();
        int iteration = 0;
        for (ItemStack is : mc.player.inventory.armorInventory) {
            ++iteration;

            if (is.isEmpty())
                continue;

            int x = ((9 - iteration) * 14);
            itemRender.zLevel = 200.0f;
            itemRender.renderItemAndEffectIntoGUI(is, (Momentum.componentManager.getComponentByName("Armor").getX() - 70) + x , Momentum.componentManager.getComponentByName("Armor").getY());
            itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, is, (Momentum.componentManager.getComponentByName("Armor").getX() - 70) + x, Momentum.componentManager.getComponentByName("Armor").getY(), "");
            itemRender.zLevel = 0.0f;
            width = x - 8;
            height = 17;
        }

        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
    }

    @Override
    public void mouseHovered(int mouseX, int mouseY) {
        if (isMouseOnComponent(mouseX, mouseY)) colors = new Color(82, 81, 77, 125).getRGB();
        else colors = new Color(117, 116, 110, 125).getRGB();
    }

    public boolean isMouseOnComponent(int x, int y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height;
    }
}
