package me.linus.momentum.module.modules.render;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;

/**
 * @author linustouchtips
 * @since 11/26/2020
 */

public class Weather extends Module {
    public Weather() {
        super("Weather", Category.RENDER, "Allows you to control weather client-side");
    }

    public static Mode mode = new Mode("Mode", "Clear", "Rain", "Thunder");
    public static Slider time = new Slider("Time", 0.0D, 6000.0D, 24000.0D, 0);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(time);
    }

    public void onUpdate() {
        if (nullCheck())
            return;

        mc.world.setWorldTime((long) time.getValue());

        switch (mode.getValue()) {
            case 0:
                mc.world.setRainStrength(0);
                break;
            case 1:
                mc.world.setRainStrength(1);
                break;
            case 2:
                mc.world.setRainStrength(2);
                break;
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}