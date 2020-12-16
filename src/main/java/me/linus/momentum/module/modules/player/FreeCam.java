package me.linus.momentum.module.modules.player;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.slider.Slider;

/**
 * @author linustouchtips
 * @since 12/11/2020
 */

public class FreeCam extends Module {
    public FreeCam() {
        super("FreeCam", Category.PLAYER, "Allows you to fly out of your body");
    }

    public static Slider speed = new Slider("Speed", 0.0D, 0.5D, 3.0D, 1);
    private static final Checkbox playerModel = new Checkbox("Player Model", true);
    private static final Checkbox cancelPackets = new Checkbox("Cancel Packets", true);
    private static final Checkbox noClip = new Checkbox("NoClip", true);

    @Override
    public void setup() {
        addSetting(speed);
        addSetting(playerModel);
        addSetting(cancelPackets);
        addSetting(noClip);
    }
}
