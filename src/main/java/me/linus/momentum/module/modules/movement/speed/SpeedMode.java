package me.linus.momentum.module.modules.movement.speed;

import me.linus.momentum.event.events.player.MoveEvent;
import me.linus.momentum.mixin.MixinInterface;

/**
 * @author linustouchtips
 * @since 02/03/2021
 */

public class SpeedMode implements MixinInterface {

    public double moveSpeed = 0.0;
    public int level = 4;
    public int ticks = 0;
    public double lastDist = 0.0;
    public int timerDelay;
    public boolean boostable = true;

    public void onMotionUpdate() {

    }

    public void handleSpeed(MoveEvent event) {

    }

    public void onRubberband() {
        moveSpeed = 0.0;
        level = 4;
        timerDelay = 0;
        moveSpeed = 0.2873;
        lastDist = 0;
    }

    public void onKnockback() {

    }
}
