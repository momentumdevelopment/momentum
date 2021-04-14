package me.linus.momentum.gui.hud.element.elements;

import me.linus.momentum.gui.hud.element.AnchorPoint;
import me.linus.momentum.util.client.color.ThemeColor;
import me.linus.momentum.module.modules.client.HUD;
import me.linus.momentum.gui.hud.element.HUDElement;
import me.linus.momentum.util.render.FontUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Time extends HUDElement {
    public Time() {
        super("Time", 2, 35, Category.INFO, AnchorPoint.BottomRight);
    }

    @Override
    public void renderElement() {
        FontUtil.drawString(new SimpleDateFormat("h:mm a").format(new Date()), this.x, this.y, HUD.colorSync.getValue() ? ThemeColor.BRIGHT : -1);
        width = (int) (FontUtil.getStringWidth(new SimpleDateFormat("h:mm a").format(new Date())) + 2);
    }
}