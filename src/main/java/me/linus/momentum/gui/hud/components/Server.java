package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.gui.theme.ThemeColor;
import me.linus.momentum.module.modules.client.HUDEditor;
import me.linus.momentum.util.render.FontUtil;

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

        FontUtil.drawString(server, this.x, this.y, HUDEditor.colorSync.getValue() ? ThemeColor.BRIGHT : -1);

        width = Momentum.fontManager.getCustomFont().getStringWidth(server) + 2;
    }
}
