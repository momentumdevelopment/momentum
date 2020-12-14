package me.linus.momentum.module.modules.bot;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.GoalBlock;
import me.linus.momentum.module.Module;
import me.linus.momentum.util.world.BlockUtils;
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

        BlockPos goalPos = getHoles(20).stream().min(Comparator.comparing(c -> mc.player.getDistanceSq(c))).orElse(null);

        if (goalPos != null && lookingForHoles)
            BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(new GoalBlock(goalPos));

        if (!lookingForHoles)
            BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(new GoalBlock(null));
    }

    public List<BlockPos> getHoles(double range) {
        NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll(BlockUtils.getSphere(new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ)), (int) range, (int) range, false, true, 0).stream().filter(BlockUtils::isHole).collect(Collectors.toList()));
        return positions;
    }
}
