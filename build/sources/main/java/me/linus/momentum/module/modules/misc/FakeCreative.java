package me.linus.momentum.module.modules.misc;

import me.linus.momentum.module.Module;
import net.minecraft.world.GameType;

/**
 * @author linustouchtips
 * @since 11/29/2020
 */

public class FakeCreative extends Module {
    public FakeCreative() {
        super("FakeCreative", Category.MISC, "Changes gamemode to creative client-side");
    }

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        mc.playerController.setGameType(GameType.CREATIVE);
    }

    @Override
    public void onDisable() {
        mc.playerController.setGameType(GameType.SURVIVAL);
    }
}
