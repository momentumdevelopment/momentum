package me.linus.momentum.event.events.player;

import me.linus.momentum.event.MomentumEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

/**
 * @author linustouchtips
 * @since 01/01/2020
 * only used for overriding vanilla entityplayersp packet sending
 */

@Cancelable
public class RotationEvent extends MomentumEvent {

    float yaw;
    float pitch;
    
    public RotationEvent() {

    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}
