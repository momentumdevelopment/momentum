package me.linus.momentum.module.modules.misc;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.world.TickUtil;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Timer extends Module {
    public Timer() {
        super("Timer", Category.MISC, "Modifies client-side ticks");
    }

    public static Slider ticks = new Slider("Ticks", 0.1D, 4.0D, 20.0D, 1);
    private static final Checkbox sync = new Checkbox("TPS Sync", false);

    @Override
    public void setup() {
        addSetting(ticks);
        addSetting(sync);
    }

    @Override
    public void onDisable() {
        mc.timer.tickLength = 50;
    }

    @Override
    public void onUpdate() {
        if (!sync.getValue())
            mc.timer.tickLength = (float) (50.0f / ticks.getValue());

        else
            mc.timer.tickLength = 50.0f / TickUtil.TPS;
    }

    @Override
    public String getHUDData() {
        return " " + ticks.getValue();
    }
}
