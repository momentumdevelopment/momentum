package me.linus.momentum.module.modules.misc;

import me.linus.momentum.event.events.packet.PacketSendEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.util.client.external.MessageUtil;
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

	String suffix = "momentum";

	@SubscribeEvent
	public void onPacketSend(PacketSendEvent event) {
		if (event.getPacket() instanceof CPacketChatMessage) {
			CPacketChatMessage packet = (CPacketChatMessage) event.getPacket();
			if (packet.getMessage().startsWith("/"))
				return;

			if (packet.getMessage().startsWith("!"))
				return;

			if (packet.getMessage().startsWith("$"))
				return;

			if (packet.getMessage().startsWith("?"))
				return;

			if (packet.getMessage().startsWith("."))
				return;

			if (packet.getMessage().startsWith(","))
				return;

			String newMessage = packet.getMessage() + " \u23d0 " + MessageUtil.toUnicode(suffix);
			packet.message = newMessage;
		}
	}
}
