package me.linus.momentum.command;

import com.google.common.collect.Lists;
import me.linus.momentum.Momentum;
import me.linus.momentum.command.commands.*;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.List;

public class CommandManager {
    private final List<Command> commands;

    public CommandManager() {
        commands = Lists.newArrayList(
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
            new Cancel()
        );

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void chatEvent(ClientChatEvent event) {
        String[] args = event.getMessage().split(" ");
        if (event.getMessage().startsWith(Momentum.PREFIX)) {
            event.setCanceled(true);
            for (Command c: commands){
                if (args[0].equalsIgnoreCase(Momentum.PREFIX + c.getCommand())){
                    c.onCommand(args);
                }
            }
        }
    }

    @Nullable
    public Command getCommandByName(String name){
        for (Command command : commands) {
            if (command.getCommand().equalsIgnoreCase(name))
                return command;
        }

        return null;
    }
}
