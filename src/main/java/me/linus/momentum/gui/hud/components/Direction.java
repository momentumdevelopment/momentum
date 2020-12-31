package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.util.render.FontUtil;
import me.linus.momentum.util.world.RotationUtil;
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
        Momentum.fontManager.getCustomFont().drawStringWithShadow(RotationUtil.getFacing() + TextFormatting.GRAY + " [" + RotationUtil.getTowards() + "]", this.x, this.y, new Color(255, 255, 255).getRGB());
        width = Momentum.fontManager.getCustomFont().getStringWidth(RotationUtil.getFacing() + " [" + RotationUtil.getTowards() + "]") + 2;
    }
}
