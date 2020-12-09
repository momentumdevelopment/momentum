package me.linus.momentum.module.modules.hud;

import me.linus.momentum.module.Module;

public class PlayerViewerModule extends Module {
    public PlayerViewerModule() {
        super("PlayerViewerModule", Category.HUD, "");
        INSTANCE = this;
    }

    public static PlayerViewerModule INSTANCE;
}
