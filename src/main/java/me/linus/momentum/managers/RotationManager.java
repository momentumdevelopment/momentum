package me.linus.momentum.managers;

import me.linus.momentum.event.events.packet.PacketSendEvent;
import me.linus.momentum.event.events.player.RotationEvent;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.util.player.rotation.Rotation.RotationMode;
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

    public static float yawleftOver = 0;
    public static float pitchleftOver = 0;

    public static int tick = 5;

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        rotationQueue.stream().sorted(Comparator.comparing(rotation -> rotation.rotationPriority.getPriority()));

        if (currentRotation != null)
            currentRotation = null;

        if (!rotationQueue.isEmpty()) {
            currentRotation = rotationQueue.poll();
            currentRotation.updateRotations();
        }

        tick++;
    }

    @SubscribeEvent
    public void onRotate(RotationEvent event) {
        try {
            if (currentRotation != null && currentRotation.mode.equals(RotationMode.Packet)) {
                event.setCanceled(true);

                if (tick == 1) {
                    event.setYaw(currentRotation.yaw + yawleftOver);
                    event.setPitch(currentRotation.pitch + pitchleftOver);
                }

                else {
                    event.setYaw(currentRotation.yaw);
                    event.setPitch(currentRotation.pitch);
                }
            }
        } catch (Exception ignored) {

        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketSendEvent event) {
        if (currentRotation != null && !rotationQueue.isEmpty() && event.getPacket() instanceof CPacketPlayer) {
            if (((CPacketPlayer) event.getPacket()).rotating)
                serverRotation = new Rotation(((CPacketPlayer) event.getPacket()).yaw, ((CPacketPlayer) event.getPacket()).pitch, RotationMode.Packet, RotationPriority.Lowest);
        }
    }

    public static void resetTicks() {
        tick = 0;
    }
}
