package me.linus.momentum.managers.config;

import me.linus.momentum.Momentum;

/**
 * @author linustouchtips
 * @since 12/04/2020
 */

public class ShutdownHook extends Thread {

    @Override
    public void run() {
        ConfigManagerJSON.saveConfig();
        Momentum.LOGGER.info("Saving Config!");
    }
}
