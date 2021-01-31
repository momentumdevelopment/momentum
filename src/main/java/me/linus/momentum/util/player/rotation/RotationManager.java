package me.linus.momentum.util.player.rotation;

import me.linus.momentum.event.events.packet.PacketSendEvent;
import me.linus.momentum.mixin.MixinInterface;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

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
    public static Rotation currentRotation = null;

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
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
    public void onPacketSend(PacketSendEvent event) {
        if (currentRotation != null && !rotationQueue.isEmpty() && event.getPacket() instanceof CPacketPlayer) {
            ((CPacketPlayer) event.getPacket()).yaw = currentRotation.yaw;
            ((CPacketPlayer) event.getPacket()).pitch = currentRotation.pitch;
        }
    }
}
