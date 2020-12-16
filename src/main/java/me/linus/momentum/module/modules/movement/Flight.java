package me.linus.momentum.module.modules.movement;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.world.PlayerUtil;

/**
 * @author linustouchtips
 * @since 11/30/2020
 */

public class Flight extends Module {
    public Flight() {
        super("Flight", Category.MOVEMENT, "Allows you to fly");
    }

    private static Mode mode = new Mode("Mode", "Creative", "Vanilla");
    public static Slider hSpeed = new Slider("Horizontal Speed", 0.0D, 1.0D, 3.0D, 1);
    public static Slider ySpeed = new Slider("Vertical Speed", 0.0D, 1.0D, 3.0D, 1);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(hSpeed);
        addSetting(ySpeed);
    }

    @Override
    public void onEnable() {
       if (nullCheck())
           return;

       if (mode.getValue() == 1)
           mc.player.capabilities.isFlying = true;
    }

    @Override
    public void onDisable() {
        mc.player.capabilities.isFlying = false;
    }

    @Override
    public void onUpdate() {
        if (mode.getValue() == 0) {
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.player.motionY = ySpeed.getValue();
            } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.player.motionY = (ySpeed.getValue() * -1);
            } else mc.player.motionY = 0;

            double yaw = PlayerUtil.calcMoveYaw(mc.player.rotationYaw);
            double motX = 0;
            double motZ = 0;

            yaw -= mc.player.moveStrafing * 90;

            if (mc.gameSettings.keyBindBack.isKeyDown() && !mc.gameSettings.keyBindForward.isKeyDown()) {
                motX = (-Math.sin(yaw) * hSpeed.getValue()) * -1;
                motZ = (Math.cos(yaw) * hSpeed.getValue()) * -1;
            } else if (mc.gameSettings.keyBindForward.isKeyDown()) {
                motX = -Math.sin(yaw) * hSpeed.getValue();
                motZ = Math.cos(yaw) * hSpeed.getValue();
            }

            mc.player.motionX = motX;
            mc.player.motionZ = motZ;

            if (mc.player.moveStrafing == 0 && mc.player.moveForward == 0) {
                mc.player.motionX = 0;
                mc.player.motionZ = 0;
            }
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}
