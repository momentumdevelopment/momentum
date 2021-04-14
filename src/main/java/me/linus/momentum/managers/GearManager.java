package me.linus.momentum.managers;

import me.linus.momentum.event.events.packet.PacketReceiveEvent;
import me.linus.momentum.managers.social.friend.FriendManager;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.modules.combat.AutoCrystal;
import me.linus.momentum.module.modules.misc.Notifier;
import me.linus.momentum.util.client.MessageUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;

/**
 * @author linustouchtips
 * @since 02/21/2021
 */

public class GearManager implements MixinInterface {
    public GearManager() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static HashMap<EntityPlayer, Integer> expMap = new HashMap<>();
    public static HashMap<EntityPlayer, Integer> crystalMap = new HashMap<>();
    public static HashMap<String, Integer> totemMap = new HashMap<>();
    public static boolean lastTickPop = false;
    public static int ticks = 0;

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (lastTickPop)
            ticks++;

        if (ticks > 20)
            lastTickPop = false;
    }

    @SubscribeEvent
    public void onPacketRecieve(PacketReceiveEvent event) {
        if (event.getPacket() instanceof SPacketEntityStatus && ((SPacketEntityStatus) event.getPacket()).getOpCode() == 35) {
            if (((SPacketEntityStatus) event.getPacket()).getEntity(mc.world).equals(AutoCrystal.crystalTarget))
                lastTickPop = true;

            totemMap.put(((SPacketEntityStatus) event.getPacket()).getEntity(mc.world).getName(), totemMap.containsKey(((SPacketEntityStatus) event.getPacket()).getEntity(mc.world).getName()) ? totemMap.get(((SPacketEntityStatus) event.getPacket()).getEntity(mc.world).getName()) + 1 : 1);

            if (ModuleManager.getModuleByName("Notifier").isEnabled() && Notifier.totem.getValue()) {
                if (FriendManager.isFriend(((SPacketEntityStatus) event.getPacket()).getEntity(mc.world).getName()))
                    MessageUtil.sendClientMessage("Your friend, " + ((SPacketEntityStatus) event.getPacket()).getEntity(mc.world).getName() + ", popped " + TextFormatting.RED + totemMap.get(((SPacketEntityStatus) event.getPacket()).getEntity(mc.world).getName()) + TextFormatting.WHITE + " totems!");
                else
                    MessageUtil.sendClientMessage(((SPacketEntityStatus) event.getPacket()).getEntity(mc.world).getName() + " popped " + totemMap.get(((SPacketEntityStatus) event.getPacket()).getEntity(mc.world).getName()) + " totems!");
            }
        }
    }
}
