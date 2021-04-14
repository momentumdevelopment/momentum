package me.linus.momentum.managers;

import me.linus.momentum.event.events.packet.PacketSendEvent;
import me.linus.momentum.mixin.MixinInterface;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * @author linustouchtips
 * @since 03/07/2021
 */

public class SwitchManager implements MixinInterface {
    public SwitchManager() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static int tick = 0;

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        tick++;
    }

    @SubscribeEvent
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacket() instanceof CPacketHeldItemChange) {
            tick = 0;
        }
    }

    public static boolean switchReady(int ticks) {
        return tick > ticks;
    }
}
