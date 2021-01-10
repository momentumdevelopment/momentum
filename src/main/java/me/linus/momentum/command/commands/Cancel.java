package me.linus.momentum.command.commands;

import baritone.api.BaritoneAPI;
import me.linus.momentum.command.Command;
import me.linus.momentum.mixin.MixinInterface;

/**
 * @author linustouchtips
 * @since 12/01/2020
 */

public class Cancel extends Command implements MixinInterface {
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
