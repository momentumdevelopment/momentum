package me.linus.momentum.gui.hud.components;

import me.linus.momentum.gui.hud.HUDComponentManager;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.util.render.FontUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class FPS extends HUDComponent {
    public FPS() {
        super("FPS", 2, 13);
    }

    @Override
    public void renderComponent() {
        FontUtil.drawStringWithShadow(Minecraft.getDebugFPS() + " " + TextFormatting.GRAY + "FPS", HUDComponentManager.getComponentByName("FPS").getX(), HUDComponentManager.getComponentByName("FPS").getY(), new Color(255, 255, 255).getRGB());
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
