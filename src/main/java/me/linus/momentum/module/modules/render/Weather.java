package me.linus.momentum.module.modules.render;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.mode.Mode;

/**
 * @author linustouchtips
 * @since 11/26/2020
 */

public class Weather extends Module {
    public Weather() {
        super("Weather", Category.RENDER, "Allows you to control weather client-side");
    }

    private static final Mode mode = new Mode("Mode", "Clear", "Rain", "Thunder");

    @Override
    public void setup() {
        addSetting(mode);
    }

    public void onUpdate() {
        if (nullCheck())
            return;

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
