package me.linus.momentum.command.commands;

import me.linus.momentum.Momentum;
import me.linus.momentum.command.Command;
import me.linus.momentum.managers.social.enemy.EnemyManager;
import me.linus.momentum.util.client.MessageUtil;
import net.minecraft.util.text.TextFormatting;

/**
 * @author linustouchtips
 * @since 02/01/2021
 */

public class Enemy extends Command {
    public Enemy() {
        super("enemy", "[add/remove] [player name]", "Adds player to enemies list");
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length > 2) {
            if (args[1].equalsIgnoreCase("add")) {
                if (EnemyManager.isEnemy(args[2]))
                    MessageUtil.addOutput(TextFormatting.LIGHT_PURPLE + args[2] + TextFormatting.WHITE + " is already an enemy!");

                else if (!EnemyManager.isEnemy(args[2])) {
                    Momentum.enemyManager.addEnemy(args[2]);
                    MessageUtil.addOutput("Added " + TextFormatting.GREEN + args[2] + TextFormatting.WHITE + " to enemies list");
                }
            }

            if (args[1].equalsIgnoreCase("del") || args[1].equalsIgnoreCase("remove")) {
                if (!EnemyManager.isEnemy(args[2]))
                    MessageUtil.addOutput(TextFormatting.LIGHT_PURPLE + args[2] + TextFormatting.WHITE + " is not an enemy!");

                else if (EnemyManager.isEnemy(args[2])) {
                    Momentum.enemyManager.removeEnemy(args[2]);
                    MessageUtil.addOutput("Removed " + TextFormatting.RED + args[2] + TextFormatting.WHITE + " from enemies list");
                }
            }
        }

        else
            MessageUtil.usageException(this.getUsage(), this.getUsageException());
    }
}
