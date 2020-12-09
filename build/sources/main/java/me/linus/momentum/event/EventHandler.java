package me.linus.momentum.event;

import me.linus.momentum.event.events.packet.PacketReceiveEvent;
import me.linus.momentum.event.events.world.TotemPopEvent;
import me.linus.momentum.mixin.MixinInterface;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandler implements MixinInterface {

    @SubscribeEvent
    public void onTotemPop(PacketReceiveEvent event) {
        if (event.getPacket() instanceof SPacketEntityStatus) {
            SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();
            if (packet.getOpCode() == 35) {
                MinecraftForge.EVENT_BUS.post(new TotemPopEvent(packet.getEntity(mc.world)));
            }
        }
    }
}
