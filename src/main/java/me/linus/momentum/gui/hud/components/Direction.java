package me.linus.momentum.gui.hud.components;

import me.linus.momentum.gui.hud.HUDComponentManager;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.util.render.FontUtil;
import me.linus.momentum.util.world.PlayerUtil;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Direction extends HUDComponent {
    public Direction() {
        super("Direction", 2, 68);
    }

    @Override
    public void renderComponent() {
        FontUtil.drawStringWithShadow(PlayerUtil.getFacing() + TextFormatting.GRAY + " [" + PlayerUtil.getTowards() + "]", HUDComponentManager.getComponentByName("Direction").getX(), HUDComponentManager.getComponentByName("Direction").getY(), new Color(255, 255, 255).getRGB());
        width = (int) (FontUtil.getStringWidth(PlayerUtil.getFacing() + " [" + PlayerUtil.getTowards() + "]") + 2);
    }
}
