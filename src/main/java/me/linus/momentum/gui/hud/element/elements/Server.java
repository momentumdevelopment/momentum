package me.linus.momentum.gui.hud.element.elements;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.element.AnchorPoint;
import me.linus.momentum.util.client.color.ThemeColor;
import me.linus.momentum.module.modules.client.HUD;
import me.linus.momentum.gui.hud.element.HUDElement;
import me.linus.momentum.util.render.FontUtil;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Server extends HUDElement {
    public Server() {
        super("Server", 2, 46, Category.INFO, AnchorPoint.None);
    }

    @Override
    public void renderElement() {
        String server;
        if (!mc.isSingleplayer())
            server = mc.getCurrentServerData().serverIP;
        else
            server = "SinglePlayer";

        FontUtil.drawString(server, this.x, this.y, HUD.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);

        width = Momentum.fontManager.getCustomFont().getStringWidth(server) + 2;
    }
}
