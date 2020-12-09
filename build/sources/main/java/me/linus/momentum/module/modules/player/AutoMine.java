package me.linus.momentum.module.modules.player;

import me.linus.momentum.module.Module;

public class AutoMine extends Module {
    public AutoMine() {
        super("AutoMine", Category.PLAYER, "Automatically mines blocks in your crosshairs");
    }

    @Override
    public void onUpdate() {
        mc.sendClickBlockToController(true);
    }
}
