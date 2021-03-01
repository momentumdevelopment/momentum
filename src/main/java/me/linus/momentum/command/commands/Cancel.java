package me.linus.momentum.command.commands;

import baritone.api.BaritoneAPI;
import me.linus.momentum.command.Command;
import me.linus.momentum.util.client.MessageUtil;

/**
 * @author linustouchtips
 * @since 12/01/2020
 */

public class Cancel extends Command {
    public Cancel() {
        super("cancel", "", "Cancels current baritone pathing goal");
    }

    @Override
    public void onCommand(String[] args) {
        BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoal(null);
        MessageUtil.addOutput("Canceled all baritone process!");
    }
}
