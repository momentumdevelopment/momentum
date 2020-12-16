package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.module.modules.hud.FPSModule;
import me.linus.momentum.module.modules.hud.PingModule;
import me.linus.momentum.util.render.FontUtil;

import java.awt.*;
import java.util.Objects;

public class Ping extends HUDComponent {
    public Ping() {
        super("Ping", 2, 24, null);
    }

    @Override
    public void render() {
        FontUtil.drawStringWithShadow( "Ping " + mc.getConnection().getPlayerInfo(mc.player.getUniqueID()).getResponseTime() + " ms", Momentum.componentManager.getComponentByName("Ping").getX(), Momentum.componentManager.getComponentByName("Ping").getY(), new Color(255, 255, 255).getRGB());
        width = (int) FontUtil.getStringWidth("Ping " + mc.getConnection().getPlayerInfo(mc.player.getUniqueID()).getResponseTime() + " ms") + 2;
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
