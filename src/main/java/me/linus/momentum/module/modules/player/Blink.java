package me.linus.momentum.module.modules.player;

import me.linus.momentum.event.events.packet.PacketSendEvent;
import me.linus.momentum.managers.notification.Notification.Type;
import me.linus.momentum.managers.notification.Notification;
import me.linus.momentum.managers.notification.NotificationManager;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.util.world.WorldUtil;
import net.minecraft.network.INetHandler;
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

    Queue<Packet<?>> packets = new ConcurrentLinkedQueue<>();

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        if (playerModel.getValue())
            WorldUtil.createFakePlayer(null, true, true, true, true, 6640);

        NotificationManager.addNotification(new Notification("Cancelling all player packets!", Type.Info));
    }

    @Override
    public void onDisable() {
        if (nullCheck())
            return;

        mc.world.removeEntityFromWorld(69420);

        for (Packet<? extends INetHandler> packet : packets) {
            mc.player.connection.sendPacket(packet);
        }

        packets.clear();
    }

    @SubscribeEvent
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacket() instanceof CPacketChatMessage || event.getPacket() instanceof CPacketConfirmTeleport || event.getPacket() instanceof CPacketKeepAlive || event.getPacket() instanceof CPacketTabComplete || event.getPacket() instanceof CPacketClientStatus)
            return;

        packets.add(event.getPacket());
        event.setCanceled(true);
    }
}
