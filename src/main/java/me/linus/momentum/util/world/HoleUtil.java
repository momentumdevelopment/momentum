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
        if (mc.world.getBlockState(blockPos.down()).getBlock() == Blocks.AIR || mc.world.getBlockState(blockPos.up()).getBlock() != Blocks.AIR || mc.world.getBlockState(blockPos).getBlock() != Blocks.AIR)
            return false;

        BlockPos[] touchingBlocks = new BlockPos[]{
                blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west()
        };

        int validHorizontalBlocks = 0;
        for (BlockPos touching : touchingBlocks) {
            IBlockState touchingState = mc.world.getBlockState(touching);
            if ((touchingState.getBlock() != Blocks.AIR) && touchingState.isFullBlock())
                validHorizontalBlocks++;
        }

        return validHorizontalBlocks >= 4;
    }

    public static boolean isVoidHole(BlockPos blockPos) {
        return mc.player.dimension == -1 ? (blockPos.getY() == 0 || blockPos.getY() == 126) && mc.world.getBlockState(blockPos).getBlock().equals(Blocks.AIR) : blockPos.getY() == 0 && mc.world.getBlockState(blockPos).getBlock().equals(Blocks.AIR);
    }

    public static boolean IsObsidianHole(BlockPos blockPos) {
        return !(HoleESP.mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock() != Blocks.AIR || IsBedRockHole(blockPos) || HoleESP.mc.world.getBlockState(blockPos.add(0, 0, 0)).getBlock() != Blocks.AIR || HoleESP.mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock() != Blocks.AIR || HoleESP.mc.world.getBlockState(blockPos.add(0, 0, -1)).getBlock() != Blocks.OBSIDIAN && HoleESP.mc.world.getBlockState(blockPos.add(0, 0, -1)).getBlock() != Blocks.BEDROCK || HoleESP.mc.world.getBlockState(blockPos.add(1, 0, 0)).getBlock() != Blocks.OBSIDIAN && HoleESP.mc.world.getBlockState(blockPos.add(1, 0, 0)).getBlock() != Blocks.BEDROCK || HoleESP.mc.world.getBlockState(blockPos.add(-1, 0, 0)).getBlock() != Blocks.OBSIDIAN && HoleESP.mc.world.getBlockState(blockPos.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK || HoleESP.mc.world.getBlockState(blockPos.add(0, 0, 1)).getBlock() != Blocks.OBSIDIAN && HoleESP.mc.world.getBlockState(blockPos.add(0, 0, 1)).getBlock() != Blocks.BEDROCK || HoleESP.mc.world.getBlockState(blockPos.add(0.5, 0.5, 0.5)).getBlock() != Blocks.AIR || HoleESP.mc.world.getBlockState(blockPos.add(0, -1, 0)).getBlock() != Blocks.OBSIDIAN && HoleESP.mc.world.getBlockState(blockPos.add(0, -1, 0)).getBlock() != Blocks.BEDROCK);
    }

    public static boolean IsBedRockHole(BlockPos blockPos) {
        return HoleESP.mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock() == Blocks.AIR && HoleESP.mc.world.getBlockState(blockPos.add(0, 0, 0)).getBlock() == Blocks.AIR && HoleESP.mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock() == Blocks.AIR && HoleESP.mc.world.getBlockState(blockPos.add(0, 0, -1)).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(blockPos.add(1, 0, 0)).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(blockPos.add(-1, 0, 0)).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(blockPos.add(0, 0, 1)).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(blockPos.add(0.5, 0.5, 0.5)).getBlock() == Blocks.AIR && HoleESP.mc.world.getBlockState(blockPos.add(0, -1, 0)).getBlock() == Blocks.BEDROCK;
    }

    public static boolean isHole(BlockPos blockPos) {
        return mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock() == Blocks.AIR && (mc.world.getBlockState(blockPos.add(0, 0, 0)).getBlock() == Blocks.AIR) && (mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock() == Blocks.AIR) && ((mc.world.getBlockState(blockPos.add(0, 0, -1)).getBlock() == Blocks.OBSIDIAN) || (mc.world.getBlockState(blockPos.add(0, 0, -1)).getBlock() == Blocks.BEDROCK)) && ((mc.world.getBlockState(blockPos.add(1, 0, 0)).getBlock() == Blocks.OBSIDIAN) || (mc.world.getBlockState(blockPos.add(1, 0, 0)).getBlock() == Blocks.BEDROCK)) && ((mc.world.getBlockState(blockPos.add(-1, 0, 0)).getBlock() == Blocks.OBSIDIAN) || (mc.world.getBlockState(blockPos.add(-1, 0, 0)).getBlock() == Blocks.BEDROCK)) && ((mc.world.getBlockState(blockPos.add(0, 0, 1)).getBlock() == Blocks.OBSIDIAN) || (mc.world.getBlockState(blockPos.add(0, 0, 1)).getBlock() == Blocks.BEDROCK)) && (mc.world.getBlockState(blockPos.add(0.5, 0.5, 0.5)).getBlock() == Blocks.AIR) && ((mc.world.getBlockState(blockPos.add(0, -1, 0)).getBlock() == Blocks.OBSIDIAN) || (mc.world.getBlockState(blockPos.add(0, -1, 0)).getBlock() == Blocks.BEDROCK));
    }
}