package me.linus.momentum.module.modules.player;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.util.player.InventoryUtil;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

/**
 * @author linustouchtips
 * @since 12/03/2020
 */

public class NoFall extends Module {
    public NoFall() {
        super("NoFall", Category.PLAYER, "Prevents fall damage");
    }

    public static Mode mode = new Mode("Mode", "NCP", "AAC", "Bucket");

    @Override
    public void setup() {
        addSetting(mode);
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (mc.player.fallDistance > 4.0f) {
            switch (mode.getValue()) {
                case 0:
                    mc.player.fallDistance = 0;
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX +420420, mc.player.posY, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1, mc.player.posZ, true));
                    break;
                case 1:
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1, mc.player.posZ, true));
                    break;
                case 2:
                    InventoryUtil.switchToSlot(Items.WATER_BUCKET);

                    BlockPos bucketPos = null;
                    for (int i = 0; i < mc.player.posY - 5; i++) {
                        BlockPos belowPos = new BlockPos(mc.player.posX, i, mc.player.posZ);

                        if (mc.world.getBlockState(belowPos).getBlock() instanceof BlockLiquid)
                            continue;

                        bucketPos = belowPos;
                    }

                    mc.player.rotationPitch = 90.0f;
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(bucketPos, EnumFacing.UP, EnumHand.MAIN_HAND, 0, 0, 0));
            }
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}
