package me.linus.momentum.module.modules.hud;

import me.linus.momentum.module.Module;

public class TimeModule extends Module {
    public TimeModule() {
        super("TimeModule", Category.HUD, "");
        INSTANCE = this;
    }

    public static TimeModule INSTANCE;
}
