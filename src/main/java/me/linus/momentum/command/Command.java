package me.linus.momentum.command;

import me.linus.momentum.mixin.MixinInterface;

/**
 * @author linustouchtips
 * @since 12/01/2020
 */

public class Command implements MixinInterface {
    String usage;

    public Command(String usage) {
        this.usage = usage;
    }

    public void onCommand(String[] args) {}

    public String getUsage() {
        return usage;
    }

    public String getDescription() {
        return "";
    }
}
