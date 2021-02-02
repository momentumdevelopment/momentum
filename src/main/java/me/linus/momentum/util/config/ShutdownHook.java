package me.linus.momentum.util.config;

import me.linus.momentum.Momentum;

import java.io.IOException;

/**
 * @author linustouchtips
 * @since 12/04/2020
 */

public class ShutdownHook extends Thread {

    @Override
    public void run() {
        try {
            ConfigManagerJSON.saveConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Momentum.LOGGER.info("Saving Config!");
    }
}
