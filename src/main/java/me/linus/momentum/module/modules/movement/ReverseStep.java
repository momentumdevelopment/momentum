package me.linus.momentum.module.modules.movement;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.slider.Slider;

/**
 * @author linustouchtips
 * @since 11/26/2020
 */

public class ReverseStep extends Module {
    public ReverseStep() {
        super("ReverseStep", Category.MOVEMENT, "Allows you to fall faster");
    }

    public static Slider height = new Slider("Height", 0.0D, 2.0D, 5.0D, 0);

    public static Checkbox pause = new Checkbox("Pause", true);
    public static SubCheckbox sneakPause = new SubCheckbox(pause, "When Sneaking", false);
    public static SubCheckbox waterPause = new SubCheckbox(pause, "When in Liquid", true);

    @Override
    public void setup() {
        addSetting(height);
        addSetting(pause);
    }

    @Override
    public void onUpdate() {
    	if (nullCheck())
    	    return;

        if (mc.player.isInWater() && waterPause.getValue() || mc.player.isInLava() && waterPause.getValue() || mc.player.isOnLadder() || mc.gameSettings.keyBindJump.isKeyDown())
            return;

        if (mc.player.isSneaking() && sneakPause.getValue())
            return;

        if (mc.player != null && mc.player.onGround && !mc.player.isInWater() && !mc.player.isOnLadder()) {
            for (double y = 0.0; y < height.getValue() + 0.5; y += 0.01) {
                if (!mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, -y, 0.0)).isEmpty()) {
                    mc.player.motionY = -10.0;
                    break;
                }
            }
        }
    }
}
