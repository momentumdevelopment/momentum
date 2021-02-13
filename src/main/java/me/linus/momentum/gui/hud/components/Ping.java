package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.AnchorPoint;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.gui.theme.ThemeColor;
import me.linus.momentum.module.modules.client.HUDEditor;
import me.linus.momentum.util.render.FontUtil;
import net.minecraft.util.text.TextFormatting;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Ping extends HUDComponent {
    public Ping() {
        super("Ping", 2, 24, AnchorPoint.BottomRight);
    }

    @Override
    public void renderComponent() {
        FontUtil.drawString("Ping " + TextFormatting.WHITE + (!mc.isSingleplayer() ? mc.getConnection().getPlayerInfo(mc.player.getUniqueID()).getResponseTime() : -1) + " ms", this.x, this.y, HUDEditor.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);
        width = Momentum.fontManager.getCustomFont().getStringWidth("Ping " + (!mc.isSingleplayer() ? mc.getConnection().getPlayerInfo(mc.player.getUniqueID()).getResponseTime() : -1) + " ms") + 2;
    }
}
