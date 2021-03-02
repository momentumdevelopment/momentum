package me.linus.momentum.command.commands;

import me.linus.momentum.command.Command;
import me.linus.momentum.util.client.MessageUtil;
import net.minecraft.util.math.BlockPos;

/**
 * @author linustouchtips
 * @since 12/01/2020
 */

public class Dupe extends Command {
    public Dupe() {
        super("dupe", "[x] [y] [z]", "Sets the starting position for the dupe");
    }

    public static BlockPos startingPosition;

    @Override
    public void onCommand(String[] args) {
        if (args.length > 2) {
            MessageUtil.addOutput("Starting position set at " + args[1] + ", " + args[2]  + ", " + args[3] + "!");
            startingPosition = new BlockPos(Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
        }

        else
            MessageUtil.usageException(this.getUsage(), this.getUsageException());
    }
}