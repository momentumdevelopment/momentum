package me.linus.momentum.module.modules.hud;

import me.linus.momentum.module.Module;

public class WelcomerModule extends Module {
    public WelcomerModule() {
        super("WelcomerModule", Category.HUD, "");
        INSTANCE = this;
    }

    public static WelcomerModule INSTANCE;
}
