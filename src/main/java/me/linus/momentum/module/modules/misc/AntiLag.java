package me.linus.momentum.module.modules.misc;

import me.linus.momentum.event.events.packet.PacketReceiveEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class AntiLag extends Module {
    public AntiLag() {
        super("AntiLag", Category.MISC, "Prevents particles from lagging your game");
    }

    private static final Checkbox particles = new Checkbox("Particles", true);
    private static final Checkbox fireworks = new Checkbox("Fireworks", true);
    private static final Checkbox offhand = new Checkbox("Offhand", true);
    private static final Checkbox slime = new Checkbox("Slime", false);

    @Override
    public void setup() {
        addSetting(particles);
        addSetting(fireworks);
        addSetting(offhand);
        addSetting(slime);
    }

    @SubscribeEvent
    public void onPacketRecieve(PacketReceiveEvent event) {
        if ((event.getPacket() instanceof SPacketParticles || event.getPacket() instanceof SPacketEffect) && particles.getValue())
            event.setCanceled(true);

        if (event.getPacket() instanceof SPacketSoundEffect && offhand.getValue()) {
            if (((SPacketSoundEffect) event.getPacket()).getSound() == SoundEvents.ITEM_ARMOR_EQUIP_GENERIC)
                event.setCanceled(true);
        }
    }
}
