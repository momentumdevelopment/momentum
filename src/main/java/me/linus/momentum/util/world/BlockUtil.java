package me.linus.momentum.util.world;

import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.player.InventoryUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linustouchtips
 * @since 11/30/2020
 */

public class BlockUtil implements MixinInterface {

    public static void placeBlock(BlockPos pos, boolean rotate) {
        for (EnumFacing enumFacing : EnumFacing.values()) {
            if (!mc.world.getBlockState(pos.offset(enumFacing)).getBlock().equals(Blocks.AIR) && !EntityUtil.isIntercepted(pos)) {
                Vec3d vec = new Vec3d(pos.getX() + 0.5D + (double) enumFacing.getFrontOffsetX() * 0.5D, pos.getY() + 0.5D + (double) enumFacing.getFrontOffsetY() * 0.5D, pos.getZ() + 0.5D + (double) enumFacing.getFrontOffsetZ() * 0.5D);

                float[] old = new float[] {
                        mc.player.rotationYaw, mc.player.rotationPitch
                };

                if (rotate)
                    mc.player.connection.sendPacket(new CPacketPlayer.Rotation((float) Math.toDegrees(Math.atan2((vec.z - mc.player.posZ), (vec.x - mc.player.posX))) - 90.0F, (float) (-Math.toDegrees(Math.atan2((vec.y - (mc.player.posY + (double) mc.player.getEyeHeight())), (Math.sqrt((vec.x - mc.player.posX) * (vec.x - mc.player.posX) + (vec.z - mc.player.posZ) * (vec.z - mc.player.posZ)))))), mc.player.onGround));

                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                mc.playerController.processRightClickBlock(mc.player, mc.world, pos.offset(enumFacing), enumFacing.getOpposite(), new Vec3d(pos), EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));

                if (rotate)
                    mc.player.connection.sendPacket(new CPacketPlayer.Rotation(old[0], old[1], mc.player.onGround));

                return;
            }
        }
    }

    public static boolean isCollidedBlocks(BlockPos pos) {
        return mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock().equals(Blocks.OBSIDIAN) || isInterceptedBlockPos(pos) || InventoryUtil.getBlockInHotbar(Blocks.OBSIDIAN) == -1;
    }

    public static boolean isInterceptedBlockPos(BlockPos pos) {
        mc.world.loadedEntityList.stream().filter(entity -> entity != mc.player).forEach(entity -> {
            if (!new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox()))
                return;
        });

        return true;
    }

    public static boolean canBreak(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock().getBlockHardness(mc.world.getBlockState(pos), mc.world, pos) != -1;
    }

    public static List<BlockPos> getNearbyBlocks(EntityPlayer player, double blockRange, boolean motion) {
        List<BlockPos> nearbyBlocks = new ArrayList<>();
        int range = (int) MathUtil.roundDouble(blockRange, 0);

        if (motion)
            player.getPosition().add(new Vec3i(player.motionX, player.motionY, player.motionZ));

        for (int x = -range; x <= range; x++)
            for (int y = -range; y <= range; y++)
                for (int z = -range; z <= range; z++)
                    nearbyBlocks.add(player.getPosition().add(x, y, z));

        return nearbyBlocks;
    }
}
