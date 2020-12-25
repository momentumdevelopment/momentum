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
        if ((event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock)) {
            if (((CPacketPlayerTryUseItemOnBlock) event.getPacket()).getPos().getY() < 255)
                return;

            if (((CPacketPlayerTryUseItemOnBlock) event.getPacket()).getDirection() != EnumFacing.UP)
                return;

            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(((CPacketPlayerTryUseItemOnBlock) event.getPacket()).getPos(), EnumFacing.DOWN, ((CPacketPlayerTryUseItemOnBlock) event.getPacket()).getHand(), ((CPacketPlayerTryUseItemOnBlock) event.getPacket()).getFacingX(), ((CPacketPlayerTryUseItemOnBlock) event.getPacket()).getFacingY(), ((CPacketPlayerTryUseItemOnBlock) event.getPacket()).getFacingZ()));
            event.setCanceled(true);
        }
    }
}
