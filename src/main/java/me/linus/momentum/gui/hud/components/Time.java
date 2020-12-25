package me.linus.momentum.gui.hud.components;

import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.util.render.FontUtil;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Time extends HUDComponent {
    public Time() {
        super("Time", 2, 35);
    }

    @Override
    public void renderComponent() {
        final String time = new SimpleDateFormat("h:mm a").format(new Date());
        FontUtil.drawStringWithShadow(time, this.x, this.y, new Color(255, 255, 255).getRGB());
        width = (int) FontUtil.getStringWidth(time) + 2;
    }
}
