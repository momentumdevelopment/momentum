package me.linus.momentum.command;

/**
 * @author linustouchtips
 * @since 12/01/2020
 */

public class Command {

    String usage;
    String specialUsage;
    String description;

    public Command(String usage, String specialUsage, String description) {
        this.usage = usage;
        this.specialUsage = specialUsage;
        this.description = description;
    }

    public void onCommand(String[] args) {}

    public String getUsage() {
        return usage;
    }

    public String getDescription() {
        return this.description;
    }

    public String getUsageException() {
        return this.specialUsage;
    }
}
