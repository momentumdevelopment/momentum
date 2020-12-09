package me.linus.momentum.module.modules.hud;

import me.linus.momentum.module.Module;

public class CoordinatesModule extends Module {
    public CoordinatesModule() {
        super("CoordinatesModule", Category.HUD, "");
        INSTANCE = this;
    }

    public static CoordinatesModule INSTANCE;
}
