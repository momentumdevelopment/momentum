package me.linus.momentum.module.modules.render.esp.modes;

import me.linus.momentum.module.modules.render.esp.ESPMode;

public class Box extends ESPMode {
    public Box() {
        isRender = true;
    }

    @Override
    public void drawESP() {
        if ((mc.getRenderManager()).options == null)
            return;

    }
}
