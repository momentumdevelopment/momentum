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
    }

    // updates player rotations here
    public void updateRotations(RotationMode mode) {
        switch (mode) {
            case Packet:
                mc.player.rotationYawHead = this.yaw;
                break;
            case Legit:
                mc.player.rotationYaw = this.yaw;
                mc.player.rotationPitch = this.pitch;
                break;
        }
    }

    public void restoreRotation() {
        this.yaw = mc.player.rotationYaw;
        this.pitch = mc.player.rotationPitch;
    }

    public enum RotationMode {
        Packet,
        Legit
    }
}
