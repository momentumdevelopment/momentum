package me.linus.momentum.module.modules.misc;

import me.linus.momentum.event.events.packet.PacketSendEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Portal extends Module {
    public Portal() {
        super("Portal", Category.MISC, "Modify portal behavior");
    }

    public static Checkbox portalGui = new Checkbox("GUI's", true);
    public static Checkbox godMode = new Checkbox("GodMode", false);

    @Override
    public void setup() {
        addSetting(portalGui);
        addSetting(godMode);
    }

    @SubscribeEvent
    public void onPacketSend(PacketSendEvent event) {
        if (godMode.getValue() && event.getPacket() instanceof CPacketConfirmTeleport) {
            mc.player.timeInPortal = 0;
            event.setCanceled(true);
        }
    }
}
