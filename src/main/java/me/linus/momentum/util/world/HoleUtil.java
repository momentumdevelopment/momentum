package me.linus.momentum.util.world;

import me.linus.momentum.mixin.MixinInterface;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

/**
 * @author linustouchtips
 * @since 12/29/2020
 */

public class HoleUtil implements MixinInterface {

    public static boolean isInHole(EntityPlayer entityPlayer) {
        BlockPos blockPos = new BlockPos(Math.floor(entityPlayer.posX), Math.floor(entityPlayer.posY), Math.floor(entityPlayer.posZ));
        if (BlockUtil.getBlockResistance(blockPos.down()) != BlockUtil.blockResistance.Blank || BlockUtil.getBlockResistance(blockPos.up()) != BlockUtil.blockResistance.Blank || BlockUtil.getBlockResistance(blockPos) != BlockUtil.blockResistance.Blank)
            return false;

        BlockPos[] touchingBlocks = new BlockPos[]{
                blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west()
        };

        int validHorizontalBlocks = 0;
        for (BlockPos touching : touchingBlocks) {
            if ((BlockUtil.getBlockResistance(touching) != BlockUtil.blockResistance.Blank) && mc.world.getBlockState(touching).isFullBlock())
                validHorizontalBlocks++;
        }

        return validHorizontalBlocks >= 4;
    }

    public static boolean isVoidHole(BlockPos blockPos) {
        return mc.player.dimension == -1 ? (blockPos.getY() == 0 || blockPos.getY() == 127) && BlockUtil.getBlockResistance(blockPos) == BlockUtil.blockResistance.Blank : blockPos.getY() == 0 && BlockUtil.getBlockResistance(blockPos) == BlockUtil.blockResistance.Blank;
    }

    public static boolean isObsidianHole(BlockPos blockPos) {
        return !(mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock() != Blocks.AIR || isBedRockHole(blockPos) || mc.world.getBlockState(blockPos.add(0, 0, 0)).getBlock() != Blocks.AIR || mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock() != Blocks.AIR || mc.world.getBlockState(blockPos.add(0, 0, -1)).getBlock() != Blocks.OBSIDIAN && mc.world.getBlockState(blockPos.add(0, 0, -1)).getBlock() != Blocks.BEDROCK || mc.world.getBlockState(blockPos.add(1, 0, 0)).getBlock() != Blocks.OBSIDIAN && mc.world.getBlockState(blockPos.add(1, 0, 0)).getBlock() != Blocks.BEDROCK || mc.world.getBlockState(blockPos.add(-1, 0, 0)).getBlock() != Blocks.OBSIDIAN && mc.world.getBlockState(blockPos.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK || mc.world.getBlockState(blockPos.add(0, 0, 1)).getBlock() != Blocks.OBSIDIAN && mc.world.getBlockState(blockPos.add(0, 0, 1)).getBlock() != Blocks.BEDROCK || mc.world.getBlockState(blockPos.add(0.5, 0.5, 0.5)).getBlock() != Blocks.AIR || mc.world.getBlockState(blockPos.add(0, -1, 0)).getBlock() != Blocks.OBSIDIAN && mc.world.getBlockState(blockPos.add(0, -1, 0)).getBlock() != Blocks.BEDROCK);
    }

    public static boolean isBedRockHole(BlockPos blockPos) {
        return BlockUtil.getBlockResistance(blockPos.add(0, 1, 0)) == BlockUtil.blockResistance.Blank && BlockUtil.getBlockResistance(blockPos.add(0, 0, 0)) == BlockUtil.blockResistance.Blank && BlockUtil.getBlockResistance(blockPos.add(0, 2, 0)) == BlockUtil.blockResistance.Blank && BlockUtil.getBlockResistance(blockPos.add(0, 0, -1)) == BlockUtil.blockResistance.Unbreakable && BlockUtil.getBlockResistance(blockPos.add(1, 0, 0)) == BlockUtil.blockResistance.Unbreakable && BlockUtil.getBlockResistance(blockPos.add(-1, 0, 0)) == BlockUtil.blockResistance.Unbreakable && BlockUtil.getBlockResistance(blockPos.add(0, 0, 1)) == BlockUtil.blockResistance.Unbreakable && BlockUtil.getBlockResistance(blockPos.add(0.5, 0.5, 0.5)) == BlockUtil.blockResistance.Blank && BlockUtil.getBlockResistance(blockPos.add(0, -1, 0)) == BlockUtil.blockResistance.Unbreakable;
    }

    public static boolean isHole(BlockPos blockPos) {
        return BlockUtil.getBlockResistance(blockPos.add(0, 1, 0)) == BlockUtil.blockResistance.Blank && BlockUtil.getBlockResistance(blockPos.add(0, 0, 0)) == BlockUtil.blockResistance.Blank && BlockUtil.getBlockResistance(blockPos.add(0, 2, 0)) == BlockUtil.blockResistance.Blank && (BlockUtil.getBlockResistance(blockPos.add(0, 0, -1)) == BlockUtil.blockResistance.Resistant || BlockUtil.getBlockResistance(blockPos.add(0, 0, -1)) == BlockUtil.blockResistance.Unbreakable) && ((BlockUtil.getBlockResistance(blockPos.add(1, 0, 0)) == BlockUtil.blockResistance.Resistant || (BlockUtil.getBlockResistance(blockPos.add(1, 0, 0)) == BlockUtil.blockResistance.Unbreakable)) && ((BlockUtil.getBlockResistance(blockPos.add(-1, 0, 0)) == BlockUtil.blockResistance.Resistant) || (BlockUtil.getBlockResistance(blockPos.add(-1, 0, 0)) == BlockUtil.blockResistance.Unbreakable)) && ((BlockUtil.getBlockResistance(blockPos.add(0, 0, 1)) == BlockUtil.blockResistance.Resistant) || (BlockUtil.getBlockResistance(blockPos.add(0, 0, 1)) == BlockUtil.blockResistance.Unbreakable)) && (BlockUtil.getBlockResistance(blockPos.add(0.5, 0.5, 0.5)) == BlockUtil.blockResistance.Blank) && ((BlockUtil.getBlockResistance(blockPos.add(0, -1, 0)) == BlockUtil.blockResistance.Resistant) || (BlockUtil.getBlockResistance(blockPos.add(0, -1, 0)) == BlockUtil.blockResistance.Unbreakable)));
    }
}