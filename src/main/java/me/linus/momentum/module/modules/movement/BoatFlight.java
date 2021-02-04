package me.linus.momentum.module.modules.movement;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.player.FlightUtil;
import net.minecraft.entity.Entity;

/**
 * @author linustouchtips
 * @since 12/07/2020
 */

public class BoatFlight extends Module {
    public BoatFlight() {
        super("BoatFlight", Category.MOVEMENT, "Allows you to fly on rideable entities");
    }

    public static Mode mode = new Mode("Mode", "Control", "Row");
    public static Slider hSpeed = new Slider("Horizontal Speed", 0.0D, 2.9D, 4.0D, 1);
    public static Slider ySpeed = new Slider("Vertical Speed", 0.0D, 1.0D, 3.0D, 1);
    public static Slider yOffset = new Slider("Y-Offset", 0.0D, 0.009D, 0.1D, 3);
    public static Slider fallSpeed = new Slider("Fall Speed", 0.0D, 0.0D, 0.1D, 3);

    public static Checkbox useTimer = new Checkbox("Use Timer", false);
    public static SubSlider timerTicks = new SubSlider(useTimer, "Timer Speed", 0.1D, 1.1D, 2.0D, 2);

    public static Checkbox disable = new Checkbox("Disable", true);
    public static SubCheckbox waterCancel = new SubCheckbox(disable, "In Liquid", true);
    public static SubCheckbox onUpward = new SubCheckbox(disable, "On Upward Motion", false);
    public static SubCheckbox onCollision = new SubCheckbox(disable, "On Collision", false);
    public static SubSlider lowestY = new SubSlider(disable, "Below Y-Level", 0.0D, 8.0D, 20.0D, 0);

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

        mc.player.ridingEntity.rotationYaw = mc.player.rotationYaw;

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

            FlightUtil.horizontalEntityFlight(hSpeed.getValue());
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

    public void flyTick() {
        if (mc.player.isRiding() && !useTimer.getValue())
            mc.timer.tickLength = 50;

        if (mc.player.isRiding() && useTimer.getValue())
            mc.timer.tickLength = (float) (50.0f / timerTicks.getValue());
    }

    public void disableCheck() {
        if (!disable.getValue())
            return;

        if (mc.player.posY <= lowestY.getValue())
            return;

        if ((mc.player.isInWater() || mc.player.isInLava()) && waterCancel.getValue())
            return;

        if (mc.player.rotationPitch >= 40 && onUpward.getValue())
            return;

        if (mc.player.isRowingBoat() && mc.gameSettings.keyBindJump.isKeyDown() && onUpward.getValue())
            return;

        if (mc.player.collidedHorizontally && onCollision.getValue())
            return;
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}
