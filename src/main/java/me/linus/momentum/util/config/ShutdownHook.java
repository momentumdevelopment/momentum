package me.linus.momentum.util.config;

import me.linus.momentum.Momentum;

/**
 * @author linustouchtips
 * @since 12/04/2020
 */

public class ShutdownHook extends Thread {

    @Override
    public void run() {
        ConfigManager.saveConfig();

        Momentum.LOGGER.info("Saving Config!");
    }
}
