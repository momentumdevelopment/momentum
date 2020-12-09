package me.linus.momentum.module.modules.hud;

import me.linus.momentum.module.Module;

public class SystemModule extends Module {
    public SystemModule() {
        super("SystemModule", Category.HUD, "");
        INSTANCE = this;
    }

    public static SystemModule INSTANCE;
}
