package me.linus.momentum.module.modules.hud;

import me.linus.momentum.module.Module;

public class SpeedModule extends Module {
    public SpeedModule() {
        super("SpeedModule", Category.HUD, "");
        INSTANCE = this;
    }

    public static SpeedModule INSTANCE;
}
