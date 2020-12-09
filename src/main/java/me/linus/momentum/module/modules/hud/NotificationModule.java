package me.linus.momentum.module.modules.hud;

import me.linus.momentum.module.Module;

public class NotificationModule extends Module {
    public NotificationModule() {
        super("NotificationModule", Category.HUD, "");
        INSTANCE = this;
    }

    public static NotificationModule INSTANCE;
}
