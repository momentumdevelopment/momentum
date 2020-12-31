package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.util.render.FontUtil;
import me.linus.momentum.util.world.TickUtil;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class TPS extends HUDComponent {
    public TPS() {
        super("TPS", 2, 57);
    }

    @Override
    public void renderComponent() {
        Momentum.fontManager.getCustomFont().drawStringWithShadow(TickUtil.TPS + " " + TextFormatting.GRAY + "TPS", this.x, this.y, new Color(255, 255, 255).getRGB());
        width = Momentum.fontManager.getCustomFont().getStringWidth(TickUtil.TPS + " TPS") + 2;
    }
}
