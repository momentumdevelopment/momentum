package me.linus.momentum.module.modules.hud;

import me.linus.momentum.module.Module;

public class InventoryModule extends Module {
    public InventoryModule() {
        super("InventoryModule", Category.HUD, "");
        INSTANCE = this;
    }

    public static InventoryModule INSTANCE;
}
