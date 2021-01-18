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

    private static final Mode mode = new Mode("Mode", "Normal", "Vanilla");

    @Override
    public void setup() {
        addSetting(mode);
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (mc.player.isInWeb) {
            switch (mode.getValue()) {
                case 0:
                    for (int i = 0; i < 10; i++)
                        mc.player.motionY--;
                    break;
                case 1:
                    mc.player.isInWeb = false;
                    break;
            }
        }
    }
}
