package me.linus.momentum.command.commands;

import me.linus.momentum.util.client.MessageUtil;
import net.minecraft.util.text.TextFormatting;
import me.linus.momentum.Momentum;
import me.linus.momentum.module.Module;
import me.linus.momentum.command.Command;

public class Toggle extends Command {
    public Toggle() {
        super("Toggle", new String[]{"toggle", "t"});
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length > 1) {
            try {
            for (Module m: Momentum.moduleManager.getModules()) {
                if (m.getName().equalsIgnoreCase(args[1])) {
                    m.toggle();
                    if (m.isEnabled()) {
                        MessageUtil.sendClientMessage(TextFormatting.AQUA + m.getName() + TextFormatting.WHITE + " is now " + TextFormatting.GREEN + "ENABLED");
                    } else {
                        MessageUtil.sendClientMessage(TextFormatting.AQUA + m.getName() + TextFormatting.WHITE + " is now " + TextFormatting.RED + "DISABLED");
                    }
                }
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
