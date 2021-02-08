package me.linus.momentum.command.commands;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.GoalXZ;
import me.linus.momentum.command.Command;
import me.linus.momentum.util.client.MessageUtil;

/**
 * @author linustouchtips
 * @since 12/01/2020
 */

public class GoTo extends Command {
    public GoTo() {
        super("goto");
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length > 2)
            BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(new GoalXZ(Integer.valueOf(args[1]), Integer.valueOf(args[3])));

        else
            MessageUtil.usageException(this, "[x] [y] [z]");
    }

    @Override
    public String getDescription() {
        return "Sets a baritone xyz goal path";
    }

    @Override
    public String getUsageException() {
        return "[x] [y] [z]";
    }
}
