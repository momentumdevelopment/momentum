package me.linus.momentum.gui.hud.element.elements;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.element.AnchorPoint;
import me.linus.momentum.util.client.color.ThemeColor;
import me.linus.momentum.module.modules.client.HUD;
import me.linus.momentum.gui.hud.element.HUDElement;
import me.linus.momentum.util.player.MotionUtil;
import me.linus.momentum.util.render.FontUtil;
import net.minecraft.util.text.TextFormatting;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Speed extends HUDElement {
    public Speed() {
        super("Speed", 2, 46, Category.INFO, AnchorPoint.BottomRight);
    }

    @Override
    public void renderElement() {
        FontUtil.drawString("Speed " + TextFormatting.WHITE + MotionUtil.getSpeed(), this.x, this.y, HUD.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);
        width = Momentum.fontManager.getCustomFont().getStringWidth("Speed " + TextFormatting.WHITE + MotionUtil.getSpeed()) + 2;
    }
}
