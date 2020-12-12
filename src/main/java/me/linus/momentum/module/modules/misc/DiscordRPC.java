package me.linus.momentum.module.modules.misc;

import me.linus.momentum.module.Module;
import me.linus.momentum.util.client.DiscordPresence;
import me.linus.momentum.util.client.MessageUtil;

/**
 * @author linustouchtips
 * @since 11/30/2020
 */

public class DiscordRPC extends Module {
    public DiscordRPC() {
        super("DiscordRPC", Category.MISC, "Displays a custom Discord Rich Presence");
    }

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        MessageUtil.sendClientMessage("Discord Rich Presence started!");
        DiscordPresence.startup();
    }

    @Override
    public void onDisable() {
        MessageUtil.sendClientMessage("Discord Rich Presence shutdown!");
        DiscordPresence.shutdown();
    }
}
