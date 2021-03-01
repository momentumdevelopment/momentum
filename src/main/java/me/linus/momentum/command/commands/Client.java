package me.linus.momentum.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.linus.momentum.Momentum;
import me.linus.momentum.command.Command;
import me.linus.momentum.util.client.MessageUtil;

/**
 * @author linustouchtips
 * @since 01/31/2020
 */

public class Client extends Command {
    public Client() {
        super("client", "[new client name]","Changes the client name");
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length > 0) {
            Momentum.NAME = args[1];
            MessageUtil.addOutput(ChatFormatting.LIGHT_PURPLE + "Client Name" + ChatFormatting.WHITE + " is now " + ChatFormatting.LIGHT_PURPLE + args[1].toUpperCase());
        }

        else
            MessageUtil.usageException(this.getUsage(), this.getUsageException());
    }
}
