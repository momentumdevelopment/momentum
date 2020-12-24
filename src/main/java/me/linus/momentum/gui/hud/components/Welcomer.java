package me.linus.momentum.gui.hud.components;

import me.linus.momentum.gui.hud.HUDComponentManager;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.util.render.FontUtil;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Welcomer extends HUDComponent {
    public Welcomer() {
        super("Welcomer", 200, 2);
    }

    @Override
    public void renderComponent() {
        FontUtil.drawStringWithShadow("Welcome " + mc.player.getName() + "! :^)", HUDComponentManager.getComponentByName("Welcomer").getX(), HUDComponentManager.getComponentByName("Welcomer").getY(), new Color(255, 255, 255).getRGB());
        width = (int) FontUtil.getStringWidth("Welcome " + mc.player.getName() + "! :^)") + 2;
    }
}
