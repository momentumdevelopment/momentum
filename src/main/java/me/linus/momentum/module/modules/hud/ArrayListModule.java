package me.linus.momentum.module.modules.hud;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.mode.Mode;

public class ArrayListModule extends Module {
    public ArrayListModule() {
        super("ArrayListModule", Category.HUD, "");
        INSTANCE = this;
    }

    private static final Mode mode = new Mode("Mode", "Alpha", "Category", "Rainbow");

    @Override
    public void setup() {
        addSetting(mode);
    }

    public static ArrayListModule INSTANCE;
}

