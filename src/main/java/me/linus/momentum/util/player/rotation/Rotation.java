package me.linus.momentum.util.player.rotation;

import me.linus.momentum.mixin.MixinInterface;

/**
 * @author linustouchtips
 * @since 12/31/2020
 */

public class Rotation implements MixinInterface {

    public float yaw;
    public float pitch;
    public boolean requiresUpdate;

    public Rotation(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.requiresUpdate = false;
    }

    // updates player rotations here
    public void updateRotations(int mode) {
        this.requiresUpdate = true;
        switch (mode) {
            case 0:
                mc.player.rotationYawHead = this.yaw;
                break;
            case 1:
                mc.player.rotationYaw = this.yaw;
                mc.player.rotationPitch = this.pitch;
                break;
            case 2:
                break;
        }
    }

    public void restoreRotation() {
        this.yaw = mc.player.rotationYaw;
        this.pitch = mc.player.rotationPitch;
        this.requiresUpdate = false;
    }
}
