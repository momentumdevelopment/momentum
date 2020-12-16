package me.linus.momentum.module.modules.render;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.mode.Mode;

/**
 * @author linustouchtips
 * @since 11/27/2020
 */

public class NoBob extends Module {
    public NoBob() {
        super("NoBob", Category.RENDER, "Prevents the bobbing animation");
    }

    private static final Mode mode = new Mode("Mode", "Vanilla", "Settings");

    @Override
    public void setup() {
        addSetting(mode);
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (mode.getValue() == 0)
            mc.player.distanceWalkedModified = 4.0f;
        else
            mc.gameSettings.viewBobbing = false;
    }
}
