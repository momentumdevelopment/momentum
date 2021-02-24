package me.linus.momentum.module.modules.movement;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;

/**
 * @author linustouchtips
 * @since 11/30/2020
 */

public class LongJump extends Module {
    public LongJump() {
        super("LongJump", Category.MOVEMENT, "Increases player jump distance");
    }

    public static Mode mode = new Mode("Mode", "ByPass", "Glide", "Deer");
    public static Slider speed = new Slider("Speed", 0.0D, 4.0D, 10.0D, 0);
    public static Checkbox packet = new Checkbox("Packet", true);
    public static Checkbox knockback = new Checkbox("Knockback", true);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(speed);
        addSetting(packet);
        addSetting(knockback);
    }


    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}
