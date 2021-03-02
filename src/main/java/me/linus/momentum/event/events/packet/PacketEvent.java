package me.linus.momentum.event.events.packet;

import me.linus.momentum.event.MomentumEvent;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

/**
 * @author bon
 * @since 11/21/20
 */

@Cancelable
public class PacketEvent extends MomentumEvent {

	Packet<?> packet;

	public PacketEvent(Packet<?> packet, Stage stage) {
		super(stage);
		this.packet = packet;
	}
	
	public Packet<?> getPacket(){
		return this.packet;
	}

}
