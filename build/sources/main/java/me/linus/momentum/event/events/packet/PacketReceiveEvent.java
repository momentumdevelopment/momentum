package me.linus.momentum.event.events.packet;

import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

/**
 * @author bon
 * @since 11/21/20
 */

@Cancelable
public class PacketReceiveEvent extends PacketEvent {

	public PacketReceiveEvent(final Packet<?> packet, final Stage stage) {
		super(packet, stage);
	}
	
}
