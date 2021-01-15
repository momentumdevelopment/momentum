package me.linus.momentum.module.modules.movement;

import me.linus.momentum.event.events.packet.PacketReceiveEvent;
import me.linus.momentum.event.events.player.MoveEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.module.ModuleManager;
import me.linus.momentum.module.modules.movement.speed.SpeedMode;
import me.linus.momentum.module.modules.movement.speed.modes.*;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.keybind.SubKeybind;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.player.MotionUtil;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Speed extends Module {
    public Speed() {
        super("Speed", Category.MOVEMENT, "Allows you to go faster");
    }

    public static Mode mode = new Mode("Mode", "SmoothHop", "StrictHop", "LowHop", "CrystalHop", "Y-Port", "OnGround");
    public static SubKeybind modeKey = new SubKeybind(mode, "ModeSwitch Key", -2);
    public static SubCheckbox strict = new SubCheckbox(mode, "Strict", false);
    public static SubCheckbox enableStep = new SubCheckbox(mode, "Use Step", false);

    public static Slider multiplier = new Slider("Multiplier", 0.0D, 0.03D, 0.3D, 3);

    public static Checkbox useTimer = new Checkbox("Use Timer", false);
    public static SubSlider timerTicks = new SubSlider(useTimer, "Timer Speed", 0.0D, 1.12D, 2.0D, 2);

    public static Slider speed = new Slider("Speed", 0.0D, 0.27D, 1.0D, 2);
    public static Checkbox jump = new Checkbox("Jump", true);

    public static Checkbox disable = new Checkbox("Disable", true);
    public static SubCheckbox inWater = new SubCheckbox(disable, "Disable in Liquid", true);
    public static SubCheckbox rubberband = new SubCheckbox(disable, "Disable on Rubberband", false);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(multiplier);
        addSetting(useTimer);
        addSetting(speed);
        addSetting(jump);
        addSetting(disable);
    }

    SpeedMode speedMode;

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (Keyboard.isKeyDown(modeKey.getKey()))
            mode.setMode(mode.nextMode());

        if ((mc.player.isInWater() || mc.player.isInLava() && inWater.getValue()))
            return;

        if (enableStep.getValue())
            mc.player.stepHeight = 2.5f;

        if (jump.getValue() && mc.player.onGround && MotionUtil.isMoving() && mode.getValue() != 3)
            mc.player.jump();

        switch (mode.getValue()) {
            case 0:
                speedMode = new SmoothHop();
                break;
            case 1:
                speedMode = new StrictHop();
                break;
            case 2:
                speedMode = new LowHop();
                break;
            case 3:
                speedMode = new CrystalHop();
                break;
            case 5:
                speedMode = new OnGround();
                break;
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.timer.tickLength = 50;
        mc.player.stepHeight = 0.5f;
    }

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        if (!this.isEnabled())
            return;

        speedMode.onMove(event);
    }

    @SubscribeEvent
    public void onPacketRecieve(PacketReceiveEvent event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            if (rubberband.getValue())
                this.disable();

            speedMode.onRubberband();
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}