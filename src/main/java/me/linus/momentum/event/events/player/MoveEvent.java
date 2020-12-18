package me.linus.momentum.event.events.player;

import me.linus.momentum.event.MomentumEvent;
import net.minecraft.entity.MoverType;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

@Cancelable
public class MoveEvent extends MomentumEvent {
    private MoverType type;
    private double x;
    private double y;
    private double z;

    public MoveEvent(MoverType type, double x, double y, double z) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public MoverType getType() {
        return type;
    }

    public void setType(MoverType type) {
        this.type = type;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
