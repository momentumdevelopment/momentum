package me.linus.momentum.command.commands;

import me.linus.momentum.command.Command;
import me.linus.momentum.util.client.MessageUtil;
import me.linus.momentum.util.world.WorldUtil;

public class Spawn extends Command {
    public Spawn() {
        super("spawn");
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length >= 1) {
            WorldUtil.createFakePlayer(args[1], true, true, true, false, null);
        }

        else
            MessageUtil.usageException(this, "[name]");
    }

    @Override
    public String getUsageException() {
        return "[name]";
    }

    @Override
    public String getDescription() {
        return "Spawns a fakeplayer";
    }
}
