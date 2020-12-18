package me.linus.momentum.command.commands;

import me.linus.momentum.command.Command;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Follow extends Command {
    public Follow() {
        super("Follow", new String[] {"follow"});
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length > 2) {
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
