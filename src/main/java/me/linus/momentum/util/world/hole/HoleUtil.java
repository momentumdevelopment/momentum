package me.linus.momentum.util.world.hole;

import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.util.world.BlockUtil;
import me.linus.momentum.util.world.BlockUtil.BlockResistance;
import me.linus.momentum.util.world.hole.Hole.Type;
import me.linus.momentum.util.world.hole.Hole.Facing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linustouchtips & yoink
 * @since 12/29/2020
 */

public class HoleUtil implements MixinInterface {

    public static boolean isInHole(EntityPlayer entityPlayer) {
        return isHole(new BlockPos(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ));
    }

    public static boolean isVoidHole(BlockPos blockPos) {
        return mc.player.dimension == -1 ? (blockPos.getY() == 0 || blockPos.getY() == 127) && BlockUtil.getBlockResistance(blockPos) == BlockResistance.Blank : blockPos.getY() == 0 && BlockUtil.getBlockResistance(blockPos) == BlockResistance.Blank;
    }

    public static List<Hole> getHoles(double range) {
        List<Hole> holes = new ArrayList<>();

        for (BlockPos pos : BlockUtil.getNearbyBlocks(mc.player, range, false)) {
            if (isObsidianHole(pos))
                holes.add(new Hole(Type.Obsidian, Facing.None, pos));

            if (isBedRockHole(pos))
                holes.add(new Hole(Type.Bedrock, Facing.None, pos));
        }

        return holes;
    }

    public static boolean isDoubleBedrockHoleX(BlockPos blockPos) {
        if (!mc.world.getBlockState(blockPos).getBlock().equals(Blocks.AIR) || !mc.world.getBlockState(blockPos.add(1, 0, 0)).getBlock().equals(Blocks.AIR) || !mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock().equals(Blocks.AIR) && !mc.world.getBlockState(blockPos.add(1, 1, 0)).getBlock().equals(Blocks.AIR) || !mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock().equals(Blocks.AIR) && !mc.world.getBlockState(blockPos.add(1, 2, 0)).getBlock().equals(Blocks.AIR))
            return false;

        for (BlockPos blockPos2 : new BlockPos[]{blockPos.add(2, 0, 0), blockPos.add(1, 0, 1), blockPos.add(1, 0, -1), blockPos.add(-1, 0, 0), blockPos.add(0, 0, 1), blockPos.add(0, 0, -1), blockPos.add(0, -1, 0), blockPos.add(1, -1, 0)}) {
            IBlockState iBlockState = mc.world.getBlockState(blockPos2);

            if (iBlockState.getBlock() != Blocks.AIR && (iBlockState.getBlock() == Blocks.BEDROCK))
                continue;

            return false;
        }

        return true;
    }

    public static boolean isDoubleBedrockHoleZ(BlockPos blockPos) {
        if (!mc.world.getBlockState(blockPos).getBlock().equals(Blocks.AIR) || !mc.world.getBlockState(blockPos.add(0, 0, 1)).getBlock().equals(Blocks.AIR) || !mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock().equals(Blocks.AIR) && !mc.world.getBlockState(blockPos.add(0, 1, 1)).getBlock().equals(Blocks.AIR) || !mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock().equals(Blocks.AIR) && !mc.world.getBlockState(blockPos.add(0, 2, 1)).getBlock().equals(Blocks.AIR))
            return false;

        for (BlockPos blockPos2 : new BlockPos[]{blockPos.add(0, 0, 2), blockPos.add(1, 0, 1), blockPos.add(-1, 0, 1), blockPos.add(0, 0, -1), blockPos.add(1, 0, 0), blockPos.add(-1, 0, 0), blockPos.add(0, -1, 0), blockPos.add(0, -1, 1)}) {
            IBlockState iBlockState = mc.world.getBlockState(blockPos2);

            if (iBlockState.getBlock() != Blocks.AIR && (iBlockState.getBlock() == Blocks.BEDROCK))
                continue;

            return false;
        }

        return true;
    }

    public static boolean isDoubleObsidianHoleX(BlockPos blockPos) {
        if (!mc.world.getBlockState(blockPos).getBlock().equals(Blocks.AIR) || !mc.world.getBlockState(blockPos.add(1, 0, 0)).getBlock().equals(Blocks.AIR) || !mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock().equals(Blocks.AIR) && !mc.world.getBlockState(blockPos.add(1, 1, 0)).getBlock().equals(Blocks.AIR) || !mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock().equals(Blocks.AIR) && !mc.world.getBlockState(blockPos.add(1, 2, 0)).getBlock().equals(Blocks.AIR))
            return false;

        for (BlockPos blockPos2 : new BlockPos[]{blockPos.add(2, 0, 0), blockPos.add(1, 0, 1), blockPos.add(1, 0, -1), blockPos.add(-1, 0, 0), blockPos.add(0, 0, 1), blockPos.add(0, 0, -1), blockPos.add(0, -1, 0), blockPos.add(1, -1, 0)}) {
            IBlockState iBlockState = mc.world.getBlockState(blockPos2);

            if (iBlockState.getBlock() != Blocks.AIR && (iBlockState.getBlock() == Blocks.OBSIDIAN))
                continue;

            return false;
        }
        
        return true;
    }

    public static boolean isDoubleObsidianHoleZ(BlockPos blockPos) {
        if (!mc.world.getBlockState(blockPos).getBlock().equals(Blocks.AIR) || !mc.world.getBlockState(blockPos.add(0, 0, 1)).getBlock().equals(Blocks.AIR) || !mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock().equals(Blocks.AIR) && !mc.world.getBlockState(blockPos.add(0, 1, 1)).getBlock().equals(Blocks.AIR) || !mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock().equals(Blocks.AIR) && !mc.world.getBlockState(blockPos.add(0, 2, 1)).getBlock().equals(Blocks.AIR))
            return false;

        for (BlockPos blockPos2 : new BlockPos[]{blockPos.add(0, 0, 2), blockPos.add(1, 0, 1), blockPos.add(-1, 0, 1), blockPos.add(0, 0, -1), blockPos.add(1, 0, 0), blockPos.add(-1, 0, 0), blockPos.add(0, -1, 0), blockPos.add(0, -1, 1)}) {
            IBlockState iBlockState = mc.world.getBlockState(blockPos2);

            if (iBlockState.getBlock() != Blocks.AIR && (iBlockState.getBlock() == Blocks.OBSIDIAN))
                continue;

            return false;
        }

        return true;
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
}