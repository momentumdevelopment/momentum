package me.linus.momentum.module.modules.client;

import me.linus.momentum.module.Module;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Capes extends Module {
    public Capes() {
        super("Capes", Category.CLIENT, "Adds a custom cape to the player");
        this.setDrawn(false);
    }
}