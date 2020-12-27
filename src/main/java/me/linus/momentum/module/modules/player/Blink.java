package me.linus.momentum.module.modules.player;

import me.linus.momentum.event.events.packet.PacketSendEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.util.client.external.MessageUtil;
import me.linus.momentum.util.world.WorldUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author linustouchtips & Hoosiers
 * @since 11/30/2020
 */

public class Blink extends Module {
    public Blink() {
        super("Blink", Category.PLAYER, "Cancels all player packets");
    }

    public static Checkbox playerModel = new Checkbox("Player Model", true);

    @Override
    public void setup() {
        addSetting(playerModel);
    }

    EntityOtherPlayerMP entity;
    Queue<Packet> packets = new ConcurrentLinkedQueue();

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        if (playerModel.getValue())
            WorldUtil.createFakePlayer(entity, true, true, true);

        MessageUtil.sendClientMessage("Cancelling all player packets!");
    }

    @Override
    public void onDisable() {
        if (nullCheck())
            return;

        if (entity != null)
            mc.world.removeEntity(entity);

        if (packets.size() > 0) {
            for (Packet packet : packets)
                mc.player.connection.sendPacket(packet);

            packets.clear();
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacket() instanceof CPacketChatMessage || event.getPacket() instanceof CPacketConfirmTeleport || event.getPacket() instanceof CPacketKeepAlive || event.getPacket() instanceof CPacketTabComplete || event.getPacket() instanceof CPacketClientStatus)
            return;

        if (nullCheck()) {
            packets.add(event.getPacket());
            event.setCanceled(true);
        }
    }
}
