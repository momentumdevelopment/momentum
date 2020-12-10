package me.linus.momentum.module.modules.bot;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.GoalBlock;
import me.linus.momentum.module.Module;
import me.linus.momentum.util.client.Timer;
import me.linus.momentum.util.world.PlayerUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author linustouchtips
 * @since 12/09/2020
 */

public class Milo extends Module {
    public Milo() {
        super("Milo", Category.BOT, "A bot made for anarchy");
    }

    private boolean lookingForHoles = true;

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        baritoneIntoHole();
    }

    private void baritoneIntoHole() {
        if (PlayerUtil.isInHole()) {
            lookingForHoles = false;
            return;
        }

        BlockPos goalPos = getHoles().stream().min(Comparator.comparing(c -> mc.player.getDistanceSq(c))).orElse(null);

        if (goalPos != null && lookingForHoles)
            BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(new GoalBlock(goalPos));

        if (!lookingForHoles)
            BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(new GoalBlock(null));
    }

    private List<BlockPos> getHoles() {
        NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll(getSphere(new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ)), 10, 10, false, true, 0).stream().filter(this::isHole).collect(Collectors.toList()));
        return positions;
    }

    public List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        List<BlockPos> blocks = new ArrayList<>();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();
        for (int x = cx - (int) r; x <= cx + r; x++) {
            for (int z = cz - (int) r; z <= cz + r; z++) {
                for (int y = (sphere ? cy - (int) r : cy); y < (sphere ? cy + r : cy + h); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        blocks.add(l);
                    }
                }
            }
        }
        return blocks;
    }

    private boolean isHole(BlockPos blockPos) {
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
}
