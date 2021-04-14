package me.linus.momentum.gui.hud.element.elements;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.element.AnchorPoint;
import me.linus.momentum.util.client.color.ThemeColor;
import me.linus.momentum.module.modules.client.HUD;
import me.linus.momentum.gui.hud.element.HUDElement;
import me.linus.momentum.util.player.rotation.RotationUtil;
import me.linus.momentum.util.render.FontUtil;
import net.minecraft.util.text.TextFormatting;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Direction extends HUDElement {
    public Direction() {
        super("Direction", 2, 68, Category.INFO, AnchorPoint.BottomLeft);
    }

    @Override
    public void renderElement() {
        FontUtil.drawString(RotationUtil.getFacing() + " " + TextFormatting.WHITE + RotationUtil.getTowards(), this.x, this.y, HUD.colorSync.getValue() ? ThemeColor.BRIGHT : ThemeColor.GRAY);
        width = Momentum.fontManager.getCustomFont().getStringWidth(RotationUtil.getFacing() + " " + TextFormatting.WHITE + RotationUtil.getTowards()) + 2;
    }
}
