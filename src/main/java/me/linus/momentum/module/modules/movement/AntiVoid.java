package me.linus.momentum.module.modules.movement;

import me.linus.momentum.managers.notification.Notification;
import me.linus.momentum.managers.notification.Notification.Type;
import me.linus.momentum.managers.notification.NotificationManager;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.mode.Mode;

/**
 * @author linustouchtips
 * @since 12/03/2020
 */

public class AntiVoid extends Module {
    public AntiVoid() {
        super("AntiVoid", Category.MOVEMENT, "Pulls you out of the void");
    }

    public static Mode mode = new Mode("Mode", "Float", "Freeze", "SlowFall", "Teleport", "Timer");

    @Override
    public void setup() {
        addSetting(mode);
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (mc.player.posY <= 0) {
            NotificationManager.addNotification(new Notification("Attempting to get you out of the void!", Type.Info));

            switch (mode.getValue()) {
                case 0:
                    mc.player.motionY = 0.5;
                    break;
                case 1:
                    mc.player.motionY = 0;
                    break;
                case 2:
                    mc.player.motionY /= 4;
                    break;
                case 3:
                    mc.player.setPosition(mc.player.posX, mc.player.posY + 2, mc.player.posZ);
                    break;
                case 4:
                    mc.timer.tickLength = 50f / 0.1f;
                    break;
            }
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}
