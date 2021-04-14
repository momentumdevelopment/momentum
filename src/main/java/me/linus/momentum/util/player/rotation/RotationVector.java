package me.linus.momentum.util.player.rotation;

import net.minecraft.util.math.Vec3d;

/**
 * @author linustouchtips
 * @since 03/18/2021
 */

public class RotationVector {

    public float[] rotation;
    public Vec3d vector;

    public RotationVector(Vec3d vector, float[] rotation) {
        this.vector = vector;
        this.rotation = rotation;
    }

    public Vec3d getVector() {
        return this.vector;
    }

    public float[] getRotation() {
        return this.rotation;
    }
}
