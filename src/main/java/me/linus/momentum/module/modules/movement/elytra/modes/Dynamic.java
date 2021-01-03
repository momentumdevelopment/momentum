package me.linus.momentum.module.modules.movement.elytra.modes;

import me.linus.momentum.module.modules.movement.ElytraFlight;
import me.linus.momentum.module.modules.movement.elytra.ElytraMode;
import me.linus.momentum.util.player.FlightUtil;
import me.linus.momentum.util.player.rotation.RotationUtil;

/**
 * @author linustouchtips
 * @since 12/29/2020
 */

public class Dynamic extends ElytraMode {

    boolean boostable = true;

    @Override
    public void onVerticalMovement() {
        if (mc.gameSettings.keyBindJump.isKeyDown())
            mc.player.motionY = ElytraFlight.ySpeed.getValue();

        else if (mc.gameSettings.keyBindSneak.isKeyDown())
            mc.player.motionY = ElytraFlight.ySpeed.getValue() * -1;
    }

    @Override
    public void onHorizontalMovement() {
        if (boostable)
            FlightUtil.horizontalFlight(ElytraFlight.hSpeed.getValue());
    }

    @Override
    public void onRubberband() {
        boostable = false;
    }

    @Override
    public void noMovement() {
        FlightUtil.freezeFlight(ElytraFlight.fallSpeed.getValue(), ElytraFlight.yOffset.getValue());
    }

    @Override
    public void onRotation() {
        if (ElytraFlight.rotationLock.getValue()) {
            RotationUtil.resetPitch(ElytraFlight.rotationNCP.getValue());
            RotationUtil.resetYaw(ElytraFlight.rotationNCP.getValue());
        }
    }
}
