package me.linus.momentum.module.modules.render;

import me.linus.momentum.module.Module;

/**
 * @author linustouchtips
 * @since 11/26/2020
 */

public class NoWeather extends Module {
    public NoWeather() {
        super("NoWeather", Category.RENDER, "Prevents rain from rendering client-side");
    }

    public void onUpdate() {
        if (mc.world == null)
            return;

        if (mc.world.isRaining()) {
            mc.world.setRainStrength(0);
        }
    }
}
