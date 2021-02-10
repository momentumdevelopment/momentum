package me.linus.momentum.managers;

import me.linus.momentum.event.events.packet.PacketSendEvent;
import me.linus.momentum.event.events.player.RotationEvent;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.util.client.MessageUtil;
import me.linus.momentum.util.player.rotation.Rotation;
import me.linus.momentum.util.player.rotation.RotationPriority;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Comparator;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author linustouchtips
 * @since 01/30/2021
 */

public class RotationManager implements MixinInterface {
    public RotationManager() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static LinkedBlockingQueue<Rotation> rotationQueue = new LinkedBlockingQueue<>();
    public static Rotation serverRotation = null;
    public static Rotation currentRotation = null;

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        rotationQueue.stream().sorted(Comparator.comparing(rotation -> rotation.rotationPriority.getPriority()));

        if (currentRotation != null && currentRotation.requiresUpdate()) {
            currentRotation.restoreRotation();
            currentRotation = null;
        }

        if (currentRotation == null && !rotationQueue.isEmpty()) {
            currentRotation = rotationQueue.poll();
            currentRotation.updateRotations();
        }
    }

    @SubscribeEvent
    public void onRotate(RotationEvent event) {
        try {
            MessageUtil.sendClientMessage("Rotating to " + currentRotation.yaw + ", " + currentRotation.pitch + " with a priority of " + currentRotation.rotationPriority.toString());

            if (currentRotation != null && currentRotation.mode.equals(Rotation.RotationMode.Packet)) {
                event.setCanceled(true);
                event.setYaw(currentRotation.yaw);
                event.setPitch(currentRotation.pitch);
            }
        } catch (Exception e) {

        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketSendEvent event) {
        if (currentRotation != null && !rotationQueue.isEmpty() && event.getPacket() instanceof CPacketPlayer) {
            if (((CPacketPlayer) event.getPacket()).rotating)
                serverRotation = new Rotation(((CPacketPlayer) event.getPacket()).yaw, ((CPacketPlayer) event.getPacket()).pitch, Rotation.RotationMode.Packet, RotationPriority.Lowest);
        }
    }
}
