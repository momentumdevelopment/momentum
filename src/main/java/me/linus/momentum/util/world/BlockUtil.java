package me.linus.momentum.util.world;

import me.linus.momentum.managers.RotationManager;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.player.InventoryUtil;
import me.linus.momentum.util.player.rotation.Rotation;
import me.linus.momentum.util.player.rotation.Rotation.RotationMode;
import me.linus.momentum.util.player.rotation.RotationPriority;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
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

    public static void placeBlock(BlockPos pos, boolean rotate, boolean strict, boolean raytrace, boolean packet, boolean swingArm, boolean antiGlitch) {
        for (EnumFacing enumFacing : EnumFacing.values()) {
            if (!(getBlockResistance(pos.offset(enumFacing)) == BlockResistance.Blank) && !EntityUtil.isIntercepted(pos)) {
                Vec3d vec = new Vec3d(pos.getX() + 0.5D + (double) enumFacing.getXOffset() * 0.5D, pos.getY() + 0.5D + (double) enumFacing.getYOffset() * 0.5D, pos.getZ() + 0.5D + (double) enumFacing.getZOffset() * 0.5D);

                float[] old = new float[] {
                        mc.player.rotationYaw, mc.player.rotationPitch
                };

                if (strict)
                    RotationManager.rotationQueue.add(new Rotation((float) Math.toDegrees(Math.atan2((vec.z - mc.player.posZ), (vec.x - mc.player.posX))) - 90.0F, (float) (-Math.toDegrees(Math.atan2((vec.y - (mc.player.posY + (double) mc.player.getEyeHeight())), (Math.sqrt((vec.x - mc.player.posX) * (vec.x - mc.player.posX) + (vec.z - mc.player.posZ) * (vec.z - mc.player.posZ)))))), RotationMode.Packet, RotationPriority.High));

                if (rotate)
                    mc.player.connection.sendPacket(new CPacketPlayer.Rotation((float) Math.toDegrees(Math.atan2((vec.z - mc.player.posZ), (vec.x - mc.player.posX))) - 90.0F, (float) (-Math.toDegrees(Math.atan2((vec.y - (mc.player.posY + (double) mc.player.getEyeHeight())), (Math.sqrt((vec.x - mc.player.posX) * (vec.x - mc.player.posX) + (vec.z - mc.player.posZ) * (vec.z - mc.player.posZ)))))), mc.player.onGround));

                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));

                if (packet)
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, raytrace ? enumFacing : EnumFacing.UP, EnumHand.MAIN_HAND, 0, 0, 0));
                else
                    mc.playerController.processRightClickBlock(mc.player, mc.world, pos.offset(enumFacing), raytrace ? enumFacing.getOpposite() : EnumFacing.UP, new Vec3d(pos), EnumHand.MAIN_HAND);

                if (swingArm)
                    mc.player.swingArm(EnumHand.MAIN_HAND);

                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));

                if (rotate)
                    mc.player.connection.sendPacket(new CPacketPlayer.Rotation(old[0], old[1], mc.player.onGround));

                if (antiGlitch)
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos.offset(enumFacing), enumFacing.getOpposite()));

                return;
            }
        }
    }

    public static List<BlockPos> getNearbyBlocks(EntityPlayer player, double blockRange, boolean motion) {
        List<BlockPos> nearbyBlocks = new ArrayList<>();
        int range = (int) MathUtil.roundDouble(blockRange, 0);

        if (motion)
            player.getPosition().add(new Vec3i(player.motionX, player.motionY, player.motionZ));

        for (int x = -range; x <= range; x++)
            for (int y = -range; y <= range - (range / 2); y++)
                for (int z = -range; z <= range; z++)
                    nearbyBlocks.add(player.getPosition().add(x, y, z));

        return nearbyBlocks;
    }

    @SuppressWarnings("deprecation")
    public static BlockResistance getBlockResistance(BlockPos block) {
        if (mc.world.isAirBlock(block))
            return BlockResistance.Blank;

        else if (mc.world.getBlockState(block).getBlock().getBlockHardness(mc.world.getBlockState(block), mc.world, block) != -1 && !(mc.world.getBlockState(block).getBlock().equals(Blocks.OBSIDIAN) || mc.world.getBlockState(block).getBlock().equals(Blocks.ANVIL) || mc.world.getBlockState(block).getBlock().equals(Blocks.ENCHANTING_TABLE) || mc.world.getBlockState(block).getBlock().equals(Blocks.ENDER_CHEST)))
            return BlockResistance.Breakable;

        else if (mc.world.getBlockState(block).getBlock().equals(Blocks.OBSIDIAN) || mc.world.getBlockState(block).getBlock().equals(Blocks.ANVIL) || mc.world.getBlockState(block).getBlock().equals(Blocks.ENCHANTING_TABLE) || mc.world.getBlockState(block).getBlock().equals(Blocks.ENDER_CHEST))
            return BlockResistance.Resistant;

        else if (mc.world.getBlockState(block).getBlock().equals(Blocks.BEDROCK))
            return BlockResistance.Unbreakable;

        return null;
    }

    public enum BlockResistance {
        Blank,
        Breakable,
        Resistant,
        Unbreakable
    }
}
