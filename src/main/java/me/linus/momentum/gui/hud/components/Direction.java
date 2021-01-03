package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.util.player.rotation.RotationUtil;
import net.minecraft.util.text.TextFormatting;

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
        Momentum.fontManager.getCustomFont().drawStringWithShadow(RotationUtil.getFacing() + TextFormatting.GRAY + " [" + RotationUtil.getTowards() + "]", this.x, this.y, -1);
        width = Momentum.fontManager.getCustomFont().getStringWidth(RotationUtil.getFacing() + " [" + RotationUtil.getTowards() + "]") + 2;
    }
}
