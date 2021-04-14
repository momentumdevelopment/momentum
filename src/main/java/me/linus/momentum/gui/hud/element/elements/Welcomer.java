package me.linus.momentum.gui.hud.element.elements;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.element.AnchorPoint;
import me.linus.momentum.module.modules.client.HUD;
import me.linus.momentum.gui.hud.element.HUDElement;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.util.client.color.ThemeColor;
import me.linus.momentum.util.render.FontUtil;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Welcomer extends HUDElement {
    public Welcomer() {
        super("Welcomer", 200, 2, Category.MISC, AnchorPoint.None);
    }

    public static Mode mode = new Mode("Mode", "Dynamic", "Static");

    @Override
    public void setup() {
        addSetting(mode);
    }

    @Override
    public void renderElement() {
        FontUtil.drawString("Welcome " + mc.player.getName() + "! :^)", this.x, this.y, HUD.colorSync.getValue() ? ThemeColor.BRIGHT : -1);
        width = Momentum.fontManager.getCustomFont().getStringWidth("Welcome " + mc.player.getName() + "! :^)") + 2;
    }
}
