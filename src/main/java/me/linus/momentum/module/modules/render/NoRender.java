package me.linus.momentum.module.modules.render;

import me.linus.momentum.event.events.packet.PacketReceiveEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author linustouchtips
 * @since 12/03/2020
 */

public class NoRender extends Module {
    public NoRender() {
        super("NoRender", Category.RENDER, "Prevents certain things from rendering");
    }

    public static Checkbox hurtCamera = new Checkbox("Hurt Camera", true);
    public static Checkbox fire = new Checkbox("Fire", true);
    public static Checkbox water = new Checkbox("Water", true);
    public static Checkbox armor = new Checkbox("Armor", false);
    public static Checkbox bossBar = new Checkbox("Boss Bars", true);
    public static Checkbox blockOverlay = new Checkbox("Block Overlay", true);
    public static Checkbox noCluster = new Checkbox("Cluster", true);
    public static Checkbox particles = new Checkbox("Particles", true);
    public static Checkbox fireworks = new Checkbox("Fireworks", true);
    public static Checkbox offhand = new Checkbox("Offhand", true);

    @Override
    public void setup() {
        addSetting(hurtCamera);
        addSetting(fire);
        addSetting(armor);
        addSetting(bossBar);
        addSetting(blockOverlay);
        addSetting(noCluster);
        addSetting(particles);
        addSetting(fireworks);
        addSetting(offhand);
    }

    @SubscribeEvent
    public void onRenderBlockOverlay(RenderBlockOverlayEvent event) {
        if (fire.getValue() && event.getOverlayType().equals(RenderBlockOverlayEvent.OverlayType.FIRE))
            event.setCanceled(true);

        if (water.getValue() && event.getOverlayType().equals(RenderBlockOverlayEvent.OverlayType.WATER))
            event.setCanceled(true);

        if (blockOverlay.getValue() && event.getOverlayType().equals(RenderBlockOverlayEvent.OverlayType.BLOCK))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void onPacketRecieve(PacketReceiveEvent event) {
        if ((event.getPacket() instanceof SPacketParticles || event.getPacket() instanceof SPacketEffect) && particles.getValue())
            event.setCanceled(true);

        if (event.getPacket() instanceof SPacketSoundEffect && offhand.getValue() && ((SPacketSoundEffect) event.getPacket()).getSound() == SoundEvents.ITEM_ARMOR_EQUIP_GENERIC)
            event.setCanceled(true);
    }
}