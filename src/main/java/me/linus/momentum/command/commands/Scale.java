package me.linus.momentum.command.commands;

import me.linus.momentum.command.Command;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Scale extends Command {

    public Scale() {
        super("Scale", new String[] {"gui"});
    }

    public float scale = 1.0f;

    @Override
    public void onCommand(String[] args) {
        if (args.length > 1) {
            try {
                scale = Float.valueOf(args[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
