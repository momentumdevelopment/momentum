package me.linus.momentum.command.commands;

import me.linus.momentum.command.Command;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author linustouchtips
 * @since 12/01/2020
 */

public class Config extends Command {
    public Config() {
        super("config");
    }

    @Override
    public void onCommand(String[] args) {
        try {
            Desktop.getDesktop().open(new File("momentum"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDescription() {
        return "Opens the config folder";
    }
}
