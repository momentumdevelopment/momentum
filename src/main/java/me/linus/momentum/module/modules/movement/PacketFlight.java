package me.linus.momentum.module.modules.movement;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;

/**
 * @author linustouchtips
 * @since 12/07/2020
 */

public class PacketFlight extends Module {
    public PacketFlight() {
        super("PacketFlight", Category.MOVEMENT, "Allows you to fly using packets");
    }

    public static Mode mode = new Mode("Mode","Phase", "Upward", "Downward");
    public static SubSlider upSpeed = new SubSlider(mode,"Upward Speed", 0.0D, 0.0625D, 0.1D, 4);
    public static SubSlider packetIteration = new SubSlider(mode,"Packet Iteration", 0.0D, 20.0D, 40.0D, 0);

    public static Mode phase = new Mode("Phase","Full", "Semi", "None");
    public static SubCheckbox packet = new SubCheckbox(phase, "Packet Spoof", false);
    public static SubCheckbox noMove = new SubCheckbox(phase, "Unnatural Movement", false);
    public static SubCheckbox noClip = new SubCheckbox(phase, "NoClip", true);

    public static Checkbox pause = new Checkbox("Pause", false);
    public static SubCheckbox pauseRotation = new SubCheckbox(pause, "Server Rotations", false);

    public static Slider speed = new Slider("Speed", 0.0D, 0.18D, 1.0D, 2);

    public static Checkbox gravity = new Checkbox("Gravity", true);
    public static SubSlider fallSpeed = new SubSlider(gravity,"Fall Speed", 0.0D, 0.04D, 0.1D, 3);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(phase);
        addSetting(pause);
        addSetting(speed);
        addSetting(gravity);
    }



    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}
