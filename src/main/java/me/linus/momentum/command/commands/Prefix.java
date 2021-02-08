package me.linus.momentum.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.linus.momentum.Momentum;
import me.linus.momentum.command.Command;
import me.linus.momentum.util.client.MessageUtil;
import org.lwjgl.input.Keyboard;

/**
 * @author linustouchtips
 * @since 12/01/2020
 */

public class Prefix extends Command {
    public Prefix() {
        super("prefix");
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length > 0) {
            Momentum.PREFIX = args[1];
            MessageUtil.sendClientMessage(ChatFormatting.LIGHT_PURPLE + "Prefix" + ChatFormatting.WHITE + " is now " + ChatFormatting.RED + args[1].toUpperCase() + ChatFormatting.GRAY + " (" + ChatFormatting.WHITE + Keyboard.getKeyIndex(args[1].toUpperCase() + "") + ChatFormatting.GRAY + ")");
        }

        else
            MessageUtil.usageException(this, "[new prefix]");
    }

    @Override
    public String getDescription() {
        return "Changes the command prefix";
    }

    @Override
    public String getUsageException() {
        return "[new prefix]";
    }
}

