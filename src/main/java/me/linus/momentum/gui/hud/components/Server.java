package me.linus.momentum.gui.hud.components;

import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.util.render.FontUtil;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Server extends HUDComponent {
    public Server() {
        super("Server", 2, 46);
    }

    @Override
    public void renderComponent() {
        String server;
        if (!mc.isSingleplayer())
            server = mc.getCurrentServerData().serverIP;
        else
            server = "SinglePlayer";

        FontUtil.drawStringWithShadow(server, this.x, this.y, new Color(255, 255, 255).getRGB());

        width = (int) FontUtil.getStringWidth(server) + 2;
    }
}
