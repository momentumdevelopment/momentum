package me.linus.momentum.module.modules.movement;

import me.linus.momentum.event.events.packet.PacketSendEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.client.system.MathUtil;
import me.linus.momentum.util.combat.RotationUtil;
import me.linus.momentum.util.world.InventoryUtil;
import me.linus.momentum.util.world.PlayerUtil;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author linustouchtips
 * @since 12/03/2020
 */

public class ElytraFlight extends Module {
    public ElytraFlight() {
        super("ElytraFlight", Category.MOVEMENT, "Allows you to fly faster on an elytra");
    }

    private static final Mode mode = new Mode("Mode", "Control", "Pitch", "NCP-Pitch", "Highway");
    public static final SubSlider rotationNCP = new SubSlider(mode, "NCP Rotation", 0.0D, 30.0D, 90.0D, 1);
    private static final SubCheckbox pitchReset = new SubCheckbox(mode, "Pitch Reset", false);

    private static final Mode boost = new Mode("Boost", "None", "Firework", "Infinite");

    public static Slider hSpeed = new Slider("Glide Speed", 0.0D, 2.1D, 3.0D, 1);
    public static Slider ySpeed = new Slider("Vertical Speed", 0.0D, 1.0D, 3.0D, 1);
    public static Slider yOffset = new Slider("Y-Offset", 0.0D, 0.009D, 0.1D, 3);
    public static Slider fallSpeed = new Slider("Fall Speed", 0.0D, 0.0D, 0.1D, 3);

    private static final Checkbox takeoffTimer = new Checkbox("Takeoff Timer", false);
    private static final SubSlider takeoffTicks = new SubSlider(takeoffTimer, "Takeoff Timer Speed", 0.1D, 0.5D, 1.0D, 2);

    private static final Checkbox useTimer = new Checkbox("Use Timer", false);
    private static final SubSlider timerTicks = new SubSlider(useTimer, "Timer Speed", 0.1D, 1.1D, 2.0D, 2);

    private static final Checkbox pitchSpoof = new Checkbox("Pitch Spoof", false);
    private static final Checkbox autoTakeoff = new Checkbox("Auto-Takeoff", false);

    private static final Checkbox disable = new Checkbox("Disable", true);
    private static final SubCheckbox waterCancel = new SubCheckbox(disable, "In Liquid", true);
    private static final SubCheckbox onUpward = new SubCheckbox(disable, "On Upward Motion", false);
    private static final SubCheckbox onCollision = new SubCheckbox(disable, "On Collision", false);
    private static final SubSlider lowestY = new SubSlider(disable, "Below Y-Level", 0.0D, 8.0D, 20.0D, 0);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(boost);
        addSetting(hSpeed);
        addSetting(ySpeed);
        addSetting(yOffset);
        addSetting(fallSpeed);
        addSetting(takeoffTimer);
        addSetting(useTimer);
        addSetting(pitchSpoof);
        addSetting(autoTakeoff);
        addSetting(disable);
    }

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        if (autoTakeoff.getValue())
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
    }

    @Override
    public void onDisable() {
        mc.timer.tickLength = 50f;
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        disableCheck();
        flyTick();

        if (boost.getValue() == 1)
            fireworkElytra();

        mc.player.fallDistance = 0;
        switch (mode.getValue()) {
            case 0:
                flyControl();
                break;
            case 1:
                flyPitch();
                break;
            case 2:
                flyNCPPitch();
                break;
            case 3:
                flyHighway();
                break;
        }
    }

    public void flyControl() {
        if (mc.player.isElytraFlying()) {
            if (!PlayerUtil.isMoving())
                PlayerUtil.freezePlayer(fallSpeed.getValue(), yOffset.getValue());

            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.player.motionY = ySpeed.getValue();
            } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.player.motionY = (ySpeed.getValue() * -1);
            }

            if (pitchReset.getValue())
                RotationUtil.resetPitch(rotationNCP.getValue());

            accelerateElytra();
        }
    }

    public void flyHighway() {
        if (mc.player.isElytraFlying()) {
            if (!PlayerUtil.isMoving())
                PlayerUtil.freezePlayer(fallSpeed.getValue(), yOffset.getValue());

            RotationUtil.resetPitch(rotationNCP.getValue());
            RotationUtil.resetYaw(rotationNCP.getValue());

            accelerateElytra();
        }
    }

    public void flyPitch() {
        if (mc.player.isElytraFlying()) {
            if (!PlayerUtil.isMoving())
                PlayerUtil.freezePlayer(fallSpeed.getValue(), yOffset.getValue());

            mc.player.motionY = (-MathUtil.degToRad(mc.player.rotationPitch)) * mc.player.movementInput.moveForward;

            if (pitchReset.getValue())
                RotationUtil.resetPitch(rotationNCP.getValue());

            accelerateElytra();
        }
    }

    public void flyNCPPitch() {
        if (mc.player.isElytraFlying()) {
            if (!PlayerUtil.isMoving())
                PlayerUtil.freezePlayer(fallSpeed.getValue(), yOffset.getValue());

            RotationUtil.resetPitch(rotationNCP.getValue());

            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.player.rotationPitch = (float) -rotationNCP.getValue();
            } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.player.rotationPitch = (float) rotationNCP.getValue();
            }

            accelerateElytra();
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

    public void accelerateElytra() {
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

    public void fireworkElytra() {
        if (mc.gameSettings.keyBindJump.isKeyDown() || mc.player.rotationPitch >= rotationNCP.getValue()) {
            InventoryUtil.switchToSlot(Items.FIREWORKS);
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        }
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

        if (mc.player.rotationPitch >= rotationNCP.getValue() && onUpward.getValue()) {
            this.disable();
            return;
        }

        if (mc.player.isElytraFlying() && mc.gameSettings.keyBindJump.isKeyDown() && onUpward.getValue()) {
            this.disable();
            return;
        }

        if (mc.player.collidedHorizontally && onCollision.getValue()) {
            this.disable();
            return;
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacket() instanceof CPacketPlayer && pitchSpoof.getValue()) {
            if (!mc.player.isElytraFlying())
                return;

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