package me.linus.momentum.command.commands;

import me.linus.momentum.Momentum;
import me.linus.momentum.command.Command;
import me.linus.momentum.module.Module;
import me.linus.momentum.util.client.MessageUtil;
import net.minecraft.util.text.TextFormatting;

/**
 * @author linustouchtips
 * @since 12/25/2020
 */

public class Drawn extends Command {
    public Drawn() {
        super("drawn", "[module name]", "Hides or draws a module on the arraylist");
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length > 1) {
            for (Module module : Momentum.moduleManager.getModules()) {
                if (module.getName().equalsIgnoreCase(args[1]) && !module.isDrawn()) {
                    MessageUtil.addOutput(TextFormatting.AQUA + module.getName() + TextFormatting.WHITE + " is now " + TextFormatting.GREEN + "DRAWN");
                    module.setDrawn(true);
                }

                else if (module.getName().equalsIgnoreCase(args[1]) && module.isDrawn()) {
                    MessageUtil.addOutput(TextFormatting.AQUA + module.getName() + TextFormatting.WHITE + " is now " + TextFormatting.RED + "HIDDEN");
                    module.setDrawn(false);
                }
            }
        }

        else
            MessageUtil.usageException(this.getUsage(), this.getUsageException());
    }
}
