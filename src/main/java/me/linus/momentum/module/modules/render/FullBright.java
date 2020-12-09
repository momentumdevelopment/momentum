package me.linus.momentum.module.modules.render;

import me.linus.momentum.module.Module;

/**
 * @author linustouchtips
 * @since 11/27/2020
 */

public class FullBright extends Module {
    public FullBright() {
        super("FullBright", Category.RENDER, "Adjusts light levels");
    }

    private float oldBright;

    @Override
    public void onEnable() {
        oldBright = mc.gameSettings.gammaSetting;

        if (mc.player != null) {
            mc.gameSettings.gammaSetting = +100;
        }
    }

    @Override
    public void onDisable() {
        if (mc.player != null) {
            mc.gameSettings.gammaSetting = oldBright;
        }
    }
}
