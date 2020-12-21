package me.linus.momentum.module.modules.combat;

import me.linus.momentum.module.Module;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.math.BlockPos;

/**
 * @author linustouchtips
 * @since 11/27/2020
 */

public class QuickBow extends Module {
    public QuickBow() {
        super("QuickBow", Category.COMBAT, "Releases bow at a very high speed");
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (mc.player.getHeldItemMainhand().getItem() instanceof ItemBow && mc.player.isHandActive() && mc.player.getItemInUseMaxCount() >= 3) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(mc.player.getActiveHand()));
            mc.player.stopActiveHand();
        }
    }
}
