package me.linus.momentum.module.modules.movement.speed.modes;

import me.linus.momentum.event.events.player.MoveEvent;
import me.linus.momentum.module.modules.movement.Speed;
import me.linus.momentum.module.modules.movement.speed.SpeedMode;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.player.MotionUtil;

/**
 * @author linustouchtips
 * @since 01/15/2021
 */

public class StrictHop extends SpeedMode {

    @Override
    public void onMove(MoveEvent event) {
        timerDelay++;
        timerDelay %= 5;

        if (timerDelay != 0)
            mc.timer.tickLength = 50f;

        else if (MotionUtil.hasMotion()) {
            mc.timer.tickLength = 50f / 1.3f;
            mc.player.motionX *= 1.02f;
            mc.player.motionZ *= 1.02f;
        }

        if (mc.player.onGround && MotionUtil.hasMotion())
            level = 2;

        if (MathUtil.round(mc.player.posY - (double)((int)mc.player.posY)) == MathUtil.round(0.138)) {
            mc.player.motionY -= 0.08;
            event.setY(event.getY() - 0.09316090325960147);
            mc.player.posY -= 0.09316090325960147;
        }

        if (level == 1 && (mc.player.moveForward != 0.0f || mc.player.moveStrafing != 0.0f)) {
            level = 2;
            moveSpeed = 1.38 * Speed.speed.getValue() - 0.01;
        }

        else if (level == 2) {
            level = 3;
            mc.player.motionY = 0.3994f;
            event.setY(0.3994f);
            moveSpeed *= 2.149;
        }

        else if (level == 3) {
            level = 4;
            double difference = 0.66 * (lastDist - Speed.speed.getValue());
            moveSpeed = lastDist - difference;
        }

        else {
            if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, mc.player.motionY, 0.0)).size() > 0 || mc.player.collidedVertically)
                level = 1;

            moveSpeed = lastDist - lastDist / 159.0;
        }

        moveSpeed = Math.max(moveSpeed, Speed.speed.getValue());
        moveSpeed = Math.min(moveSpeed, ticks > 25 ? 0.449 : 0.433);
        ticks++;

        if (ticks > 50)
            ticks = 0;

        if (mc.player.movementInput.moveForward == 0.0f && mc.player.movementInput.moveStrafe == 0.0f) {
            event.setX(0.0);
            event.setZ(0.0);
        }

        else if (mc.player.movementInput.moveForward != 0.0f) {
            if (mc.player.movementInput.moveStrafe >= 1.0f) {
                mc.player.rotationYaw += (float) (mc.player.movementInput.moveForward > 0.0f ? -45 : 45);
                mc.player.movementInput.moveStrafe = 0.0f;
            }

            else if (mc.player.movementInput.moveStrafe <= -1.0f) {
                mc.player.rotationYaw += (float) (mc.player.movementInput.moveForward > 0.0f ? 45 : -45);
                mc.player.movementInput.moveStrafe = 0.0f;
            }

            if (mc.player.movementInput.moveForward > 0.0f)
                mc.player.movementInput.moveForward = 1.0f;

            else if (mc.player.movementInput.moveForward < 0.0f)
                mc.player.movementInput.moveForward = -1.0f;
        }

        event.setX((double) mc.player.movementInput.moveForward * moveSpeed * Math.cos(Math.toRadians(mc.player.rotationYaw + 90.0f)) + (double) mc.player.movementInput.moveStrafe * moveSpeed * Math.sin(Math.toRadians(mc.player.rotationYaw + 90.0f)));
        event.setZ((double) mc.player.movementInput.moveForward * moveSpeed * Math.sin(Math.toRadians(mc.player.rotationYaw + 90.0f)) - (double) mc.player.movementInput.moveStrafe * moveSpeed * Math.cos(Math.toRadians(mc.player.rotationYaw + 90.0f)));
        mc.player.stepHeight = 0.6f;

        if (mc.player.movementInput.moveForward == 0.0f && mc.player.movementInput.moveStrafe == 0.0f) {
            event.setX(0.0);
            event.setZ(0.0);
        }
    }

    @Override
    public void onRubberband() {
        moveSpeed = 0.2873;
        lastDist = 0.0;
        moveSpeed = 0.0;
        level = 4;
        timerDelay = 0;
    }
}
