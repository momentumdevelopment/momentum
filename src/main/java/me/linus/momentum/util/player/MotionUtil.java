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
    static DecimalFormat formatter = new DecimalFormat("#.#");
    static float roundedForward = getRoundedMovementInput(mc.player.movementInput.moveForward);
    static float roundedStrafing = getRoundedMovementInput(mc.player.movementInput.moveStrafe);
    private static double prevPosX;
    private static double prevPosZ;
    private static Timer timer = new Timer();

    public static boolean isMoving() {
        return (mc.player.moveForward != 0.0D || mc.player.moveStrafing != 0.0D);
    }

    public static boolean hasMotion() {
        return mc.player.motionX != 0.0 && mc.player.motionZ != 0.0 && mc.player.motionY != 0.0;
    }

    public static double calcMoveYaw(float yawIn) {
        float moveForward = roundedForward;
        float moveString = roundedStrafing;

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

    private static float getRoundedMovementInput(Float input) {
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
        if (mc.player != null && mc.player.isPotionActive(Potion.getPotionById(1))) {
            int amplifier = mc.player.getActivePotionEffect(Potion.getPotionById(1)).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }

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

        double deltaX = mc.player.posX - prevPosX;
        double deltaZ = mc.player.posZ - prevPosZ;

        float distance = MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ);

        double KMH = MathUtil.roundAvoid((distance / 1000.0f) / (0.05f / 3600.0f), 1);

        String formattedString = formatter.format(KMH);

        if (!formattedString.contains("."))
            formattedString += ".0";

        String bps = " " + formattedString + TextFormatting.GRAY + "km/h";

        return bps;
    }

    public String format(double input) {
        String result = formatter.format(input);

        if (!result.contains("."))
            result += ".0";

        return result;
    }
}
