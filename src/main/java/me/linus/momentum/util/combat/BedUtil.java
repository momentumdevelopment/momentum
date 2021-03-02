package me.linus.momentum.util.combat;

import me.linus.momentum.mixin.MixinInterface;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/**
 * @author linustouchtips & bon
 * @since 12/20/2020
 */

public class BedUtil implements MixinInterface {

    /**
     * break
     */

    public static void attackBed(BlockPos pos) {
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.UP, EnumHand.OFF_HAND, 0, 0, 0));
    }

    /**
     * place
     */

    public static BlockPos getBedPosition(EntityPlayer currentTarget) {
        BlockPos placeTarget = null;

        if (currentTarget != null) {
            placeTarget = new BlockPos(currentTarget.getPositionVector().add(1, 1, 0));
            boolean nowTop = false;

            BlockPos block1 = placeTarget;
            if (!canPlaceBed(block1)) {
                placeTarget = new BlockPos(currentTarget.getPositionVector().add(-1, 1, 0));
                nowTop = false;
            }

            BlockPos block2 = placeTarget;
            if (!canPlaceBed(block2)) {
                placeTarget = new BlockPos(currentTarget.getPositionVector().add(0, 1, 1));
                nowTop = false;
            }

            BlockPos block3 = placeTarget;
            if (!canPlaceBed(block3)) {
                placeTarget = new BlockPos(currentTarget.getPositionVector().add(0, 1, -1));
                nowTop = false;
            }

            BlockPos block4 = placeTarget;
            if (!canPlaceBed(block4)) {
                placeTarget = new BlockPos(currentTarget.getPositionVector().add(0, 2, -1));
                nowTop = true;
            }

            BlockPos blockt1 = placeTarget;
            if (nowTop && !canPlaceBed(blockt1)) {
                placeTarget = new BlockPos(currentTarget.getPositionVector().add(-1, 2, 0));
            }

            BlockPos blockt2 = placeTarget;
            if (nowTop && !canPlaceBed(blockt2)) {
                placeTarget = new BlockPos(currentTarget.getPositionVector().add(0, 2, 1));
            }

            BlockPos blockt3 = placeTarget;
            if (nowTop && !canPlaceBed(blockt3)) {
                placeTarget = new BlockPos(currentTarget.getPositionVector().add(1, 2, 0));
            }
        }

        return placeTarget;
    }

    public static void placeBed(BlockPos pos, EnumFacing side, float rotVar, boolean rotate) {
        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));

        if (rotate)
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rotVar, 0, mc.player.onGround));

        BlockPos neighbour = pos.offset(side);
        EnumFacing opposite = side.getOpposite();
        Vec3d hitVec = new Vec3d(neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));

        mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);

        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
    }

    public static boolean canPlaceBed(BlockPos pos) {
        return (mc.world.getBlockState(pos).getBlock() == Blocks.AIR || mc.world.getBlockState(pos).getBlock() == Blocks.BED) && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos)).isEmpty();
    }
}