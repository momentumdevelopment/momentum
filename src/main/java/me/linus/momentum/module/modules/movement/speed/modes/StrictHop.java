package me.linus.momentum.module.modules.movement.speed.modes;

import me.linus.momentum.event.events.player.MoveEvent;
import me.linus.momentum.module.modules.movement.Speed;
import me.linus.momentum.module.modules.movement.speed.SpeedMode;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.player.MotionUtil;

/**
 * @author linustouchtips
 * @since 02/03/2021
 */

public class StrictHop extends SpeedMode {

    @Override
    public void onMotionUpdate() {
        double xDist = mc.player.posX - mc.player.prevPosX;
        double zDist = mc.player.posZ - mc.player.prevPosZ;
        lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
    }

    @Override
    public void handleSpeed(MoveEvent event) {
        ++timerDelay;
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

        if (MathUtil.round(mc.player.posY - (double) ((int) mc.player.posY)) == MathUtil.round(0.138)) {
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
        float forward = mc.player.movementInput.moveForward;
        float strafe = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.rotationYaw;
        ticks++;

        if (ticks > 50)
            ticks = 0;

        if (forward == 0.0f && strafe == 0.0f) {
            event.setX(0.0);
            event.setZ(0.0);
        }

        else if (forward != 0.0f) {
            if (strafe >= 1.0f) {
                yaw += (float) (forward > 0.0f ? -45 : 45);
                strafe = 0.0f;
            }

            else if (strafe <= -1.0f) {
                yaw += (float) (forward > 0.0f ? 45 : -45);
                strafe = 0.0f;
            }

            if (forward > 0.0f)
                forward = 1.0f;


            else if (forward < 0.0f)
                forward = -1.0f;

        }

        double mx = Math.cos(Math.toRadians(yaw + 90.0f));
        double mz = Math.sin(Math.toRadians(yaw + 90.0f));
        event.setX((double) forward * moveSpeed * mx + (double) strafe * moveSpeed * mz);
        event.setZ((double) forward * moveSpeed * mz - (double) strafe * moveSpeed * mx);
        mc.player.stepHeight = 0.6f;

        if (forward == 0.0f && strafe == 0.0f) {
            event.setX(0.0);
            event.setZ(0.0);
        }
    }
}
