package me.linus.momentum.module.modules.hud;

import me.linus.momentum.module.Module;

public class DirectionModule extends Module {
    public DirectionModule() {
        super("DirectionModule", Category.HUD, "");
        INSTANCE = this;
    }

    public static DirectionModule INSTANCE;
}
