package me.linus.momentum.module.modules.movement.elytra.modes;

import me.linus.momentum.module.modules.movement.ElytraFlight;
import me.linus.momentum.module.modules.movement.elytra.ElytraMode;
import me.linus.momentum.util.player.FlightUtil;
import me.linus.momentum.util.player.rotation.RotationUtil;

/**
 * @author linustouchtips
 * @since 12/29/2020
 */

public class Deer extends ElytraMode {

    @Override
    public void onVerticalMovement() {
        if (mc.gameSettings.keyBindJump.isKeyDown()) {
            FlightUtil.horizontalFlight(ElytraFlight.hSpeed.getValue());
            mc.player.motionY = ElytraFlight.ySpeed.getValue();
        }
    }

    @Override
    public void onRotation() {
        if (ElytraFlight.rotationLock.getValue()) {
            RotationUtil.resetPitch(ElytraFlight.rotationNCP.getValue());
            RotationUtil.resetYaw(ElytraFlight.rotationNCP.getValue());
        }
    }
}
