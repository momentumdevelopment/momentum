package me.linus.momentum.command.commands;

import me.linus.momentum.command.Command;
import me.linus.momentum.util.client.MessageUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author linustouchtips
 * @since 12/01/2020
 */

public class Config extends Command {
    public Config() {
        super("config");
    }

    @Override
    public void onCommand(String[] args) {
        try {
            Desktop.getDesktop().open(new File("momentum"));
            MessageUtil.addOutput("Opened config folder!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDescription() {
        return "Opens the config folder";
    }
}
