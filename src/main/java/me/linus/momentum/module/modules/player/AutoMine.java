package me.linus.momentum.module.modules.player;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.mode.Mode;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class AutoMine extends Module {
    public AutoMine() {
        super("AutoMine", Category.PLAYER, "Automatically mines blocks in your crosshairs");
    }

    private static final Mode mode = new Mode("Mode", "Packet", "Frenzy", "Auto-Tunnel");

    @Override
    public void setup() {
        addSetting(mode);
    }

    @Override
    public void onUpdate() {
        mc.sendClickBlockToController(true);
    }
}
