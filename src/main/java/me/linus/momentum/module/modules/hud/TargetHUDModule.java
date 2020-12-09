package me.linus.momentum.module.modules.hud;

import me.linus.momentum.module.Module;

public class TargetHUDModule extends Module {
    public TargetHUDModule() {
        super("TargetHUDModule", Category.HUD, "");
        INSTANCE = this;
    }

    public static TargetHUDModule INSTANCE;
}
