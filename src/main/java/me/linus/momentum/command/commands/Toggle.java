package me.linus.momentum.command.commands;

import me.linus.momentum.Momentum;
import me.linus.momentum.command.Command;
import me.linus.momentum.module.Module;
import me.linus.momentum.util.client.MessageUtil;
import net.minecraft.util.text.TextFormatting;

/**
 * @author linustouchtips
 * @since 12/01/2020
 */

public class Toggle extends Command {
    public Toggle() {
        super("toggle", "[module name]", "Toggles the specified module");
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length >= 1) {
            for (Module m: Momentum.moduleManager.getModules()) {
                if (m.getName().equalsIgnoreCase(args[1])) {
                    m.toggle();

                    if (m.isEnabled())
                        MessageUtil.addOutput(m.getName() + " is now " + TextFormatting.GREEN + "ENABLED");

                    else
                        MessageUtil.addOutput(m.getName() + " is now " + TextFormatting.RED + "DISABLED");
                }
            }
        }

        else
            MessageUtil.usageException(this.getUsage(), this.getUsageException());
    }
}
