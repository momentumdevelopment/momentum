package me.linus.momentum.gui.hud.element.elements;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.element.AnchorPoint;
import me.linus.momentum.util.client.color.ThemeColor;
import me.linus.momentum.managers.TickManager;
import me.linus.momentum.module.modules.client.HUD;
import me.linus.momentum.gui.hud.element.HUDElement;
import me.linus.momentum.util.render.FontUtil;
import net.minecraft.util.text.TextFormatting;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class TPS extends HUDElement {
    public TPS() {
        super("TPS", 2, 57, Category.INFO, AnchorPoint.BottomRight);
    }

    @Override
    public void renderElement() {
        FontUtil.drawString("TPS " + TextFormatting.WHITE + TickManager.getTPS(), this.x, this.y, HUD.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);
        width = Momentum.fontManager.getCustomFont().getStringWidth(TickManager.getTPS() + " TPS") + 2;
    }
}
