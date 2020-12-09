package me.linus.momentum.module.modules.misc;

import me.linus.momentum.event.events.packet.PacketSendEvent;
import me.linus.momentum.module.Module;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author linustouchtips
 * @since 11/28/2020
 */

public class BuildHeight extends Module {
    public BuildHeight() {
        super("BuildHeight", Category.MISC, "Allows you to interact with objects at build height");
    }

    @SubscribeEvent
    public void onPacketSend(PacketSendEvent event) {
        if (!(event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock))
            return;

        final CPacketPlayerTryUseItemOnBlock oldPacket = (CPacketPlayerTryUseItemOnBlock) event.getPacket();
        if (oldPacket.getPos().getY() < 255)
            return;

        if (oldPacket.getDirection() != EnumFacing.UP)
            return;

        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(oldPacket.getPos(), EnumFacing.DOWN, oldPacket.getHand(), oldPacket.getFacingX(), oldPacket.getFacingY(), oldPacket.getFacingZ()));
        event.setCanceled(true);
    }
}
