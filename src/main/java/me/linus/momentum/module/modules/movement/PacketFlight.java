package me.linus.momentum.module.modules.movement;

import me.linus.momentum.event.events.packet.PacketReceiveEvent;
import me.linus.momentum.event.events.packet.PacketSendEvent;
import me.linus.momentum.event.events.player.PushEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.world.PlayerUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Set;

/**
 * @author linustouchtips
 * @since 12/07/2020
 */

public class PacketFlight extends Module {
    public PacketFlight() {
        super("PacketFlight", Category.MOVEMENT, "Allows you to fly using packets");
    }

    private static Mode mode = new Mode("Mode","Phase", "Upward", "Downward");
    public static SubSlider upSpeed = new SubSlider(mode,"Upward Speed", 0.0D, 0.0625D, 0.1D, 4);
    public static SubSlider packetIteration = new SubSlider(mode,"Packet Iteration", 0.0D, 20.0D, 40.0D, 0);

    private static Mode phase = new Mode("Phase","Full", "Semi", "None");
    public static SubCheckbox packet = new SubCheckbox(phase, "Packet", false);
    public static SubCheckbox noMove = new SubCheckbox(phase, "Unnatural Movement", false);
    public static SubCheckbox noClip = new SubCheckbox(phase, "NoClip", true);

    public static Checkbox pause = new Checkbox("Pause", false);
    public static SubCheckbox pauseRotation = new SubCheckbox(pause, "Server Rotations", false);

    public static Slider speed = new Slider("Speed", 0.0D, 0.18D, 1.0D, 2);

    private static Checkbox gravity = new Checkbox("Gravity", true);
    public static SubSlider fallSpeed = new SubSlider(gravity,"Fall Speed", 0.0D, 0.04D, 0.1D, 3);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(phase);
        addSetting(pause);
        addSetting(speed);
        addSetting(gravity);

        teleport = true;
    }

    private boolean teleport;
    public static int lastTeleportId = 0;
    private Set<CPacketPlayer> packets;
    private int posLookPackets;

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        posLookPackets = 0;
        packets.clear();
    }

    @Override
    public void onDisable() {
        if (nullCheck())
            return;

        posLookPackets = 0;
        packets.clear();
        mc.player.noClip = false;
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        switch (mode.getValue()) {
            case 0:
                phasePlayer();
                break;
        }
    }

    public void phasePlayer() {
        final double[] dirSpeed = PlayerUtil.directionSpeed(teleport ? 0.02250000089406967 : 0.02239999920129776);
        mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX + dirSpeed[0], mc.player.posY + (mc.gameSettings.keyBindJump.isKeyDown() ? (teleport ? 0.0625 : 0.0624) : 1.0E-8) - (mc.gameSettings.keyBindSneak.isKeyDown() ? (teleport ? 0.0625 : 0.0624) : 2.0E-8), mc.player.posZ + dirSpeed[1], mc.player.rotationYaw, mc.player.rotationPitch, false));
        mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX, -1337.0, mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch, true));
        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        mc.player.setPosition(mc.player.posX + dirSpeed[0], mc.player.posY + (mc.gameSettings.keyBindJump.isKeyDown() ? (teleport ? 0.0625 : 0.0624) : 1.0E-8) - (mc.gameSettings.keyBindSneak.isKeyDown() ? (teleport ? 0.0625 : 0.0624) : 2.0E-8), mc.player.posZ + dirSpeed[1]);
        teleport = !teleport;
        final EntityPlayerSP player = mc.player;
        final EntityPlayerSP player2 = mc.player;
        final EntityPlayerSP player3 = mc.player;
        final double motionX = 0.0;
        player3.motionZ = motionX;
        player2.motionY = motionX;
        player.motionX = motionX;

        if (noClip.getValue())
            mc.player.noClip = teleport;
    }

    @SubscribeEvent
    public void onPush(PushEvent event) {
        event.setCanceled(true);
    }

    @SubscribeEvent
    public void onPacketReceive(PacketReceiveEvent event) {
        if (noMove.getValue() && event.getPacket() instanceof SPacketPlayerPosLook) {
            final SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
            if (mc.player.isEntityAlive() && mc.world.isBlockLoaded(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)) && !(mc.currentScreen instanceof GuiDownloadTerrain)) {
                if (lastTeleportId <= 0) 
                    lastTeleportId = packet.getTeleportId();
                
                if (pause.getValue() && pauseRotation.getValue())
                    return;

                if (pause.getValue() && posLookPackets >= packetIteration.getValue()) {
                    posLookPackets = 0;
                    event.setCanceled(true);
                }

                posLookPackets++;
            }
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketSendEvent event) {
        if (packet.getValue() && event.getPacket() instanceof CPacketPlayer) {
            final CPacketPlayer packetPlayer = (CPacketPlayer) event.getPacket();
            if (packets.contains(packetPlayer)) {
                packets.remove(packetPlayer);
            }
            
            else {
                event.setCanceled(true);
            }
        }
    }

    @Override
    public String getHUDData() {
        return mode.getMode(mode.getValue());
    }
}
