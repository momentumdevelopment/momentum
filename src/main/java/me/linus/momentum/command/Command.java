package me.linus.momentum.command;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Command {
    private final String command;
    private final String[] usage;

    public Command(String name, String[] usage){
        this.command = name;
        this.usage = usage;
    }

    public void onCommand(String[] args){}

    public String getCommand() {
        return command;
    }

    public String[] getUsage() {
        return usage;
    }
}
