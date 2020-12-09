package me.linus.momentum.module.modules.hud;

import me.linus.momentum.module.Module;

public class CrystalModule extends Module {
    public CrystalModule() {
        super("CrystalModule", Category.HUD, "");
        INSTANCE = this;
    }

    public static CrystalModule INSTANCE;
}
