package me.linus.momentum.command.commands;

import me.linus.momentum.Momentum;
import me.linus.momentum.command.Command;
import me.linus.momentum.util.client.external.MessageUtil;
import net.minecraft.util.text.TextFormatting;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Help extends Command {
    public Help() {
        super("Help", new String[] {"help", "command", "commands"});
    }

    @Override
    public void onCommand(String[] args) {
        MessageUtil.sendClientMessage(TextFormatting.BLUE + "[Momentum Help]");
        MessageUtil.sendClientMessage(TextFormatting.BLUE + "[Current Prefix: " + Momentum.PREFIX + "]");
        MessageUtil.sendClientMessage(TextFormatting.WHITE + Momentum.PREFIX + "toggle [module] - toggles a specific modules");
        MessageUtil.sendClientMessage(TextFormatting.WHITE + Momentum.PREFIX + "prefix [character] - changes the prefix");
        MessageUtil.sendClientMessage(TextFormatting.WHITE + Momentum.PREFIX + "help - shows all the commands");
        MessageUtil.sendClientMessage(TextFormatting.WHITE + Momentum.PREFIX + "friend [add/del] [name] - adds/removes a friend from your friend list");
        MessageUtil.sendClientMessage(TextFormatting.WHITE + Momentum.PREFIX + "drawn - adds/removes a module from the array list");
        MessageUtil.sendClientMessage(TextFormatting.WHITE + Momentum.PREFIX + "hclip [blocks] - teleports you forward a certain number of blocks");
        MessageUtil.sendClientMessage(TextFormatting.WHITE + Momentum.PREFIX + "vclip [blocks] - teleports you upward a certain number of blocks");
        MessageUtil.sendClientMessage(TextFormatting.WHITE + Momentum.PREFIX + "set [module] [setting] [value] - sets a setting to an exact value");
        MessageUtil.sendClientMessage(TextFormatting.WHITE + Momentum.PREFIX + "dupe [x y z] - sets the starting position for autodupe");
        MessageUtil.sendClientMessage(TextFormatting.WHITE + Momentum.PREFIX + "set [module] [setting] [value] - sets a setting to an exact value");
        MessageUtil.sendClientMessage(TextFormatting.WHITE + Momentum.PREFIX + "panic - disables all modules");
        MessageUtil.sendClientMessage(TextFormatting.WHITE + Momentum.PREFIX + "goto [x y z] - baritones you to the set coordinates");
        MessageUtil.sendClientMessage(TextFormatting.WHITE + Momentum.PREFIX + "cancel - stops baritone");
        MessageUtil.sendClientMessage(TextFormatting.WHITE + Momentum.PREFIX + "config - opens the config folder");
    }
}
