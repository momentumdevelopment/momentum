package me.linus.momentum.command.commands;

import me.linus.momentum.command.Command;
import me.linus.momentum.util.client.external.MessageUtil;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Dupe extends Command {
    public Dupe() {
        super("Dupe", new String[]{"dupe"});
    }

    int x = 0;
    int y = 0;
    int z = 0;

    @Override
    public void onCommand(String[] args) {
        if (args.length > 2) {
            try {
                MessageUtil.sendClientMessage(" Starting position set at " + args[1] + ", " + args[2]  + ", " + args[3] + "!");
                x = Integer.valueOf(args[1]);
                y = Integer.valueOf(args[2]);
                z = Integer.valueOf(args[3]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}