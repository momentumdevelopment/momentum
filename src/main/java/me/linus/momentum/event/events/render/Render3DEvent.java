package me.linus.momentum.event.events.render;

import me.linus.momentum.event.MomentumEvent;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Render3DEvent extends MomentumEvent {
    private final float partialTicks;

    public Render3DEvent(float ticks) {
        partialTicks = ticks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
