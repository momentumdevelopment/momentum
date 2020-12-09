package me.linus.momentum.module.modules.hud;

import me.linus.momentum.module.Module;

public class TotemModule extends Module {
    public TotemModule() {
        super("TotemModule", Category.HUD, "");
        INSTANCE = this;
    }

    public static TotemModule INSTANCE;
}
