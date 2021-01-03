package me.linus.momentum.module.modules.movement.elytra.modes;

import me.linus.momentum.module.modules.movement.ElytraFlight;
import me.linus.momentum.module.modules.movement.elytra.ElytraMode;
import me.linus.momentum.util.player.FlightUtil;
import me.linus.momentum.util.player.rotation.RotationUtil;

public class Vanilla extends ElytraMode {

    @Override
    public void onHorizontalMovement() {
        mc.player.capabilities.setFlySpeed((float) (ElytraFlight.hSpeed.getValue() / 23));
        mc.player.capabilities.isFlying = true;

        if (mc.player.capabilities.isCreativeMode)
            return;

        mc.player.capabilities.allowFlying = true;
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
