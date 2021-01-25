package me.linus.momentum.util.player;

import me.linus.momentum.mixin.MixinInterface;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;

/**
 * @author linustouchtips
 * @since 12/30/2020
 */

public class FlightUtil implements MixinInterface {

    public static void horizontalFlight(double hSpeed) {
        double yaw = MotionUtil.calcMoveYaw(mc.player.rotationYaw);
        double motX = 0;
        double motZ = 0;

        yaw -= mc.player.moveStrafing * 90;

        if (mc.gameSettings.keyBindBack.isKeyDown() && !mc.gameSettings.keyBindForward.isKeyDown()) {
            motX = (-Math.sin(yaw) * hSpeed) * -1;
            motZ = (Math.cos(yaw) * hSpeed) * -1;
        }

        else if (mc.gameSettings.keyBindForward.isKeyDown()) {
            motX = -Math.sin(yaw) * hSpeed;
            motZ = Math.cos(yaw) * hSpeed;
        }

        mc.player.motionX = motX;
        mc.player.motionZ = motZ;

        if (mc.player.moveStrafing == 0 && mc.player.moveForward == 0) {
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
        }
    }

    public static void horizontalEntityFlight(double hSpeed) {
        Entity ridingEntity = mc.player.ridingEntity;
        double yaw = MotionUtil.calcMoveYaw(ridingEntity.rotationYaw);
        double motX = 0;
        double motZ = 0;

        yaw -= mc.player.moveStrafing * 90;

        if (mc.gameSettings.keyBindBack.isKeyDown() && !mc.gameSettings.keyBindForward.isKeyDown()) {
            motX = (-Math.sin(yaw) * hSpeed) * -1;
            motZ = (Math.cos(yaw) * hSpeed) * -1;
        }

        else if (mc.gameSettings.keyBindForward.isKeyDown()) {
            motX = -Math.sin(yaw) * hSpeed;
            motZ = Math.cos(yaw) * hSpeed;
        }

        ridingEntity.motionX = motX;
        ridingEntity.motionZ = motZ;

        if (mc.player.moveStrafing == 0 && mc.player.moveForward == 0) {
            ridingEntity.motionX = 0;
            ridingEntity.motionZ = 0;
        }
    }

    public static void fireworkElytra(double rotation) {
        if (mc.gameSettings.keyBindJump.isKeyDown() || mc.player.rotationPitch >= rotation) {
            InventoryUtil.switchToSlot(Items.FIREWORKS);
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        }
    }

    public static void freezeFlight(double fallSpeed, double yOffset) {
        if (mc.player.ridingEntity == null) {
            mc.player.motionX = 0.0;
            mc.player.motionY = 0.0;
            mc.player.motionZ = 0.0;
            mc.player.setVelocity(0f, 0f, 0f);
            mc.player.setPosition(mc.player.posX, mc.player.posY - fallSpeed + yOffset, mc.player.posZ);
        }

        else {
            mc.player.ridingEntity.motionX = 0.0;
            mc.player.ridingEntity.motionY = 0.0;
            mc.player.ridingEntity.motionZ = 0.0;
            mc.player.ridingEntity.setVelocity(0f, 0f, 0f);
            mc.player.ridingEntity.setPosition(mc.player.posX, mc.player.posY - fallSpeed + yOffset, mc.player.posZ);
        }
    }
}
