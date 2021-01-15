package me.linus.momentum.module.modules.movement.speed;

import me.linus.momentum.event.events.player.MoveEvent;
import me.linus.momentum.mixin.MixinInterface;

/**
 * @author linustouchtips
 * @since 01/15/2021
 */

public class SpeedMode implements MixinInterface {

    public double moveSpeed = 0.0;
    public int level = 4;
    public int ticks = 0;
    public double lastDist = 0.0;
    public int timerDelay;

    public void onMove(MoveEvent event) {

    }

    public void onRubberband() {

    }
}
