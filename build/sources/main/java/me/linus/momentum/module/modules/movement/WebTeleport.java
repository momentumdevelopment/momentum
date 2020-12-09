package me.linus.momentum.module.modules.movement;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.mode.Mode;

/**
 * @author linustouchtips
 * @since 12/03/2020
 */

public class WebTeleport extends Module {
    public WebTeleport() {
        super("WebTeleport", Category.MOVEMENT, "Allows you to fall through webs faster");
    }

    private static Mode mode = new Mode("Mode", "Normal", "Vanilla");

    @Override
    public void setup() {
        addSetting(mode);
    }

    @Override
    public void onUpdate() {
        if (mc.player.isInWeb && mode.getValue() == 0) {
            for (int i = 0; i < 10; i++) {
                mc.player.motionY--;
            }
        } if (mc.player.isInWeb && mode.getValue() == 1) {
            mc.player.isInWeb = false;
        }
    }
}
