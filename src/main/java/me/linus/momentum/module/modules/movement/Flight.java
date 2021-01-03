package me.linus.momentum.module.modules.movement;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.player.FlightUtil;

/**
 * @author linustouchtips
 * @since 11/30/2020
 */

public class Flight extends Module {
    public Flight() {
        super("Flight", Category.MOVEMENT, "Allows you to fly");
    }

    private static final Mode mode = new Mode("Mode", "Creative", "Vanilla");
    public static Slider hSpeed = new Slider("Horizontal Speed", 0.0D, 1.0D, 3.0D, 1);
    public static Slider ySpeed = new Slider("Vertical Speed", 0.0D, 1.0D, 3.0D, 1);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(hSpeed);
        addSetting(ySpeed);
    }

    @Override
    public void onEnable() {
       if (nullCheck())
           return;

       if (mode.getValue() == 1)
           mc.player.capabilities.isFlying = true;
    }

    @Override
    public void onDisable() {
        mc.player.capabilities.isFlying = false;
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (mode.getValue() == 0) {
            if (mc.gameSettings.keyBindJump.isKeyDown())
                mc.player.motionY = ySpeed.getValue();

            else if (mc.gameSettings.keyBindSneak.isKeyDown())
                mc.player.motionY = (ySpeed.getValue() * -1);

            else
                mc.player.motionY = 0;

            FlightUtil.horizontalEntityFlight(hSpeed.getValue());
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}
