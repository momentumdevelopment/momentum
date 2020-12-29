package me.linus.momentum.module.modules.movement;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.world.MotionUtil;
import me.linus.momentum.util.world.PlayerUtil;
import net.minecraft.entity.Entity;

/**
 * @author linustouchtips
 * @since 12/07/2020
 */

public class BoatFlight extends Module {
    public BoatFlight() {
        super("BoatFlight", Category.MOVEMENT, "Allows you to fly on rideable entities");
    }

    private static final Mode mode = new Mode("Mode", "Control", "Row");
    public static Slider hSpeed = new Slider("Horizontal Speed", 0.0D, 2.9D, 4.0D, 1);
    public static Slider ySpeed = new Slider("Vertical Speed", 0.0D, 1.0D, 3.0D, 1);
    public static Slider yOffset = new Slider("Y-Offset", 0.0D, 0.009D, 0.1D, 3);
    public static Slider fallSpeed = new Slider("Fall Speed", 0.0D, 0.0D, 0.1D, 3);

    private static final Checkbox useTimer = new Checkbox("Use Timer", false);
    private static final SubSlider timerTicks = new SubSlider(useTimer, "Timer Speed", 0.1D, 1.1D, 2.0D, 2);

    private static final Checkbox disable = new Checkbox("Disable", true);
    private static final SubCheckbox waterCancel = new SubCheckbox(disable, "In Liquid", true);
    private static final SubCheckbox onUpward = new SubCheckbox(disable, "On Upward Motion", false);
    private static final SubCheckbox onCollision = new SubCheckbox(disable, "On Collision", false);
    private static final SubSlider lowestY = new SubSlider(disable, "Below Y-Level", 0.0D, 8.0D, 20.0D, 0);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(hSpeed);
        addSetting(ySpeed);
        addSetting(yOffset);
        addSetting(fallSpeed);
        addSetting(useTimer);
        addSetting(disable);
    }

    @Override
    public void onDisable() {
        mc.timer.tickLength = 50;
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        disableCheck();
        flyTick();

        switch (mode.getValue()) {
            case 0:
                flyControl();
                break;
            case 1:
                flySwim();
                break;
        }
    }

    public void flyControl() {
        if (mc.player.isRiding()) {
            Entity ridingEntity = mc.player.ridingEntity;

            ridingEntity.motionY += yOffset.getValue();
            ridingEntity.setVelocity(0, -fallSpeed.getValue(), 0);

            if (mc.gameSettings.keyBindJump.isKeyDown())
                ridingEntity.motionY = ySpeed.getValue();

            else if (mc.gameSettings.keyBindSneak.isKeyDown())
                ridingEntity.motionY = (ySpeed.getValue() * -1);

            accelerateEntity();
        }
    }

    public void flySwim() {
        if (mc.player.isRiding()) {
            Entity ridingEntity = mc.player.ridingEntity;

            ridingEntity.motionY += yOffset.getValue();
            ridingEntity.setVelocity(0, -fallSpeed.getValue(), 0);

            if (mc.gameSettings.keyBindJump.isKeyDown())
                ridingEntity.motionY = ySpeed.getValue();

            else if (mc.gameSettings.keyBindSneak.isKeyDown())
                ridingEntity.motionY = (ySpeed.getValue() * -1);
        }
    }

    public void accelerateEntity() {
        Entity ridingEntity = mc.player.ridingEntity;
        double yaw = MotionUtil.calcMoveYaw(ridingEntity.rotationYaw);
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

        ridingEntity.motionX = motX;
        ridingEntity.motionZ = motZ;

        if (mc.player.moveStrafing == 0 && mc.player.moveForward == 0) {
            ridingEntity.motionX = 0;
            ridingEntity.motionZ = 0;
        }
    }

    public void flyTick() {
        if (mc.player.isRiding() && !useTimer.getValue())
            mc.timer.tickLength = 50;

        if (mc.player.isRiding() && useTimer.getValue())
            mc.timer.tickLength = (float) (50.0f / timerTicks.getValue());

    }

    public void disableCheck() {
        if (!disable.getValue())
            return;

        if (mc.player.posY <= lowestY.getValue()) {
            this.disable();
            return;
        }

        if ((mc.player.isInWater() || mc.player.isInLava()) && waterCancel.getValue()) {
            this.disable();
            return;
        }

        if (mc.player.rotationPitch >= 40 && onUpward.getValue()) {
            this.disable();
            return;
        }

        if (mc.player.isRowingBoat() && mc.gameSettings.keyBindJump.isKeyDown() && onUpward.getValue()) {
            this.disable();
            return;
        }

        if (mc.player.collidedHorizontally && onCollision.getValue()) {
            this.disable();
            return;
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}
