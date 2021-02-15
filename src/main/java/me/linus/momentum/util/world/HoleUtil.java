package me.linus.momentum.util.world;

import me.linus.momentum.mixin.MixinInterface;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import me.linus.momentum.util.world.BlockUtil.BlockResistance;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author linustouchtips
 * @since 12/29/2020
 */

public class HoleUtil implements MixinInterface {

    public static boolean isInHole(EntityPlayer entityPlayer) {
        BlockPos blockPos = new BlockPos(Math.floor(entityPlayer.posX), Math.floor(entityPlayer.posY), Math.floor(entityPlayer.posZ));
        if (BlockUtil.getBlockResistance(blockPos.down()) != BlockResistance.Blank || BlockUtil.getBlockResistance(blockPos.up()) != BlockResistance.Blank || BlockUtil.getBlockResistance(blockPos) != BlockResistance.Blank)
            return false;

        BlockPos[] touchingBlocks = new BlockPos[] {
                blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west()
        };

        int validHorizontalBlocks = 0;
        for (BlockPos touching : touchingBlocks) {
            if ((BlockUtil.getBlockResistance(touching) != BlockResistance.Blank) && mc.world.getBlockState(touching).isFullBlock())
                validHorizontalBlocks++;
        }

        return validHorizontalBlocks >= 4;
    }

    public static boolean isVoidHole(BlockPos blockPos) {
        return mc.player.dimension == -1 ? (blockPos.getY() == 0 || blockPos.getY() == 127) && BlockUtil.getBlockResistance(blockPos) == BlockResistance.Blank : blockPos.getY() == 0 && BlockUtil.getBlockResistance(blockPos) == BlockResistance.Blank;
    }

    public static boolean isObsidianHole(BlockPos blockPos) {
        return !(BlockUtil.getBlockResistance(blockPos.add(0, 1, 0)) != BlockResistance.Blank || isBedRockHole(blockPos) || BlockUtil.getBlockResistance(blockPos.add(0, 0, 0)) != BlockResistance.Blank || BlockUtil.getBlockResistance(blockPos.add(0, 2, 0)) != BlockResistance.Blank || BlockUtil.getBlockResistance(blockPos.add(0, 0, -1)) != BlockResistance.Resistant && BlockUtil.getBlockResistance(blockPos.add(0, 0, -1)) != BlockResistance.Unbreakable || BlockUtil.getBlockResistance(blockPos.add(1, 0, 0)) != BlockResistance.Resistant && BlockUtil.getBlockResistance(blockPos.add(1, 0, 0)) != BlockResistance.Unbreakable || BlockUtil.getBlockResistance(blockPos.add(-1, 0, 0)) != BlockResistance.Resistant && BlockUtil.getBlockResistance(blockPos.add(-1, 0, 0)) != BlockResistance.Unbreakable || BlockUtil.getBlockResistance(blockPos.add(0, 0, 1)) != BlockResistance.Resistant && BlockUtil.getBlockResistance(blockPos.add(0, 0, 1)) != BlockResistance.Unbreakable || BlockUtil.getBlockResistance(blockPos.add(0.5, 0.5, 0.5)) != BlockResistance.Blank || BlockUtil.getBlockResistance(blockPos.add(0, -1, 0)) != BlockResistance.Resistant && BlockUtil.getBlockResistance(blockPos.add(0, -1, 0)) != BlockResistance.Unbreakable);
    }

    public static boolean isBedRockHole(BlockPos blockPos) {
        return BlockUtil.getBlockResistance(blockPos.add(0, 1, 0)) == BlockResistance.Blank && BlockUtil.getBlockResistance(blockPos.add(0, 0, 0)) == BlockResistance.Blank && BlockUtil.getBlockResistance(blockPos.add(0, 2, 0)) == BlockResistance.Blank && BlockUtil.getBlockResistance(blockPos.add(0, 0, -1)) == BlockResistance.Unbreakable && BlockUtil.getBlockResistance(blockPos.add(1, 0, 0)) == BlockResistance.Unbreakable && BlockUtil.getBlockResistance(blockPos.add(-1, 0, 0)) == BlockResistance.Unbreakable && BlockUtil.getBlockResistance(blockPos.add(0, 0, 1)) == BlockResistance.Unbreakable && BlockUtil.getBlockResistance(blockPos.add(0.5, 0.5, 0.5)) == BlockResistance.Blank && BlockUtil.getBlockResistance(blockPos.add(0, -1, 0)) == BlockResistance.Unbreakable;
    }

    public static boolean isHole(BlockPos blockPos) {
        return BlockUtil.getBlockResistance(blockPos.add(0, 1, 0)) == BlockResistance.Blank && BlockUtil.getBlockResistance(blockPos.add(0, 0, 0)) == BlockResistance.Blank && BlockUtil.getBlockResistance(blockPos.add(0, 2, 0)) == BlockResistance.Blank && (BlockUtil.getBlockResistance(blockPos.add(0, 0, -1)) == BlockResistance.Resistant || BlockUtil.getBlockResistance(blockPos.add(0, 0, -1)) == BlockResistance.Unbreakable) && ((BlockUtil.getBlockResistance(blockPos.add(1, 0, 0)) == BlockResistance.Resistant || (BlockUtil.getBlockResistance(blockPos.add(1, 0, 0)) == BlockResistance.Unbreakable)) && ((BlockUtil.getBlockResistance(blockPos.add(-1, 0, 0)) == BlockResistance.Resistant) || (BlockUtil.getBlockResistance(blockPos.add(-1, 0, 0)) == BlockResistance.Unbreakable)) && ((BlockUtil.getBlockResistance(blockPos.add(0, 0, 1)) == BlockResistance.Resistant) || (BlockUtil.getBlockResistance(blockPos.add(0, 0, 1)) == BlockResistance.Unbreakable)) && (BlockUtil.getBlockResistance(blockPos.add(0.5, 0.5, 0.5)) == BlockResistance.Blank) && ((BlockUtil.getBlockResistance(blockPos.add(0, -1, 0)) == BlockResistance.Resistant) || (BlockUtil.getBlockResistance(blockPos.add(0, -1, 0)) == BlockResistance.Unbreakable)));
    }

    public static List<BlockPos> getNearbyHoles(double range) {
        return BlockUtil.getNearbyBlocks(mc.player, range, false).stream().filter(blockPos -> HoleUtil.isHole(blockPos)).collect(Collectors.toList());
    }
}