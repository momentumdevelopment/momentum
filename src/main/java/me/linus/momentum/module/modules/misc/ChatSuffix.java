package me.linus.momentum.module.modules.misc;

import me.linus.momentum.Momentum;
import me.linus.momentum.event.events.packet.PacketSendEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.util.client.MessageUtil;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author bon & linustouchtips
 * @since 11/21/20
 */

public class ChatSuffix extends Module {
	public ChatSuffix() {
		super("ChatSuffix", Category.MISC, "Appends a chat suffix to messages");
	}

	@SubscribeEvent
	public void onPacketSend(PacketSendEvent event) {
		if (event.getPacket() instanceof CPacketChatMessage) {
			if (((CPacketChatMessage) event.getPacket()).getMessage().startsWith("/") || ((CPacketChatMessage) event.getPacket()).getMessage().startsWith("!") || ((CPacketChatMessage) event.getPacket()).getMessage().startsWith("$") || ((CPacketChatMessage) event.getPacket()).getMessage().startsWith("?") || ((CPacketChatMessage) event.getPacket()).getMessage().startsWith(".") || ((CPacketChatMessage) event.getPacket()).getMessage().startsWith(","))
				return;

			String newMessage = ((CPacketChatMessage) event.getPacket()).getMessage() + " \u23d0 " + MessageUtil.toUnicode(Momentum.NAME);
			((CPacketChatMessage) event.getPacket()).message = newMessage;
		}
	}
}
