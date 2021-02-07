package me.linus.momentum.command.commands;

import me.linus.momentum.Momentum;
import me.linus.momentum.command.Command;
import me.linus.momentum.util.client.MessageUtil;
import me.linus.momentum.managers.social.friend.FriendManager;
import net.minecraft.util.text.TextFormatting;

/**
 * @author linustouchtips
 * @since 12/01/2020
 */

public class Friend extends Command {
    public Friend() {
        super("friend");
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length > 2) {
            if (args[1].equalsIgnoreCase("add")) {
                if (FriendManager.isFriend(args[2]))
                    MessageUtil.sendClientMessage(TextFormatting.LIGHT_PURPLE + args[2] + TextFormatting.WHITE + " is already a friend!");

                else if (!FriendManager.isFriend(args[2])) {
                    Momentum.friendManager.addFriend(args[2]);
                    MessageUtil.sendClientMessage("Added " + TextFormatting.GREEN + args[2] + TextFormatting.WHITE + " to friends list");
                }
            }

            if (args[1].equalsIgnoreCase("del") || args[1].equalsIgnoreCase("remove")) {
                if (!FriendManager.isFriend(args[2]))
                    MessageUtil.sendClientMessage(TextFormatting.LIGHT_PURPLE + args[2] + TextFormatting.WHITE + " is not a friend!");

                else if (FriendManager.isFriend(args[2])) {
                    Momentum.friendManager.removeFriend(args[2]);
                    MessageUtil.sendClientMessage("Removed " + TextFormatting.RED + args[2] + TextFormatting.WHITE + " from friends list");
                }
            }
        }

        else
            MessageUtil.usageException(this, "[add/remove] [player name]");
    }

    @Override
    public String getDescription() {
        return "Adds player to friends list";
    }
}
