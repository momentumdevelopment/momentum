package me.linus.momentum.module.modules.movement;

import me.linus.momentum.event.events.packet.PacketReceiveEvent;
import me.linus.momentum.event.events.player.PushEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.slider.Slider;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author bon & linustouchtips
 * @since 11/21/20
 */

public class Velocity extends Module {
	public Velocity() {
		super("Velocity", Category.MOVEMENT, "Modifies player velocity");
	}

	public static Slider horizontal = new Slider("Horizontal", 0.0D, 0.0D, 100.0D, 0);
	public static Slider vertical = new Slider("Speed", 0.0D, 0.0D, 100.0D, 0);
	private static final Checkbox noPush = new Checkbox("No Push", true);

	@Override
	public void setup() {
		addSetting(horizontal);
		addSetting(vertical);
		addSetting(noPush);
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

	@SubscribeEvent
	public void onPush(PushEvent event) {
		if (noPush.getValue())
			event.setCanceled(true);
	}

	@Override
	public String getHUDData() {
		return "H:0% V:0%";
	}
}
