package me.linus.momentum.command.commands;

import me.linus.momentum.Momentum;
import me.linus.momentum.command.Command;
import me.linus.momentum.util.client.external.MessageUtil;
import me.linus.momentum.util.client.friend.FriendManager;
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
            MessageUtil.sendClientMessage("Usage : " + Momentum.PREFIX + "friend [add/del] [playername]");
        } try {
            if (args[1].equalsIgnoreCase("add")){
                if (FriendManager.isFriend(args[2])) {
                    MessageUtil.sendClientMessage(TextFormatting.LIGHT_PURPLE + args[2] + TextFormatting.WHITE + " is already a friend!");
                    return;
                }

                if (!FriendManager.isFriend(args[2])){
                    Momentum.friendManager.addFriend(args[2]);
                    MessageUtil.sendClientMessage("Added " + TextFormatting.GREEN + args[2] + TextFormatting.WHITE + " to friends list");
                }

            }

            if (args[1].equalsIgnoreCase("del") || args[1].equalsIgnoreCase("remove")){
                if (!FriendManager.isFriend(args[2])){
                    MessageUtil.sendClientMessage(TextFormatting.LIGHT_PURPLE + args[2] + TextFormatting.WHITE + " is not a friend!");
                    return;
                }

                if (FriendManager.isFriend(args[2])){
                    Momentum.friendManager.removeFriend(args[2]);
                    MessageUtil.sendClientMessage("Removed " + TextFormatting.RED + args[2] + TextFormatting.WHITE + " from friends list");
                }
            }
        } catch(ArrayIndexOutOfBoundsException e) {
            MessageUtil.sendClientMessage(Momentum.PREFIX + "friend [add/del] [playername]");
        }
    }

    @Override
    public String getDescription() {
        return "Adds player to friends list";
    }
}
