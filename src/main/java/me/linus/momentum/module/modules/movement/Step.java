package me.linus.momentum.module.modules.movement;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.player.MotionUtil;
import net.minecraft.network.play.client.CPacketPlayer;

/**
 * @author linustouchtips & seasnail8169
 * @since 12/03/2020
 */

public class Step extends Module {
    public Step() {
        super("Step", Category.MOVEMENT, "Increases player step height");
    }

    public static Mode mode = new Mode("Mode", "Teleport", "Spider", "Vanilla");
    public static Slider height = new Slider("Height", 0.0D, 2.0D, 2.5D, 1);

    public static Mode disable = new Mode("Disable", "Never", "Completion", "Unsafe");

    public static Checkbox useTimer = new Checkbox("Use Timer", false);
    public static SubSlider timerTicks = new SubSlider(useTimer, "Timer Speed", 0.1D, 0.5D, 2.0D, 2);

    public static Checkbox entityStep = new Checkbox("Entity Step", false);

    public static Checkbox pause = new Checkbox("Pause", true);
    public static SubCheckbox sneakPause = new SubCheckbox(pause, "When Sneaking", false);
    public static SubCheckbox waterPause = new SubCheckbox(pause, "When in Liquid", true);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(height);
        addSetting(disable);
        addSetting(useTimer);
        addSetting(entityStep);
        addSetting(pause);
    }

    double[] forwardStep;
    double originalHeight;

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        originalHeight = mc.player.posY;
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (!mc.player.collidedHorizontally)
            return;

        if (mc.player.isOnLadder() || mc.player.movementInput.jump)
            return;

        if ((mc.player.isInWater() || mc.player.isInLava()) && waterPause.getValue())
            return;

        if (useTimer.getValue())
            mc.timer.tickLength = (float) (50.0f / timerTicks.getValue());

        if (entityStep.getValue() && mc.player.isRiding())
            mc.player.ridingEntity.stepHeight = (float) height.getValue();

        if (mc.player.isSneaking() && sneakPause.getValue())
            return;

        forwardStep = MotionUtil.getMoveSpeed(0.1);

        if (getStepHeight().equals(StepHeight.Unsafe)) {
            if (disable.getValue() == 2)
                this.disable();

            return;
        }

        switch (mode.getValue()) {
            case 0:
                stepTeleport();
                break;
            case 1:
                stepSpider();
                break;
            case 2:
                stepVanilla();
                break;
        }

        if (mc.player.posY > originalHeight + getStepHeight().height && disable.getValue() == 1)
            this.disable();
    }

    public void stepTeleport() {
        updateStepPackets(getStepHeight().stepArray);
        mc.player.setPosition(mc.player.posX, mc.player.posY + getStepHeight().height, mc.player.posZ);
    }

    public void stepSpider() {
       updateStepPackets(getStepHeight().stepArray);

        mc.player.motionY = 0.2;
        mc.player.fallDistance = 0;
    }

    public void stepVanilla() {
        mc.player.setPosition(mc.player.posX, mc.player.posY + getStepHeight().height, mc.player.posZ);
    }

    public void updateStepPackets(double[] stepArray) {
        for (int i = 0; i < stepArray.length; i++) {
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + stepArray[i], mc.player.posZ, mc.player.onGround));
        }
    }

    public StepHeight getStepHeight() {
        if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(forwardStep[0], 1.0, forwardStep[1])).isEmpty() && !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(forwardStep[0], 0.6, forwardStep[1])).isEmpty())
            return StepHeight.One;
        else if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(forwardStep[0], 1.6, forwardStep[1])).isEmpty() && !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(forwardStep[0], 1.4, forwardStep[1])).isEmpty())
            return StepHeight.OneHalf;
        else if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(forwardStep[0], 2.1, forwardStep[1])).isEmpty() && !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(forwardStep[0], 1.9, forwardStep[1])).isEmpty())
            return StepHeight.Two;
        else if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(forwardStep[0], 2.6, forwardStep[1])).isEmpty() && !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(forwardStep[0], 2.4, forwardStep[1])).isEmpty())
            return StepHeight.TwoHalf;
        else
            return StepHeight.Unsafe;
    }

    public enum StepHeight {
        One(1, new double[] { 0.42, 0.753 }),
        OneHalf(1.5, new double[] { 0.42, 0.75, 1.0, 1.16, 1.23, 1.2 }),
        Two(2, new double[] { 0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43 }),
        TwoHalf(2.5, new double[] { 0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907 }),
        Unsafe(3, new double[] { 0 });

        double[] stepArray;
        double height;

        StepHeight(double height, double[] stepArray) {
            this.height = height;
            this.stepArray = stepArray;
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}
