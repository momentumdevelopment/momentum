package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.util.render.FontUtil;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Ping extends HUDComponent {
    public Ping() {
        super("Ping", 2, 24, null);
    }

    @Override
    public void render() {
        int ping;

        if (!mc.isSingleplayer())
            ping = mc.getConnection().getPlayerInfo(mc.player.getUniqueID()).getResponseTime();
        else
            ping = -1;

        FontUtil.drawStringWithShadow( "Ping " + ping + " ms", Momentum.componentManager.getComponentByName("Ping").getX(), Momentum.componentManager.getComponentByName("Ping").getY(), new Color(255, 255, 255).getRGB());
        width = (int) FontUtil.getStringWidth("Ping " + ping + " ms") + 2;
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
