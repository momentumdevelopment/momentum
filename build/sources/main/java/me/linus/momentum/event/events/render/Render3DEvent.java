package me.linus.momentum.event.events.render;

import me.linus.momentum.event.MomentumEvent;

public class Render3DEvent extends MomentumEvent {
    private final float partialTicks;

    public Render3DEvent(float ticks) {
        super();
        partialTicks = ticks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
