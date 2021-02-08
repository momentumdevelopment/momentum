package me.linus.momentum.managers;

import com.google.common.collect.Lists;
import me.linus.momentum.Momentum;
import me.linus.momentum.command.Command;
import me.linus.momentum.command.commands.*;
import me.linus.momentum.module.Module;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author linustouchtips
 * @since 12/01/2020
 */

public class CommandManager {
    public CommandManager() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    static final List<Command> commands = Lists.newArrayList(
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
            new Drawn(),
            new Client(),
            new Enemy()
    );

    public static List<Command> getCommands() {
        return commands;
    }

    public static Command getCommandByUsage(String name) {
        return commands.stream().filter(command -> command.getUsage().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static Command getCommandByClass(Class<?> clazz) {
        return commands.stream().filter(command -> command.getClass().equals(clazz)).findFirst().orElse(null);
    }

    public static List<Command> predictCommands(String command) {
        return commands.stream().filter(predictCommand -> predictCommand.getUsage().startsWith(command)).collect(Collectors.toList());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void chatEvent(ClientChatEvent event) {
        String[] args = event.getMessage().split(" ");
        if (event.getMessage().startsWith(Momentum.PREFIX)) {
            event.setCanceled(true);

            for (Command c: commands) {
                if (args[0].equalsIgnoreCase(Momentum.PREFIX + c.getUsage()))
                    try {
                        c.onCommand(args);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        }
    }
}
