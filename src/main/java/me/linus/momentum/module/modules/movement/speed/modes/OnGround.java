package me.linus.momentum.module.modules.movement.speed.modes;

import me.linus.momentum.event.events.player.MoveEvent;
import me.linus.momentum.module.modules.movement.Speed;
import me.linus.momentum.module.modules.movement.speed.SpeedMode;
import me.linus.momentum.util.player.MotionUtil;

/**
 * @author linustouchtips
 * @since 01/15/2021
 */

public class OnGround extends SpeedMode {

    @Override
    public void onMove(MoveEvent event) {
        if (mc.player.onGround && MotionUtil.isMoving()) {
            mc.player.motionX *= Speed.speed.getValue() / 2;
            mc.player.motionZ *= Speed.speed.getValue() / 2;
        }
    }
}
