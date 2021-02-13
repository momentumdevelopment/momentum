package me.linus.momentum.gui.hud.components;

import me.linus.momentum.event.events.packet.PacketReceiveEvent;
import me.linus.momentum.event.events.packet.PacketSendEvent;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.gui.theme.ThemeColor;
import me.linus.momentum.module.modules.client.HUDEditor;
import me.linus.momentum.util.render.FontUtil;
import me.linus.momentum.util.world.Timer;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CrystalsPerSecond extends HUDComponent {
    public CrystalsPerSecond() {
        super("CrystalsPerSecond", 2, 90);
        MinecraftForge.EVENT_BUS.register(this);
    }

    Timer resetTimer = new Timer();
    String attackCrystal = null;
    int brokenCrystals = 0;
    List<Integer> crystals = new ArrayList<>();
    int finalCrystals = 0;

    @Override
    public void renderComponent() {
        FontUtil.drawString("" + brokenCrystals, this.x, this.y, HUDEditor.colorSync.getValue() ? ThemeColor.BRIGHT : -1);
        width = (int) (FontUtil.getStringWidth("" + brokenCrystals) + 2);
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (mc.world != null && resetTimer.passed(20, Timer.Format.Ticks)) {
            crystals.add(brokenCrystals);
            brokenCrystals = 0;
        }

        finalCrystals = brokenCrystals / (crystals.size() + 1);
    }

    @SubscribeEvent
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacket() instanceof CPacketUseEntity && ((CPacketUseEntity) event.getPacket()).getAction().equals(CPacketUseEntity.Action.ATTACK) && ((CPacketUseEntity) event.getPacket()).getEntityFromWorld(mc.world) instanceof EntityEnderCrystal)
            attackCrystal = "" + ((CPacketUseEntity) event.getPacket()).entityId;
    }

    @SubscribeEvent
    public void onPacketRecieve(PacketReceiveEvent event) {
        if (event.getPacket() instanceof SPacketDestroyEntities && Arrays.toString(((SPacketDestroyEntities) event.getPacket()).getEntityIDs()).contains(attackCrystal))
            brokenCrystals++;
    }
}
