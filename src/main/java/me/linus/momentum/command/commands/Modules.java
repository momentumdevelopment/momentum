package me.linus.momentum.command.commands;

import me.linus.momentum.command.Command;
import me.linus.momentum.managers.ModuleManager;
import me.linus.momentum.module.Module;
import me.linus.momentum.util.client.MessageUtil;

public class Modules extends Command {
    public Modules() {
        super("modules", "", "Lists all modules");
    }

    @Override
    public void onCommand(String[] args) {
        for (Module module : ModuleManager.getModules()) {
            MessageUtil.addOutput(module.getName() + " - " + module.getDescription());
        }
    }
}
