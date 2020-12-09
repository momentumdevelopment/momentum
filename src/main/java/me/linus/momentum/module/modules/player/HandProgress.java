package me.linus.momentum.module.modules.player;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.slider.Slider;

/**
 * @author linustouchtips
 * @since 11/26/2020
 */

public class HandProgress extends Module {
    public HandProgress() {
        super("HandProgress", Category.PLAYER, "Allows you to change your hand height");
    }

    public static Slider offhandHeight = new Slider("OffHand", 0.0D, 0.5D, 1.0D, 2);
    public static Slider mainhandHeight = new Slider("MainHand", 0.0D, 0.5D, 1.0D, 2);

    @Override
    public void setup() {
        addSetting(offhandHeight);
        addSetting(mainhandHeight);
    }

    public void onUpdate() {
        mc.entityRenderer.itemRenderer.equippedProgressMainHand = (float) mainhandHeight.getValue();
        mc.entityRenderer.itemRenderer.equippedProgressOffHand = (float) offhandHeight.getValue();
    }
}
