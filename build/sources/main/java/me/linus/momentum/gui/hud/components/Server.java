package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.module.modules.hud.ServerModule;
import me.linus.momentum.util.render.FontUtil;

import java.awt.*;

public class Server extends HUDComponent<ServerModule> {
    public Server() {
        super("Server", 2, 46, ServerModule.INSTANCE);
    }

    @Override
    public void render() {
        String server;
        if (!mc.isSingleplayer())
            server = mc.getCurrentServerData().serverIP;
        else
            server = "SinglePlayer";

        FontUtil.drawStringWithShadow(server, Momentum.componentManager.getComponentByName("Server").getX(), Momentum.componentManager.getComponentByName("Server").getY(), new Color(255, 255, 255).getRGB());

        width = (int) FontUtil.getStringWidth(server) + 2;
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
