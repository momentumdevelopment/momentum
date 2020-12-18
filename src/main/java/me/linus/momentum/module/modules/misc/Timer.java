package me.linus.momentum.module.modules.misc;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.slider.Slider;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Timer extends Module {
    public Timer() {
        super("Timer", Category.MISC, "Modifies client-side ticks");
    }

    public static Slider ticks = new Slider("Ticks", 0.1D, 4.0D, 20.0D, 1);

    @Override
    public void setup() {
        addSetting(ticks);
    }

    @Override
    public void onDisable() {
        mc.timer.tickLength = 50;
    }

    @Override
    public void onUpdate() {
        mc.timer.tickLength = (float) (50.0f / ticks.getValue());
    }

    @Override
    public String getHUDData() {
        return " " + ticks.getValue();
    }
}
