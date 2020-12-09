package me.linus.momentum.mixin.mixins;

import me.linus.momentum.module.ModuleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.linus.momentum.event.MomentumEvent.Stage;
import me.linus.momentum.event.events.packet.PacketReceiveEvent;
import me.linus.momentum.event.events.packet.PacketSendEvent;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;

/**
 * @author bon
 * @since 11/21/20
 */

@Mixin(value = NetworkManager.class/*, priority = 1006*/)
public class MixinNetworkManager {

	@Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
	public void onPacketSend(Packet<?> packet, CallbackInfo ci) {
		final PacketSendEvent event = new PacketSendEvent(packet, Stage.PRE);
		MinecraftForge.EVENT_BUS.post(event);
		if(event.isCanceled()) {
			ci.cancel();
		}
	}
	
	@Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
	public void onPacketReceive(ChannelHandlerContext chc, Packet<?> packet, CallbackInfo ci) {
		final PacketReceiveEvent event = new PacketReceiveEvent(packet, Stage.PRE);
		MinecraftForge.EVENT_BUS.post(event);
		if(event.isCanceled()) {
			ci.cancel();
		}
	}

	@Inject(method = "exceptionCaught", at = @At("HEAD"), cancellable = true)
	private void exceptionCaught(ChannelHandlerContext exceptionCaught1, Throwable exceptionCaught2, CallbackInfo info) {
		if (exceptionCaught2 instanceof IOException && ModuleManager.getModuleByName("AntiPacketKick").isEnabled())
			info.cancel();
	}
	
}
