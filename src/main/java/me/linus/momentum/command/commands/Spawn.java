package me.linus.momentum.command.commands;

import me.linus.momentum.command.Command;
import me.linus.momentum.util.client.MessageUtil;
import me.linus.momentum.util.world.WorldUtil;

public class Spawn extends Command {
    public Spawn() {
        super("spawn");
    }

    int offset = 6641;

    @Override
    public void onCommand(String[] args) {
        offset++;

        if (args.length >= 1)
            WorldUtil.createFakePlayer(args[1], true, true, true, false, offset);
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
