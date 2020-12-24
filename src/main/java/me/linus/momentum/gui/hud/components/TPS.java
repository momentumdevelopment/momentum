package me.linus.momentum.gui.hud.components;

import me.linus.momentum.gui.hud.HUDComponentManager;
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
        FontUtil.drawStringWithShadow(TickUtil.TPS + " " + TextFormatting.GRAY + "TPS", HUDComponentManager.getComponentByName("TPS").getX(), HUDComponentManager.getComponentByName("TPS").getY(), new Color(255, 255, 255).getRGB());
        width = (int) FontUtil.getStringWidth(TickUtil.TPS + " TPS") + 2;
    }
}
