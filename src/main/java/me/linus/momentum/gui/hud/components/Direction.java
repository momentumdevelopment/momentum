package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.util.render.FontUtil;
import me.linus.momentum.util.world.PlayerUtil;

import java.awt.*;

public class Direction extends HUDComponent {
    public Direction() {
        super("Direction", 2, 68, null);
    }

    @Override
    public void render() {
        FontUtil.drawStringWithShadow(PlayerUtil.getFacing() + " [" + PlayerUtil.getTowards() + "]", Momentum.componentManager.getComponentByName("Direction").getX(), Momentum.componentManager.getComponentByName("Direction").getY(), new Color(255, 255, 255).getRGB());
        width = (int) (FontUtil.getStringWidth(PlayerUtil.getFacing() + " [" + PlayerUtil.getTowards() + "]") + 2);
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
