package me.linus.momentum.gui.hud.components;

import me.linus.momentum.gui.hud.HUDComponentManager;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.util.render.FontUtil;
import me.linus.momentum.util.world.PlayerUtil;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Speed extends HUDComponent {
    public Speed() {
        super("Speed", 2, 46);
    }

    @Override
    public void renderComponent() {
        FontUtil.drawStringWithShadow(PlayerUtil.getSpeed(), HUDComponentManager.getComponentByName("Speed").getX(), HUDComponentManager.getComponentByName("Speed").getY(), new Color(255, 255, 255).getRGB());
        width = (int) FontUtil.getStringWidth(PlayerUtil.getSpeed()) + 2;
    }
}
