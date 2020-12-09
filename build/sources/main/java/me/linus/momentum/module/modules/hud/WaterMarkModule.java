package me.linus.momentum.module.modules.hud;

import me.linus.momentum.module.Module;

public class WaterMarkModule extends Module {
    public WaterMarkModule() {
        super("WaterMarkModule", Category.HUD, "");
        INSTANCE = this;
    }

    public static WaterMarkModule INSTANCE;
}
