package me.linus.momentum.command.commands;

import me.linus.momentum.command.Command;
import me.linus.momentum.util.config.ConfigManager2;

import java.io.IOException;

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
        try {
            ConfigManager2.saveModule();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDescription() {
        return "Saves current config";
    }
}
