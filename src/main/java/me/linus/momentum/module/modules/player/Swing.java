package me.linus.momentum.module.modules.player;

import me.linus.momentum.event.events.packet.PacketSendEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.mode.Mode;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author linustouchtips
 * @since 11/26/2020
 */

public class Swing extends Module {
    public Swing() {
        super("Swing", Category.PLAYER, "Swings with your offhand");
    }

    public static Mode mode = new Mode("Mode", "Offhand", "Mainhand");
    public static Checkbox noAnimation = new Checkbox("Cancel Animation", true);
    public static Checkbox noReset = new Checkbox("No Reset", true);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(noAnimation);
        addSetting(noReset);
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        switch (mode.getValue()) {
            case 0:
                mc.player.swingingHand = EnumHand.OFF_HAND;
                break;
            case 1:
                mc.player.swingingHand = EnumHand.MAIN_HAND;
                break;
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacket() instanceof CPacketAnimation && noAnimation.getValue()) {
            event.setCanceled(true);
        }
    }
}
