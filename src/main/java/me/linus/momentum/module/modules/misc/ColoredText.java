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

            String coloredMessage = " > " + ((CPacketChatMessage) event.getPacket()).getMessage();
            int newLine = 0;

            if (coloredMessage.length() > 255)
                newLine = coloredMessage.length() - 255;

            coloredMessage = coloredMessage.substring(0, coloredMessage.length() - newLine);
            ((CPacketChatMessage) event.getPacket()).message = coloredMessage;
        }
    }
}
