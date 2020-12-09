package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.module.modules.hud.PingModule;
import me.linus.momentum.util.render.FontUtil;

import java.awt.*;
import java.util.Objects;

public class Ping extends HUDComponent<PingModule> {
    public Ping() {
        super("Ping", 2, 24, PingModule.INSTANCE);
    }

    @Override
    public void render() {
        FontUtil.drawStringWithShadow( "Ping " + getPing() + " ms", Momentum.componentManager.getComponentByName("Ping").getX(), Momentum.componentManager.getComponentByName("Ping").getY(), new Color(255, 255, 255).getRGB());
        width = (int) FontUtil.getStringWidth("Ping " + getPing() + " ms") + 2;
    }

    public int getPing() {
        int p;
        if (mc.player == null || mc.getConnection() == null || mc.getConnection().getPlayerInfo(mc.player.getName()) == null)
            p = -1;
        else {
            mc.player.getName();
            p = Objects.requireNonNull(mc.getConnection().getPlayerInfo(mc.player.getName())).getResponseTime();
        }

        return p;
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
