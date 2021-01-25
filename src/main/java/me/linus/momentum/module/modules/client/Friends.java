package me.linus.momentum.module.modules.client;

import me.linus.momentum.module.Module;

/**
 * @author linustouchtips
 * @since 11/24/2020
 */

public class Friends extends Module {
    public Friends() {
        super("Friends", Category.CLIENT, "Allows friend system to function");
        this.enable();
        this.setDrawn(false);
    }
}
