package me.linus.momentum.module.modules.movement;

import me.linus.momentum.event.events.packet.PacketReceiveEvent;
import me.linus.momentum.event.events.player.MoveEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.module.modules.movement.speed.SpeedMode;
import me.linus.momentum.module.modules.movement.speed.modes.CrystalHop;
import me.linus.momentum.module.modules.movement.speed.modes.GayHop;
import me.linus.momentum.module.modules.movement.speed.modes.PullHop;
import me.linus.momentum.module.modules.movement.speed.modes.SmoothHop;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Speed extends Module {
    public Speed() {
        super("Speed", Category.MOVEMENT, "Allows you to go faster");
    }

    public static Mode mode = new Mode("Mode", "SmoothHop", "GayHop", "CrystalHop", "PullHop");

    public static Slider multiplier = new Slider("Multiplier", 1.0D, 1.2D, 1.5D, 3);

    public static Checkbox useTimer = new Checkbox("Use Timer", false);
    public static SubSlider timerTicks = new SubSlider(useTimer, "Timer Speed", 0.0D, 1.12D, 2.0D, 2);

    public static Slider speed = new Slider("Speed", 0.0D, 0.26D, 1.0D, 2);
    public static Slider stepHeight = new Slider("Step Height", 0.0D, 0.6D, 2.0D, 1);

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
        addSetting(stepHeight);
        addSetting(jump);
        addSetting(disable);
    }

    SpeedMode speedMode = new SmoothHop();

    @Override
    public void onValueChange() {
        switch (mode.getValue()) {
            case -1:
                speedMode = new SmoothHop();
                break;
            case 0:
                speedMode = new GayHop();
                break;
            case 1:
                speedMode = new CrystalHop();
                break;
            case 2:
                speedMode = new PullHop();
                break;
        }
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if ((mc.player.isInWater() || mc.player.isInLava()) && inWater.getValue())
            return;

        mc.player.stepHeight = (float) stepHeight.getValue();

        speedMode.onMotionUpdate();
    }

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        if (!this.isEnabled())
            return;

        speedMode.handleSpeed(event);
    }

    @SubscribeEvent
    public void onPacketRecieve(PacketReceiveEvent event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            if (rubberband.getValue()) {
                this.disable();
                return;
            }

            speedMode.onRubberband();
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacket() instanceof SPacketExplosion && mc.player.getDistance(((SPacketExplosion) event.getPacket()).posX, ((SPacketExplosion) event.getPacket()).posY, ((SPacketExplosion) event.getPacket()).posZ) <= 6)
            speedMode.onKnockback();
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}