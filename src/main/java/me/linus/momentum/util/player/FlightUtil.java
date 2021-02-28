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

    public static void horizontalFlight(double speed) {
        MotionUtil.setMoveSpeed(speed, 0.6f);
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
