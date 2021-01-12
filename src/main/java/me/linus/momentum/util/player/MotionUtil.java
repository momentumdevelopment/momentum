package me.linus.momentum.util.player;

import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.world.Timer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

import java.text.DecimalFormat;

/**
 * @author linustouchtips
 * @since 12/29/2020
 */

public class MotionUtil implements MixinInterface {
    static double prevPosX;
    static double prevPosZ;
    static Timer timer = new Timer();

    public static boolean isMoving() {
        return (mc.player.moveForward != 0.0D || mc.player.moveStrafing != 0.0D);
    }

    public static boolean hasMotion() {
        return mc.player.motionX != 0.0 && mc.player.motionZ != 0.0 && mc.player.motionY != 0.0;
    }

    public static double calcMoveYaw(float yawIn) {
        float moveForward = getRoundedMovementInput(mc.player.movementInput.moveForward);
        float moveString = getRoundedMovementInput(mc.player.movementInput.moveStrafe);

        float strafe = 90 * moveString;
        if (moveForward != 0f)
            strafe *= moveForward * 0.5f;
        else
            strafe *= 1f;

        float yaw = yawIn - strafe;
        if (moveForward < 0f)
            yaw -= 180;
        else
            yaw -= 0;

        return Math.toRadians(yaw);
    }

    static float getRoundedMovementInput(Float input) {
        if (input > 0)
            input = 1f;
        else if (input < 0)
            input = -1f;
        else
            input = 0f;

        return input;
    }

    public static void setSpeed(EntityLivingBase entity, double speed) {
        double[] dir = directionSpeed(speed);
        entity.motionX = dir[0];
        entity.motionZ = dir[1];
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;

        if (mc.player != null && mc.player.isPotionActive(Potion.getPotionById(1)))
            baseSpeed *= 1.0 + 0.2 * (mc.player.getActivePotionEffect(Potion.getPotionById(1)).getAmplifier() + 1);

        return baseSpeed;
    }

    public static double[] directionSpeed(double speed) {
        float forward = mc.player.movementInput.moveForward;
        float side = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();

        if (forward != 0.0f) {
            if (side > 0.0f)
                yaw += (float) (forward > 0.0f ? -45 : 45);
            else if (side < 0.0f) {
                yaw += (float) (forward > 0.0f ? 45 : -45);

                side = 0.0f;
                if (forward > 0.0f)
                    forward = 1.0f;
                else if (forward < 0.0f)
                    forward = -1.0f;

            }
        }

        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double posX = (double) forward * speed * cos + (double) side * speed * sin;
        double posZ = (double) forward * speed * sin - (double) side * speed * cos;

        return new double[] {
                posX, posZ
        };
    }

    public static String getSpeed() {
        if (timer.passed(1000, Timer.Format.System)) {
            prevPosX = mc.player.prevPosX;
            prevPosZ = mc.player.prevPosZ;
        }

        String formattedString = new DecimalFormat("#.#").format(MathUtil.roundAvoid((MathHelper.sqrt(MathUtil.square(mc.player.posX - prevPosX) + MathUtil.square(mc.player.posZ - prevPosZ)) / 1000.0f) / (0.05f / 3600.0f), 1));

        if (!formattedString.contains("."))
            formattedString += ".0";

        return " " + formattedString + TextFormatting.GRAY + "km/h";
    }

    public String format(double input) {
        String result = new DecimalFormat("#.#").format(input);

        if (!result.contains("."))
            result += ".0";

        return result;
    }
}
