package me.linus.momentum.module.modules.movement;

import me.linus.momentum.event.events.player.MoveEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.world.PlayerUtil;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

/**
 * @author linustouchtips
 * @since 12/07/2020
 */

public class PacketFlight extends Module {
    public PacketFlight() {
        super("PacketFlight", Category.MOVEMENT, "Allows you to fly using packets");
    }

    private static Mode mode = new Mode("Mode","Preserve", "Upward", "Downward");
    public static SubSlider upSpeed = new SubSlider(mode,"Upward Speed", 0.0D, 0.062D, 0.1D, 3);
    public static SubSlider downSpeed = new SubSlider(mode,"Fall Speed", 0.0D, 0.04D, 0.1D, 3);
    public static SubSlider randomIter = new SubSlider(mode,"Random Iteration", 0.0D, 100000.0D, 200000.0D, 0);

    private static Mode phase = new Mode("Phase","Full", "Semi", "None");
    public static SubCheckbox noMove = new SubCheckbox(phase, "Unnatural Movement", false);
    public static SubCheckbox noClip = new SubCheckbox(phase, "NoClip", false);

    public static Slider speed = new Slider("Speed", 0.0D, 0.18D, 1.0D, 2);

    private static Checkbox gravity = new Checkbox("Gravity", true);
    public static SubSlider fallSpeed = new SubSlider(gravity,"Fall Speed", 0.0D, 0.04D, 0.1D, 3);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(phase);
        addSetting(speed);
        addSetting(gravity);
    }

    public static int lastTeleportId = -1;
    private Random random = new Random();
    int ticksFlying = 0;

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        lastTeleportId = -1;
    }

    @Override
    public void onDisable() {
        mc.player.noClip = false;
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        mc.player.setVelocity(0, 0, 0);

        if (!mc.player.onGround && ticksFlying++ >= 4 && gravity.getValue()) {
            ticksFlying = 0;
            mc.player.motionY = -fallSpeed.getValue();
        }

        if (mc.player.movementInput.moveForward != 0 && mc.player.movementInput.moveStrafe != 0) {
            double[] directionalSpeed = PlayerUtil.directionSpeed(speed.getValue());

            double posX = mc.player.posX + directionalSpeed[0];
            double posZ = mc.player.posZ + directionalSpeed[1];

            new CPacketPlayer.PositionRotation(posX, mc.player.posY, posZ, mc.player.rotationYaw, mc.player.rotationPitch, false);
        }

        if (mc.player.movementInput.jump) {
            mc.player.motionY = upSpeed.getValue();

            if (!mc.player.onGround && ticksFlying++ >= 20) {
                ticksFlying = 0;
                mc.player.motionY = -0.032;
            }
        }

        double spoofX = mc.player.posX + mc.player.motionX;
        double spoofY = mc.player.posY + mc.player.motionY;
        double spoofZ = mc.player.posZ + mc.player.motionZ;

        switch (mode.getValue()) {
            case 0:
                spoofX += random.nextInt((int) randomIter.getValue());
                spoofZ += random.nextInt((int) randomIter.getValue());
                break;
            case 1:
                spoofY += 1337.69;
                break;
            case 2:
                spoofY -= 1337.69;
                break;
        }

        if (noClip.getValue())
            mc.player.noClip = true;

        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX + mc.player.motionX, mc.player.posY + mc.player.motionY, mc.player.posZ + mc.player.motionZ, mc.player.onGround));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(spoofX, spoofY, spoofZ, mc.player.onGround));

        if (lastTeleportId != -1)
            mc.player.connection.sendPacket(new CPacketConfirmTeleport(lastTeleportId++));

        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.onGround));
    }

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        if (noMove.getValue())
            event.setCanceled(true);
    }

    @Override
    public String getHUDData() {
        return mode.getMode(mode.getValue());
    }
}
