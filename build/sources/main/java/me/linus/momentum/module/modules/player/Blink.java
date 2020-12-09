package me.linus.momentum.module.modules.player;

import me.linus.momentum.event.events.packet.PacketSendEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.util.client.MessageUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author linustouchtips
 * @since 11/30/2020
 */

public class Blink extends Module {
    public Blink() {
        super("Blink", Category.PLAYER, "Cancels all player packets");
    }

    EntityOtherPlayerMP entity;
    private final Queue<Packet> packets = new ConcurrentLinkedQueue();

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        if (mc.player != null) {
            entity = new EntityOtherPlayerMP(mc.world, mc.getSession().getProfile());
            entity.copyLocationAndAnglesFrom(mc.player);
            entity.inventory.copyInventory(mc.player.inventory);
            entity.rotationYaw = mc.player.rotationYaw;
            entity.rotationYawHead = mc.player.rotationYawHead;
            mc.world.addEntityToWorld(667, entity);
        }

        MessageUtil.sendClientMessage("Cancelling all player packets!");
    }

    @Override
    public void onDisable() {
        if (nullCheck())
            return;

        if (entity != null)
            mc.world.removeEntity(entity);

        if (packets.size() > 0 && mc.player != null) {
            for (Packet packet : packets) {
                mc.player.connection.sendPacket(packet);
            }

            packets.clear();
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketSendEvent event) {
        Packet packet = event.getPacket();

        if (packet instanceof CPacketChatMessage || packet instanceof CPacketConfirmTeleport || packet instanceof CPacketKeepAlive || packet instanceof CPacketTabComplete || packet instanceof CPacketClientStatus) {
            return;
        }

        if (mc.player == null || mc.player.isDead) {
            packets.add(packet);
            event.setCanceled(true);
        }
    }
}
