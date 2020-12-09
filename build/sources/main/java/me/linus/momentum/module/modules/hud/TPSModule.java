package me.linus.momentum.module.modules.hud;

import me.linus.momentum.module.Module;

public class TPSModule extends Module {
    public TPSModule() {
        super("TPSModule", Category.HUD, "");
        INSTANCE = this;
    }

    public static TPSModule INSTANCE;
}
