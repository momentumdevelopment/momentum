package me.linus.momentum.module.modules.movement;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.mode.Mode;

/**
 * @author linustouchtips
 * @since 11/29/2020
 */

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", Category.MOVEMENT, "Automatically sprints");
    }

    public static Mode mode = new Mode("Mode", "Rage", "Legit");

    @Override
    public void setup() {
        addSetting(mode);
    }

    public void onUpdate() {
        if (nullCheck())
            return;

        switch (mode.getValue()) {
            case 0:
                if (!(mc.player.isSneaking()) && !(mc.player.collidedHorizontally) && mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() && mc.player.getFoodStats().getFoodLevel() > 6f)
                    mc.player.setSprinting(true);
                break;
            case 1:
                if (mc.gameSettings.keyBindForward.isKeyDown() && !(mc.player.collidedHorizontally) && !(mc.player.isSneaking()) && !(mc.player.isHandActive()) && mc.player.getFoodStats().getFoodLevel() > 6f)
                    mc.player.setSprinting(true);
                break;
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}
