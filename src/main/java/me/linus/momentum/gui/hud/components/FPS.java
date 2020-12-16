package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.util.render.FontUtil;
import net.minecraft.client.Minecraft;

import java.awt.*;

public class FPS extends HUDComponent {
    public FPS() {
        super("FPS", 2, 13, null);
    }

    @Override
    public void render() {
        FontUtil.drawStringWithShadow(Minecraft.getDebugFPS() + " FPS", Momentum.componentManager.getComponentByName("FPS").getX(), Momentum.componentManager.getComponentByName("FPS").getY(), new Color(255, 255, 255).getRGB());
        width = (int) FontUtil.getStringWidth(Minecraft.getDebugFPS() + " FPS") + 2;
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
