package me.linus.momentum.command.commands;

import me.linus.momentum.command.Command;
import me.linus.momentum.util.client.MessageUtil;

/**
 * @author linustouchtips
 * @since 12/01/2020
 */

public class Dupe extends Command {
    public Dupe() {
        super("dupe");
    }

    public static int x = 0;
    public static int y = 0;
    public static int z = 0;

    @Override
    public void onCommand(String[] args) {
        if (args.length > 2) {
            MessageUtil.sendClientMessage(" Starting position set at " + args[1] + ", " + args[2]  + ", " + args[3] + "!");
            x = Integer.valueOf(args[1]);
            y = Integer.valueOf(args[2]);
            z = Integer.valueOf(args[3]);
        }

        else
            MessageUtil.usageException(this, "[x y z]");
    }

    @Override
    public String getDescription() {
        return "Sets the starting position for the dupe";
    }

}