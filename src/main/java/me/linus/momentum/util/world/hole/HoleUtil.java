package me.linus.momentum.util.world.hole;

import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.util.world.BlockUtil;
import me.linus.momentum.util.world.BlockUtil.BlockResistance;
import me.linus.momentum.util.world.hole.Hole.Type;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author linustouchtips & seasnail8169
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
            if (BlockUtil.getBlockResistance(touching) != BlockResistance.Blank && BlockUtil.getBlockResistance(touching) == BlockResistance.Resistant)
                validHorizontalBlocks++;
        }

        return validHorizontalBlocks >= 4;
    }

    public static boolean isVoidHole(BlockPos blockPos) {
        return mc.player.dimension == -1 ? (blockPos.getY() == 0 || blockPos.getY() == 127) && BlockUtil.getBlockResistance(blockPos) == BlockResistance.Blank : blockPos.getY() == 0 && BlockUtil.getBlockResistance(blockPos) == BlockResistance.Blank;
    }

    public static List<Hole> getHoles(double range, boolean doubles) {
        List<Hole> holeBlocks = new ArrayList<>();

        for (BlockPos blockPos : BlockUtil.getNearbyBlocks(mc.player, range, false)) {

            BlockPos doubleBlock = BlockPos.ORIGIN;
            Facing facing = null;

            int obsidianSides = 0;
            int bedrockSides = 0;
            int airSides = 0;

            if (BlockUtil.getBlockResistance(blockPos.down()) == BlockResistance.Unbreakable)
                bedrockSides++;
            else if (BlockUtil.getBlockResistance(blockPos.down()) == BlockResistance.Resistant)
                obsidianSides++;

            if (BlockUtil.getBlockResistance(blockPos.east()) == BlockResistance.Unbreakable)
                bedrockSides++;
            else if (BlockUtil.getBlockResistance(blockPos.east()) == BlockResistance.Resistant)
                obsidianSides++;
            else if (BlockUtil.getBlockResistance(blockPos.east()) == BlockResistance.Blank) {
                airSides++;

                if (doubleBlock == BlockPos.ORIGIN)
                    doubleBlock = blockPos.east();

                if (facing == null)
                    facing = Facing.East;
            }

            if (BlockUtil.getBlockResistance(blockPos.south()) == BlockResistance.Unbreakable)
                bedrockSides++;
            else if (BlockUtil.getBlockResistance(blockPos.south()) == BlockResistance.Resistant)
                obsidianSides++;
            else if (BlockUtil.getBlockResistance(blockPos.south()) == BlockResistance.Blank) {
                airSides++;

                if (doubleBlock == BlockPos.ORIGIN)
                    doubleBlock = blockPos.south();

                if (facing == null)
                    facing = Facing.South;
            }

            if (BlockUtil.getBlockResistance(blockPos.west()) == BlockResistance.Unbreakable)
                bedrockSides++;
            else if (BlockUtil.getBlockResistance(blockPos.west()) == BlockResistance.Resistant)
                obsidianSides++;
            else if (BlockUtil.getBlockResistance(blockPos.west()) == BlockResistance.Blank) {
                airSides++;

                if (doubleBlock == BlockPos.ORIGIN)
                    doubleBlock = blockPos.west();

                if (facing == null)
                    facing = Facing.West;
            }

            if (BlockUtil.getBlockResistance(blockPos.north()) == BlockResistance.Unbreakable)
                bedrockSides++;
            else if (BlockUtil.getBlockResistance(blockPos.north()) == BlockResistance.Resistant)
                obsidianSides++;
            else if (BlockUtil.getBlockResistance(blockPos.north()) == BlockResistance.Blank) {
                airSides++;

                if (doubleBlock == BlockPos.ORIGIN)
                    doubleBlock = blockPos.north();

                if (facing == null)
                    facing = Facing.North;
            }

            if (bedrockSides == 5 && BlockUtil.getBlockResistance(blockPos) == BlockResistance.Blank)
                holeBlocks.add(new Hole(blockPos, Type.Bedrock));

            if (obsidianSides == 5 && BlockUtil.getBlockResistance(blockPos) == BlockResistance.Blank)
                holeBlocks.add(new Hole(blockPos, Type.Obsidian));

            if (bedrockSides + obsidianSides == 5 && BlockUtil.getBlockResistance(blockPos) == BlockResistance.Blank)
                holeBlocks.add(new Hole(blockPos, Type.Obsidian));

            if (doubles) {
                if (airSides == 1 && BlockUtil.getBlockResistance(blockPos) == BlockResistance.Blank) {
                    if (checkColliding(doubleBlock, facing, BlockResistance.Resistant) && BlockUtil.getBlockResistance(doubleBlock) == BlockResistance.Blank)
                        holeBlocks.add(new Hole(doubleBlock, Type.Obsidian));
                }

                if (airSides == 1 && BlockUtil.getBlockResistance(blockPos) == BlockResistance.Blank) {
                    if (checkColliding(doubleBlock, facing, new BlockResistance[] { BlockResistance.Unbreakable, BlockResistance.Resistant }) && BlockUtil.getBlockResistance(doubleBlock) == BlockResistance.Blank)
                        holeBlocks.add(new Hole(doubleBlock, Type.Obsidian));
                }

                if (airSides == 1  && BlockUtil.getBlockResistance(blockPos) == BlockResistance.Blank) {
                    if (checkColliding(doubleBlock, facing, BlockResistance.Unbreakable) && BlockUtil.getBlockResistance(doubleBlock) == BlockResistance.Blank)
                        holeBlocks.add(new Hole(doubleBlock, Type.Bedrock));
                }
            }
        }

        return holeBlocks;
    }

    public static boolean checkColliding(BlockPos blockPos, Facing facing, BlockResistance blockResistance) {
        List<BlockPos> touchingBlocks = new ArrayList<>();

        if (facing == Facing.East) {
            touchingBlocks.add(blockPos.east());
            touchingBlocks.add(blockPos.south());
            touchingBlocks.add(blockPos.north());
        }

        else if (facing == Facing.West) {
            touchingBlocks.add(blockPos.west());
            touchingBlocks.add(blockPos.south());
            touchingBlocks.add(blockPos.north());
        }

        else if (facing == Facing.North) {
            touchingBlocks.add(blockPos.east());
            touchingBlocks.add(blockPos.west());
            touchingBlocks.add(blockPos.north());
        }

        else if (facing == Facing.South) {
            touchingBlocks.add(blockPos.east());
            touchingBlocks.add(blockPos.south());
            touchingBlocks.add(blockPos.west());
        }

        for (BlockPos touchingBlock : touchingBlocks) {
            if (BlockUtil.getBlockResistance(touchingBlock) != blockResistance)
                return false;
        }

        return true;
    }

    public static boolean checkColliding(BlockPos blockPos, Facing facing, BlockResistance[] blockResistance) {
        List<BlockPos> touchingBlocks = new ArrayList<>();

        if (facing == Facing.East) {
            touchingBlocks.add(blockPos.east());
            touchingBlocks.add(blockPos.south());
            touchingBlocks.add(blockPos.north());
        }

        else if (facing == Facing.West) {
            touchingBlocks.add(blockPos.west());
            touchingBlocks.add(blockPos.south());
            touchingBlocks.add(blockPos.north());
        }

        else if (facing == Facing.North) {
            touchingBlocks.add(blockPos.east());
            touchingBlocks.add(blockPos.west());
            touchingBlocks.add(blockPos.north());
        }

        else if (facing == Facing.South) {
            touchingBlocks.add(blockPos.east());
            touchingBlocks.add(blockPos.south());
            touchingBlocks.add(blockPos.west());
        }

        for (BlockPos touchingBlock : touchingBlocks) {
            if (BlockUtil.getBlockResistance(touchingBlock) != blockResistance[0] || BlockUtil.getBlockResistance(touchingBlock) != blockResistance[1])
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

    public static List<BlockPos> getNearbyHoles(double range) {
        return BlockUtil.getNearbyBlocks(mc.player, range, false).stream().filter(HoleUtil::isHole).collect(Collectors.toList());
    }

    public enum Facing {
        North,
        South,
        East,
        West
    }
}