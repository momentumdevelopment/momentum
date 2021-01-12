package me.linus.momentum.module.modules.movement;

import me.linus.momentum.event.events.packet.PacketReceiveEvent;
import me.linus.momentum.event.events.packet.PacketSendEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.module.modules.movement.elytra.ElytraMode;
import me.linus.momentum.module.modules.movement.elytra.modes.*;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.player.MotionUtil;
import me.linus.momentum.util.world.Timer;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author linustouchtips
 * @since 12/03/2020
 * @updated 12/29/2020
 */

// TODO: Implement acold's 2b2t elytra fly
public class ElytraFlight extends Module {
    public ElytraFlight() {
        super("ElytraFlight", Category.MOVEMENT, "Allows you to fly faster on an elytra");
    }

    public static Mode mode = new Mode("Mode", "Control", "MotionControl", "Pitch", "PitchNCP", "Firework", "Deer", "Dynamic", "DynamicNCP", "Glide", "Vanilla", "Packet");
    public static SubSlider rotationNCP = new SubSlider(mode, "NCP Rotation", 0.0D, 30.0D, 90.0D, 1);
    public static SubCheckbox rotationLock = new SubCheckbox(mode, "Rotation Lock", false);

    public static Mode boost = new Mode("Boost", "None", "Firework", "Accelerate");

    public static Slider hSpeed = new Slider("Glide Speed", 0.0D, 2.1D, 3.0D, 1);
    public static Slider ySpeed = new Slider("Rise Speed", 0.0D, 1.0D, 3.0D, 1);
    public static Slider yOffset = new Slider("Y-Offset", 0.0D, 0.009D, 0.1D, 3);
    public static Slider fallSpeed = new Slider("Fall Speed", 0.0D, 0.0D, 0.1D, 3);

    public static Checkbox takeoff = new Checkbox("Auto-Takeoff", true);
    public static SubCheckbox takeoffTimer = new SubCheckbox(takeoff, "Takeoff Timer", false);
    public static SubSlider takeoffTicks = new SubSlider(takeoff, "Takeoff Timer Ticks", 0.1D, 0.3D, 1.0D, 2);

    public static Checkbox useTimer = new Checkbox("Use Timer", false);
    public static SubSlider timerTicks = new SubSlider(useTimer, "Timer Ticks", 0.1D, 1.1D, 2.0D, 2);

    public static Checkbox pitchSpoof = new Checkbox("Pitch Spoof", false);

    public static Checkbox disable = new Checkbox("Disable", true);
    public static SubCheckbox waterCancel = new SubCheckbox(disable, "In Liquid", true);
    public static SubCheckbox onUpward = new SubCheckbox(disable, "On Upward Motion", false);
    public static SubCheckbox onCollision = new SubCheckbox(disable, "On Collision", false);
    public static SubCheckbox onRubberband = new SubCheckbox(disable, "On Rubberband", false);
    public static SubSlider lowestY = new SubSlider(disable, "Below Y-Level", 0.0D, 8.0D, 20.0D, 0);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(boost);
        addSetting(hSpeed);
        addSetting(ySpeed);
        addSetting(yOffset);
        addSetting(fallSpeed);
        addSetting(takeoff);
        addSetting(useTimer);
        addSetting(pitchSpoof);
        addSetting(disable);
    }

    ElytraMode elytraMode;
    Timer takeOffTimer = new Timer();

    @Override
    public void onDisable() {
        mc.timer.tickLength = 50f;
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (takeoff.getValue()) {
            if (mc.player.onGround)
                mc.player.jump();
            else if (!mc.player.isElytraFlying() && takeOffTimer.passed(5, Timer.Format.Ticks))
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        }

        disableCheck();
        flyTick();

        switch (mode.getValue()) {
            case 0:
                elytraMode = new Control();
                break;
            case 1:
                elytraMode = new MotionControl();
                break;
            case 2:
                elytraMode = new Pitch();
                break;
            case 3:
                elytraMode = new PitchNCP();
                break;
            case 4:
                elytraMode = new Firework();
                break;
            case 5:
                elytraMode = new Deer();
                break;
            case 6:
                elytraMode = new Dynamic();
                break;
            case 7:
                elytraMode = new DynamicNCP();
                break;
            case 8:
                elytraMode = new Glide();
                break;
            case 9:
                elytraMode = new Vanilla();
                break;
            case 10:
                elytraMode = new Packet();
                break;
        }

        if (mc.player.isElytraFlying()) {
            if (!MotionUtil.isMoving())
                elytraMode.noMovement();

            elytraMode.onAscendingMovement();
            elytraMode.onVerticalMovement();
            elytraMode.onHorizontalMovement();
            elytraMode.onRotation();
        }
    }

    public void flyTick() {
        if (!mc.player.isElytraFlying() && takeoffTimer.getValue())
            mc.timer.tickLength = (float) (50.0f / takeoffTicks.getValue());

        if (mc.player.isElytraFlying() && !useTimer.getValue())
            mc.timer.tickLength = 50;

        if (mc.player.isElytraFlying() && useTimer.getValue())
            mc.timer.tickLength = (float) (50.0f / timerTicks.getValue());

    }

    public void disableCheck() {
        if (!disable.getValue())
            return;

        if (mc.player.posY <= lowestY.getValue())
            return;

        if ((mc.player.isInWater() || mc.player.isInLava()) && waterCancel.getValue())
            return;

        if (mc.player.rotationPitch >= rotationNCP.getValue() && onUpward.getValue())
            return;

        if (mc.player.isElytraFlying() && mc.gameSettings.keyBindJump.isKeyDown() && onUpward.getValue())
            return;

        if (mc.player.collidedHorizontally && onCollision.getValue())
            return;
    }

    @SubscribeEvent
    public void onPacketRecieve(PacketReceiveEvent event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook && mc.player.isElytraFlying()) {
            if (disable.getValue() && onRubberband.getValue())
                return;

            elytraMode.onRubberband();
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacket() instanceof CPacketPlayer && pitchSpoof.getValue() && mc.player.isElytraFlying()) {
            if (event.getPacket() instanceof CPacketPlayer.PositionRotation && pitchSpoof.getValue()) {
                CPacketPlayer.PositionRotation rotation = (CPacketPlayer.PositionRotation) event.getPacket();
                mc.getConnection().sendPacket(new CPacketPlayer.Position(rotation.x, rotation.y, rotation.z, rotation.onGround));
                event.setCanceled(true);
            }

            else if (event.getPacket() instanceof CPacketPlayer.Rotation && pitchSpoof.getValue())
                event.setCanceled(true);
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}