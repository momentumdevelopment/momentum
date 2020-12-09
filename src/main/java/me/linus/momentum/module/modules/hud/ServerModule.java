package me.linus.momentum.module.modules.hud;

import me.linus.momentum.module.Module;

public class ServerModule extends Module {
    public ServerModule() {
        super("ServerModule", Category.HUD, "");
        INSTANCE = this;
    }

    public static ServerModule INSTANCE;
}
