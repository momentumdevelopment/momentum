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

    private static final Mode mode = new Mode("Mode", "Rage", "Legit");

    @Override
    public void setup() {
        addSetting(mode);
    }

    public void onUpdate() {
        if (mode.getValue() == 1) {
            try {
                if (mc.gameSettings.keyBindForward.isKeyDown() && !(mc.player.collidedHorizontally) && !(mc.player.isSneaking()) && !(mc.player.isHandActive()) && mc.player.getFoodStats().getFoodLevel() > 6f) {
                    mc.player.setSprinting(true);
                }
            } catch (Exception ignored) {}
        } else {
            try {
                if (!(mc.player.isSneaking()) && !(mc.player.collidedHorizontally) && mc.player.getFoodStats().getFoodLevel() > 6f && mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown()) {
                    mc.player.setSprinting(true);
                }
            } catch (Exception ignored) {}
        }
    }

    @Override
    public String getHUDData() {
        return mode.getMode(mode.getValue());
    }
}
