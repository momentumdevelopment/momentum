package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.util.render.FontUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class Totem extends HUDComponent {
    public Totem() {
        super("Totem", 2, 57, null);
    }

    @Override
    public void render() {
        int totems = mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        FontUtil.drawStringWithShadow("Totems: " + totems, Momentum.componentManager.getComponentByName("Totem").getX(), Momentum.componentManager.getComponentByName("Totem").getY(), new Color(255, 255, 255).getRGB());
        width = (int) FontUtil.getStringWidth("Totems: " + totems) + 2;
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
