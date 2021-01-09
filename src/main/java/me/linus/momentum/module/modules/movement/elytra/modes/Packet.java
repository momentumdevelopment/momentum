package me.linus.momentum.module.modules.movement.elytra.modes;

import me.linus.momentum.module.modules.movement.ElytraFlight;
import me.linus.momentum.module.modules.movement.elytra.ElytraMode;
import me.linus.momentum.util.player.FlightUtil;
import me.linus.momentum.util.player.rotation.RotationUtil;
import net.minecraft.network.play.client.CPacketEntityAction;

/**
 * @author linustouchtips
 * @since 12/31/2020
 */

public class Packet extends ElytraMode {

    @Override
    public void onHorizontalMovement() {
        mc.player.capabilities.setFlySpeed((float) (ElytraFlight.hSpeed.getValue() / 23));
        mc.player.capabilities.isFlying = true;

        if (mc.player.capabilities.isCreativeMode)
            return;

        mc.player.capabilities.allowFlying = true;
        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
    }

    @Override
    public void noMovement() {
        FlightUtil.freezeFlight(ElytraFlight.fallSpeed.getValue(), 0);
    }

    @Override
    public void onRotation() {
        if (ElytraFlight.rotationLock.getValue()) {
            RotationUtil.resetPitch(ElytraFlight.rotationNCP.getValue());
            RotationUtil.resetYaw(ElytraFlight.rotationNCP.getValue());
        }
    }
}
