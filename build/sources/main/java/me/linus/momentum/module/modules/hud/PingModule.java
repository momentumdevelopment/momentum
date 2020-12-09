package me.linus.momentum.module.modules.hud;

import me.linus.momentum.module.Module;

public class PingModule extends Module {
    public PingModule() {
        super("PingModule", Category.HUD, "");
        INSTANCE = this;
    }

    public static PingModule INSTANCE;
}
