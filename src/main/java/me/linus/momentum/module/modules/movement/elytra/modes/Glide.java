package me.linus.momentum.module.modules.movement.elytra.modes;

import me.linus.momentum.module.modules.movement.ElytraFlight;
import me.linus.momentum.module.modules.movement.elytra.ElytraMode;
import me.linus.momentum.util.player.FlightUtil;
import me.linus.momentum.util.player.rotation.RotationUtil;

/**
 * @author linustouchtips
 * @since 12/31/2020
 */

public class Glide extends ElytraMode {

    @Override
    public void onHorizontalMovement() {
        FlightUtil.horizontalFlight(ElytraFlight.hSpeed.getValue());
    }

    @Override
    public void onRotation() {
        if (ElytraFlight.rotationLock.getValue()) {
            RotationUtil.resetPitch(ElytraFlight.rotationNCP.getValue());
            RotationUtil.resetYaw(ElytraFlight.rotationNCP.getValue());
        }
    }
}
