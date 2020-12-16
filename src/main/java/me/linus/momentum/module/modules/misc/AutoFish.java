package me.linus.momentum.module.modules.misc;

import me.linus.momentum.event.events.packet.PacketReceiveEvent;
import me.linus.momentum.module.Module;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author 086
 * @since 11/28/2020
 */

public class AutoFish extends Module {
    public AutoFish() {
        super("AutoFish", Category.MISC, "Automatically fishes for you");
    }

    @SubscribeEvent
    public void onPacketRecieve(PacketReceiveEvent event) {
        if (mc.player != null && (mc.player.getHeldItemMainhand().getItem() == Items.FISHING_ROD || mc.player.getHeldItemOffhand().getItem() == Items.FISHING_ROD) && event.getPacket() instanceof SPacketSoundEffect && SoundEvents.ENTITY_BOBBER_SPLASH.equals(((SPacketSoundEffect) event.getPacket()).getSound())) {
            new Thread(() -> {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mc.rightClickMouse();

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mc.rightClickMouse();

            }).start();
        }
    }
}
