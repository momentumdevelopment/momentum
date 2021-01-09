package me.linus.momentum.command.commands;

import me.linus.momentum.Momentum;
import me.linus.momentum.command.Command;
import me.linus.momentum.util.client.MessageUtil;

/**
 * @author linustouchtips
 * @since 01/08/2021
 */

public class Client extends Command {
    public Client() {
        super("client");
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length >= 1)
            Momentum.NAME = args[1];
        else
            MessageUtil.usageException(this, "[client name]");
    }

    @Override
    public String getDescription() {
        return "Changes the client name";
    }
}
