package me.linus.momentum.module.modules.movement.speed.modes;

import me.linus.momentum.event.events.player.MoveEvent;
import me.linus.momentum.module.modules.movement.speed.SpeedMode;

/**
 * @author linustouchtips
 * @since 02/03/2021
 */

public class PullHop extends SpeedMode {

    @Override
    public void onMotionUpdate() {
        double xDist = mc.player.posX - mc.player.prevPosX;
        double zDist = mc.player.posZ - mc.player.prevPosZ;
        lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
    }

    @Override
    public void handleSpeed(MoveEvent event) {
        if (mc.player.onGround) {
            mc.player.motionY = 0.3994f;
            event.setY(0.3994f);
        }

        else
            mc.player.motionY--;
    }
}
