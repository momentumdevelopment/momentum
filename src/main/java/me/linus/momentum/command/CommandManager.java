package me.linus.momentum.command;

import com.google.common.collect.Lists;
import me.linus.momentum.Momentum;
import me.linus.momentum.command.commands.*;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

/**
 * @author linustouchtips
 * @since 12/01/2020
 */

public class CommandManager {
    public CommandManager() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    private static final List<Command> commands = Lists.newArrayList(
            new Toggle(),
            new Prefix(),
            new Help(),
            new Dupe(),
            new Friend(),
            new HClip(),
            new VClip(),
            new GoTo(),
            new Panic(),
            new Config(),
            new Cancel(),
            new Peek(),
            new Drawn()
    );

    public static List<Command> getCommands() {
        return commands;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void chatEvent(ClientChatEvent event) {
        String[] args = event.getMessage().split(" ");
        if (event.getMessage().startsWith(Momentum.PREFIX)) {
            event.setCanceled(true);

            for (Command c: commands)
                if (args[0].equalsIgnoreCase(Momentum.PREFIX + c.getUsage()))
                    c.onCommand(args);
        }
    }
}
