package me.linus.momentum.command.commands;

import baritone.api.BaritoneAPI;
import me.linus.momentum.command.Command;

/**
 * @author linustouchtips
 * @since 12/01/2020
 */

public class Cancel extends Command {
    public Cancel() {
        super("cancel");
    }

    @Override
    public void onCommand(String[] args) {
        BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoal(null);
    }

    @Override
    public String getDescription() {
        return "Cancels current baritone pathing goal";
    }
}
