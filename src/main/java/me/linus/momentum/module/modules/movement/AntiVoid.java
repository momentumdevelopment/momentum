package me.linus.momentum.module.modules.movement;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.util.client.MessageUtil;

/**
 * @author linustouchtips
 * @since 12/03/2020
 */

public class AntiVoid extends Module {
    public AntiVoid() {
        super("AntiVoid", Category.MOVEMENT, "Pulls you out of the void");
    }

    private static Mode mode = new Mode("Mode", "Float", "Freeze");

    @Override
    public void setup() {
        addSetting(mode);
    }

    @Override
    public void onUpdate() {
        double yLevel = mc.player.posY;
        if (yLevel <= 0.5) {
            MessageUtil.sendClientMessage("Attempting to get you out of the void!");
            if (mode.getValue() == 0) {
                mc.player.moveVertical = 10;
                mc.player.jump();
            } else {
                mc.player.motionY = 0;
            }
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}
