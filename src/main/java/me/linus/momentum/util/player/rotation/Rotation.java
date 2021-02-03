package me.linus.momentum.util.player.rotation;

import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.modules.combat.AutoCrystal;
import me.linus.momentum.util.world.Timer;

/**
 * @author linustouchtips
 * @since 12/31/2020
 */

public class Rotation implements MixinInterface {

    public float yaw;
    public float pitch;
    public RotationMode mode;
    public Timer rotationStay = new Timer();

    public Rotation(float yaw, float pitch, RotationMode mode) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.mode = mode;

        rotationStay.reset();
    }

    // updates player rotations here
    public void updateRotations() {
        switch (this.mode) {
            case Packet:
                mc.player.rotationYawHead = this.yaw;
                mc.player.renderYawOffset = this.yaw;
                break;
            case Legit:
                mc.player.rotationYaw = this.yaw;
                mc.player.rotationPitch = this.pitch;
                break;
        }
    }

    public void restoreRotation() {
        try {
            this.yaw = mc.player.rotationYaw;
            this.pitch = mc.player.rotationPitch;
            mc.player.rotationYawHead = mc.player.rotationYaw;

            rotationStay.reset();
        } catch (Exception e) {

        }
    }

    public boolean requiresUpdate() {
        return rotationStay.passed((long) AutoCrystal.rotateDelay.getValue(), Timer.Format.System);
    }

    public enum RotationMode {
        Packet,
        Legit
    }
}
