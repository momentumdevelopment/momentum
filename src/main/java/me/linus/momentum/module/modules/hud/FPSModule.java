package me.linus.momentum.module.modules.hud;

import me.linus.momentum.module.Module;

public class FPSModule extends Module {
    public FPSModule() {
        super("FPSModule", Category.HUD, "");
        INSTANCE = this;
    }

    public static FPSModule INSTANCE;
}
