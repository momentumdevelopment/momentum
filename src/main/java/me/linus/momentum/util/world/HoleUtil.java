package me.linus.momentum.util.world;

import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.modules.render.HoleESP;
import me.linus.momentum.util.player.PlayerUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author linustouchtips
 * @since 12/29/2020
 */

public class HoleUtil implements MixinInterface {
    public static boolean isInHole(EntityPlayer entityPlayer) {
        BlockPos blockPos = new BlockPos(Math.floor(entityPlayer.posX), Math.floor(entityPlayer.posY), Math.floor(entityPlayer.posZ));

        if (mc.world.getBlockState(blockPos).getBlock() != Blocks.AIR)
            return false;

        if (mc.world.getBlockState(blockPos.up()).getBlock() != Blocks.AIR)
            return false;

        if (mc.world.getBlockState(blockPos.down()).getBlock() == Blocks.AIR)
            return false;

        final BlockPos[] touchingBlocks = new BlockPos[]{
                blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west()
        };

        int validHorizontalBlocks = 0;
        for (BlockPos touching : touchingBlocks) {
            final IBlockState touchingState = mc.world.getBlockState(touching);
            if ((touchingState.getBlock() != Blocks.AIR) && touchingState.isFullBlock())
                validHorizontalBlocks++;
        }

        return validHorizontalBlocks >= 4;
    }

    public static boolean isVoidHole(BlockPos blockPos) {
        if (blockPos.getY() != 0)
            return false;

        return mc.world.getBlockState(blockPos).getBlock() == Blocks.AIR;
    }

    public static boolean IsObbyHole(BlockPos blockPos) {
        BlockPos boost = blockPos.add(0, 1, 0);
        BlockPos boost2 = blockPos.add(0, 0, 0);
        BlockPos boost3 = blockPos.add(0, 0, -1);
        BlockPos boost4 = blockPos.add(1, 0, 0);
        BlockPos boost5 = blockPos.add(-1, 0, 0);
        BlockPos boost6 = blockPos.add(0, 0, 1);
        BlockPos boost7 = blockPos.add(0, 2, 0);
        BlockPos boost8 = blockPos.add(0.5, 0.5, 0.5);
        BlockPos boost9 = blockPos.add(0, -1, 0);
        return !(HoleESP.mc.world.getBlockState(boost).getBlock() != Blocks.AIR || IsBRockHole(blockPos) || HoleESP.mc.world.getBlockState(boost2).getBlock() != Blocks.AIR || HoleESP.mc.world.getBlockState(boost7).getBlock() != Blocks.AIR || HoleESP.mc.world.getBlockState(boost3).getBlock() != Blocks.OBSIDIAN && HoleESP.mc.world.getBlockState(boost3).getBlock() != Blocks.BEDROCK || HoleESP.mc.world.getBlockState(boost4).getBlock() != Blocks.OBSIDIAN && HoleESP.mc.world.getBlockState(boost4).getBlock() != Blocks.BEDROCK || HoleESP.mc.world.getBlockState(boost5).getBlock() != Blocks.OBSIDIAN && HoleESP.mc.world.getBlockState(boost5).getBlock() != Blocks.BEDROCK || HoleESP.mc.world.getBlockState(boost6).getBlock() != Blocks.OBSIDIAN && HoleESP.mc.world.getBlockState(boost6).getBlock() != Blocks.BEDROCK || HoleESP.mc.world.getBlockState(boost8).getBlock() != Blocks.AIR || HoleESP.mc.world.getBlockState(boost9).getBlock() != Blocks.OBSIDIAN && HoleESP.mc.world.getBlockState(boost9).getBlock() != Blocks.BEDROCK);
    }

    public static boolean IsBRockHole(BlockPos blockPos) {
        BlockPos boost = blockPos.add(0, 1, 0);
        BlockPos boost2 = blockPos.add(0, 0, 0);
        BlockPos boost3 = blockPos.add(0, 0, -1);
        BlockPos boost4 = blockPos.add(1, 0, 0);
        BlockPos boost5 = blockPos.add(-1, 0, 0);
        BlockPos boost6 = blockPos.add(0, 0, 1);
        BlockPos boost7 = blockPos.add(0, 2, 0);
        BlockPos boost8 = blockPos.add(0.5, 0.5, 0.5);
        BlockPos boost9 = blockPos.add(0, -1, 0);
        return HoleESP.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && HoleESP.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && HoleESP.mc.world.getBlockState(boost7).getBlock() == Blocks.AIR && HoleESP.mc.world.getBlockState(boost3).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(boost4).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(boost5).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(boost6).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(boost8).getBlock() == Blocks.AIR && HoleESP.mc.world.getBlockState(boost9).getBlock() == Blocks.BEDROCK;
    }

    public static boolean isHole(BlockPos blockPos) {
        BlockPos b1 = blockPos.add(0, 1, 0);
        BlockPos b2 = blockPos.add(0, 0, 0);
        BlockPos b3 = blockPos.add(0, 0, -1);
        BlockPos b4 = blockPos.add(1, 0, 0);
        BlockPos b5 = blockPos.add(-1, 0, 0);
        BlockPos b6 = blockPos.add(0, 0, 1);
        BlockPos b7 = blockPos.add(0, 2, 0);
        BlockPos b8 = blockPos.add(0.5, 0.5, 0.5);
        BlockPos b9 = blockPos.add(0, -1, 0);
        return mc.world.getBlockState(b1).getBlock() == Blocks.AIR && (mc.world.getBlockState(b2).getBlock() == Blocks.AIR) && (mc.world.getBlockState(b7).getBlock() == Blocks.AIR) && ((mc.world.getBlockState(b3).getBlock() == Blocks.OBSIDIAN) || (mc.world.getBlockState(b3).getBlock() == Blocks.BEDROCK)) && ((mc.world.getBlockState(b4).getBlock() == Blocks.OBSIDIAN) || (mc.world.getBlockState(b4).getBlock() == Blocks.BEDROCK)) && ((mc.world.getBlockState(b5).getBlock() == Blocks.OBSIDIAN) || (mc.world.getBlockState(b5).getBlock() == Blocks.BEDROCK)) && ((mc.world.getBlockState(b6).getBlock() == Blocks.OBSIDIAN) || (mc.world.getBlockState(b6).getBlock() == Blocks.BEDROCK)) && (mc.world.getBlockState(b8).getBlock() == Blocks.AIR) && ((mc.world.getBlockState(b9).getBlock() == Blocks.OBSIDIAN) || (mc.world.getBlockState(b9).getBlock() == Blocks.BEDROCK));
    }
    public static List<BlockPos> getHoles(double range) {
        NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll(BlockUtil.getSphere(new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ)), (int) range, (int) range, false, true, 0).stream().filter(HoleUtil::isHole).collect(Collectors.toList()));
        return positions;
    }
}
