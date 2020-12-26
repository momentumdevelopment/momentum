package me.linus.momentum.command.commands;

import me.linus.momentum.command.Command;
import me.linus.momentum.module.ModuleManager;
import me.linus.momentum.util.client.external.MessageUtil;
import net.minecraft.util.text.TextFormatting;

public class Drawn extends Command {
    public Drawn() {
        super("drawn");
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length > 0) {
            if (!ModuleManager.getModuleByName(args[1]).isDrawn()) {
                ModuleManager.getModuleByName(args[1]).setDrawn(false);
                MessageUtil.sendClientMessage(args + "is now" + TextFormatting.RED + " hidden!");
            }

            else {
                ModuleManager.getModuleByName(args[1]).setDrawn(true);
                MessageUtil.sendClientMessage(args + "is now" + TextFormatting.GREEN + " drawn!");
            }
        }
    }
}
