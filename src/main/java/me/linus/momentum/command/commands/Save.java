package me.linus.momentum.command.commands;

import me.linus.momentum.command.Command;
import me.linus.momentum.util.config.ConfigManager2;

/**
 * @author linustouchtips
 * @since 01/18/2021
 */

public class Save extends Command {
    public Save() {
        super("save");
    }

    @Override
    public void onCommand(String[] args) {
        ConfigManager2.saveModule();
    }

    @Override
    public String getDescription() {
        return "Saves current config";
    }
}
