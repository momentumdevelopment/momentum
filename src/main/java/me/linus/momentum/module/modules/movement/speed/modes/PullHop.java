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
        lastDist = Math.sqrt(Math.pow(mc.player.posX - mc.player.prevPosX, 2) + Math.pow(mc.player.posZ - mc.player.prevPosZ, 2));
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
