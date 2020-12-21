package me.linus.momentum.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.linus.momentum.Momentum;
import me.linus.momentum.command.Command;
import me.linus.momentum.util.client.external.MessageUtil;
import org.lwjgl.input.Keyboard;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Prefix extends Command {
    public Prefix() {
        super("Prefix", new String[]{"prefix"});
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length > 1) {
            try {
                Momentum.PREFIX = args[1];
                MessageUtil.sendClientMessage(ChatFormatting.LIGHT_PURPLE + "Default Prefix" + ChatFormatting.WHITE + " is now" + ChatFormatting.RED + args[2].toUpperCase() + ChatFormatting.GRAY + " (" + ChatFormatting.WHITE + Keyboard.getKeyIndex(args[2].toUpperCase() + "") + ChatFormatting.GRAY + ")");
            } catch (Exception e) {

            }
        }
    }
}

