package me.linus.momentum.command.commands;

import me.linus.momentum.command.Command;
import me.linus.momentum.managers.CommandManager;
import me.linus.momentum.util.client.MessageUtil;

public class Commands extends Command {
    public Commands() {
        super("commands");
    }

    @Override
    public void onCommand(String[] args) {
        for (Command command : CommandManager.getCommands()) {
            MessageUtil.addOutput(command.getUsage() + " - " + command.getDescription());
        }
    }

    @Override
    public String getDescription() {
        return "Lists all available commands";
    }
}
