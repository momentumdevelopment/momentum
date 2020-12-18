package me.linus.momentum.command.commands;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.GoalXZ;
import me.linus.momentum.command.Command;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class GoTo extends Command {
    public GoTo() {
        super("GoTo", new String[] {"goto"});
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length > 2) {
            try {
                BaritoneAPI.getSettings().allowSprint.value = true;
                BaritoneAPI.getSettings().allowParkour.value = true;
                BaritoneAPI.getSettings().allowBreak.value = true;
                BaritoneAPI.getSettings().allowPlace.value = true;
                BaritoneAPI.getSettings().allowDownward.value = true;
                BaritoneAPI.getSettings().allowJumpAt256.value = true;
                BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(new GoalXZ(Integer.valueOf(args[1]), Integer.valueOf(args[3])));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
