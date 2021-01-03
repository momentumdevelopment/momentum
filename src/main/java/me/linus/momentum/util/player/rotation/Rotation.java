package me.linus.momentum.util.player.rotation;

import me.linus.momentum.mixin.MixinInterface;

/**
 * @author linustouchtips
 * @since 12/31/2020
 */

public class Rotation implements MixinInterface {

    public float yaw;
    public float pitch;

    public Rotation(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }
}
