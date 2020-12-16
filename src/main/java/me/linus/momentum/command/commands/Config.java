package me.linus.momentum.command.commands;

import me.linus.momentum.command.Command;

import java.awt.*;
import java.io.File;

public class Config extends Command {
    public Config() {
        super("Config", new String[]{"config"});
    }

    @Override
    public void onCommand(String[] args) {
            try {
                Desktop.getDesktop().open(new File("momentum"));
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
