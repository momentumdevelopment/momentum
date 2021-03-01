package me.linus.momentum.command.commands;

import me.linus.momentum.command.Command;
import me.linus.momentum.util.client.MessageUtil;

/**
 * @author linustouchtips
 * @since 12/01/2020
 */

public class Follow extends Command {
    public Follow() {
        super("follow", "[add/remove] [player name]", "Follows a specified player");
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length > 2) {

        }

        else
            MessageUtil.usageException(this.getUsage(), this.getUsageException());
    }
}
