package me.linus.momentum.command.commands;

import me.linus.momentum.command.Command;
import me.linus.momentum.util.client.MessageUtil;

/**
 * @author linustouchtips
 * @since 12/01/2020
 */

public class Scale extends Command {

    public Scale() {
        super("scale");
    }

    public static float scale = 1.0f;

    @Override
    public void onCommand(String[] args) {
        if (args.length > 1) {
            scale = Float.valueOf(args[1]);
        }

        else
            MessageUtil.usageException(this, "[scale]");
    }

    @Override
    public String getDescription() {
        return "Allows you to change gui scale";
    }

    @Override
    public String getUsageException() {
        return "[scale]";
    }
}
