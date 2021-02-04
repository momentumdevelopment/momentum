package me.linus.momentum.module.modules.movement.speed.modes;

import me.linus.momentum.event.events.player.MoveEvent;
import me.linus.momentum.module.modules.movement.Speed;
import me.linus.momentum.module.modules.movement.speed.SpeedMode;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.player.MotionUtil;

public class NCPHop extends SpeedMode {

    @Override
    public void onMotionUpdate() {
        double xDist = mc.player.posX - mc.player.prevPosX;
        double zDist = mc.player.posZ - mc.player.prevPosZ;
        lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
    }

    @Override
    public void handleSpeed(MoveEvent event) {
        ++ this.timerDelay;
        this.timerDelay %= 5;

        if (MotionUtil.isMoving()) {
            mc.player.motionX *= 1.0199999809265137D;
            mc.player.motionZ *= 1.0199999809265137D;
        }

        if (mc.player.onGround && MotionUtil.isMoving())
            this.level = 2;

        if (MathUtil.roundDouble(mc.player.posY - (double) ((int) mc.player.posY), 3) == MathUtil.roundDouble(0.138D, 3)) {
            mc.player.motionY -= 0.08D;
            event.setY(event.getY() - 0.09316090325960147D);
            mc.player.posY -= 0.09316090325960147D;
        }

        if (this.level != 1 || mc.player.moveForward == 0.0F && mc.player.moveStrafing == 0.0F) {
            if (this.level == 2) {
                this.level = 3;
                mc.player.motionY = 0.399399995803833D;
                event.setY(0.399399995803833D);
                this.moveSpeed *= 2.149D;
            }
                
            else if (this.level == 3) {
                this.level = 4;
                double difference = 0.66D * (this.lastDist - Speed.speed.getValue());
                this.moveSpeed = this.lastDist - difference;
            }
                
            else {
                if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0D, mc.player.motionY, 0.0D)).size() > 0 || mc.player.collidedVertically)
                    this.level = 1;

                this.moveSpeed = this.lastDist - this.lastDist / 159.0D;
            }
        }
            
        else {
            this.level = 2;
            this.moveSpeed = 1.35D * Speed.speed.getValue() - 0.01D;
        }

        this.moveSpeed = Math.max(this.moveSpeed, Speed.speed.getValue());
        float forward = mc.player.movementInput.moveForward;
        float strafe = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.rotationYaw;
        if (forward == 0.0F && strafe == 0.0F) {
            event.setX(0.0D);
            event.setZ(0.0D);
        }

        else if (forward != 0.0F) {
            if (strafe >= 1.0F) {
                yaw += (float) (forward > 0.0F ? -45 : 45);
                strafe = 0.0F;
            }

            else if (strafe <= -1.0F) {
                yaw += (float) (forward > 0.0F ? 45 : -45);
                strafe = 0.0F;
            }

            if (forward > 0.0F)
                forward = 1.0F;

            else if (forward < 0.0F)
                forward = -1.0F;
        }

        event.setX((double) forward * this.moveSpeed * Math.cos(Math.toRadians(yaw + 90.0F)) + (double) strafe * this.moveSpeed * Math.sin(Math.toRadians(yaw + 90.0F)));
        event.setZ((double) forward * this.moveSpeed * Math.sin(Math.toRadians(yaw + 90.0F)) - (double) strafe * this.moveSpeed * Math.cos(Math.toRadians(yaw + 90.0F)));

        mc.player.stepHeight = 0.6F;

        if (forward == 0.0F && strafe == 0.0F) {
            event.setX(0.0D);
            event.setZ(0.0D);
        }
    }
}
