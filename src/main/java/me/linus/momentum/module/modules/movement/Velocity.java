package me.linus.momentum.module.modules.movement;

import me.linus.momentum.event.events.packet.PacketReceiveEvent;
import me.linus.momentum.module.Module;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author bon
 * @since 11/21/20
 */

public class Velocity extends Module {
	public Velocity() {
		super("Velocity", Category.MOVEMENT, "Modifies player velocity");
	}
	
	@SubscribeEvent
	public void onPacketReceive(PacketReceiveEvent event) {
		if (event.getPacket() instanceof SPacketEntityVelocity) {
			event.setCanceled(true);
		}
		
		if (event.getPacket() instanceof SPacketExplosion) {
			event.setCanceled(true);
		}
	}

	@Override
	public String getHUDData() {
		return "H:0% V:0%";
	}
}
