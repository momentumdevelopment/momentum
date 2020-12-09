package me.linus.momentum.module.modules.hud;

import me.linus.momentum.module.Module;

public class ArmorModule extends Module {
    public ArmorModule() {
        super("ArmorModule", Category.HUD, "");
        INSTANCE = this;
    }

    public static ArmorModule INSTANCE;
}

