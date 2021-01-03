package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.util.player.MotionUtil;

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
        Momentum.fontManager.getCustomFont().drawStringWithShadow(MotionUtil.getSpeed(), this.x, this.y, -1);
        width = Momentum.fontManager.getCustomFont().getStringWidth(MotionUtil.getSpeed()) + 2;
    }
}
