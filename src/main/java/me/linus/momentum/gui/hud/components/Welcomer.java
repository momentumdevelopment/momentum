package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.util.render.FontUtil;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Welcomer extends HUDComponent {
    public Welcomer() {
        super("Welcomer", 200, 2);
    }

    private static final Mode mode = new Mode("Mode", "Dynamic", "Static");

    @Override
    public void setup() {
        addSetting(mode);
    }

    @Override
    public void renderComponent() {
        Momentum.fontManager.getCustomFont().drawStringWithShadow("Welcome " + mc.player.getName() + "! :^)", this.x, this.y, -1);
        width = Momentum.fontManager.getCustomFont().getStringWidth("Welcome " + mc.player.getName() + "! :^)") + 2;
    }
}
