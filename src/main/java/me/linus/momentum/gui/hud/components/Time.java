package me.linus.momentum.gui.hud.components;

import me.linus.momentum.gui.hud.AnchorPoint;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.gui.theme.ThemeColor;
import me.linus.momentum.module.modules.client.HUDEditor;
import me.linus.momentum.util.render.FontUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Time extends HUDComponent {
    public Time() {
        super("Time", 2, 35, AnchorPoint.BottomRight);
    }

    @Override
    public void renderComponent() {
        FontUtil.drawString(new SimpleDateFormat("h:mm a").format(new Date()), this.x, this.y, HUDEditor.colorSync.getValue() ? ThemeColor.BRIGHT : -1);
        width = (int) (FontUtil.getStringWidth(new SimpleDateFormat("h:mm a").format(new Date())) + 2);
    }
}