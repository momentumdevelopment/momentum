package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.gui.theme.ThemeColor;
import me.linus.momentum.module.modules.client.HUDEditor;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.util.render.FontUtil;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Welcomer extends HUDComponent {
    public Welcomer() {
        super("Welcomer", 200, 2);
    }

    public static Mode mode = new Mode("Mode", "Dynamic", "Static");

    @Override
    public void setup() {
        addSetting(mode);
    }

    @Override
    public void renderComponent() {
        FontUtil.drawString("Welcome " + mc.player.getName() + "! :^)", this.x, this.y, HUDEditor.colorSync.getValue() ? ThemeColor.BRIGHT : -1);
        width = Momentum.fontManager.getCustomFont().getStringWidth("Welcome " + mc.player.getName() + "! :^)") + 2;
    }
}
