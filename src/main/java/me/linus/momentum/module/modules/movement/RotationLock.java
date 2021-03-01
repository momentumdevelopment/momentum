package me.linus.momentum.module.modules.movement;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;

import java.util.Objects;

/**
 * @author linustouchtips
 * @since 12/03/2020
 */

public class RotationLock extends Module {
    public RotationLock() {
        super("RotationLock", Category.MOVEMENT, "Locks player rotation");
    }

    public static Checkbox playerYaw = new Checkbox("Yaw", true);
    public static Checkbox playerPitch = new Checkbox("Pitch", true);

    @Override
    public void setup() {
        addSetting(playerYaw);
        addSetting(playerPitch);
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        int angle = 90;

        float yaw = mc.player.rotationYaw;
        float pitch = mc.player.rotationPitch;

        yaw = Math.round(yaw / angle) * angle;
        pitch = Math.round(pitch / angle) * angle;

        if (playerYaw.getValue())
            mc.player.rotationYaw = yaw;

        if (playerPitch.getValue())
            mc.player.rotationPitch = pitch;

        if (mc.player.isRiding())
            Objects.requireNonNull(mc.player.getRidingEntity()).rotationYaw = yaw;
    }
}
