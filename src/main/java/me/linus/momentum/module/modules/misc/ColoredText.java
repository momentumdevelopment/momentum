package me.linus.momentum.module.modules.misc;

import me.linus.momentum.event.events.packet.PacketSendEvent;
import me.linus.momentum.module.Module;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author linustouchtips
 * @since 11/30/2020
 */

public class ColoredText extends Module {
    public ColoredText() {
        super("ColoredText", Category.MISC, "Automatically colors your messages green");
    }

    @SubscribeEvent
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacket() instanceof CPacketChatMessage) {
            if (((CPacketChatMessage) event.getPacket()).getMessage().startsWith("/") || ((CPacketChatMessage) event.getPacket()).getMessage().startsWith("!") || ((CPacketChatMessage) event.getPacket()).getMessage().startsWith("$") || ((CPacketChatMessage) event.getPacket()).getMessage().startsWith("?") || ((CPacketChatMessage) event.getPacket()).getMessage().startsWith(".") || ((CPacketChatMessage) event.getPacket()).getMessage().startsWith(","))
                return;

            String suffix = " > ";
            String s = suffix + ((CPacketChatMessage) event.getPacket()).getMessage();
            int ok = 0;

            if (s.length() > 255)
                ok = s.length() - 255;

            s = s.substring(0, s.length() - ok);
            ((CPacketChatMessage) event.getPacket()).message = s;
        }
    }
}
