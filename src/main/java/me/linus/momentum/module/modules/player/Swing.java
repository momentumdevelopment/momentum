package me.linus.momentum.module.modules.player;

import me.linus.momentum.module.Module;
import net.minecraft.util.EnumHand;

/**
 * @author linustouchtips
 * @since 11/26/2020
 */

public class Swing extends Module {
    public Swing() {
        super("Swing", Category.PLAYER, "Swings with your offhand");
    }

    @Override
    public void onUpdate() {
        if (mc.world == null)
            return;

        mc.player.swingingHand = EnumHand.OFF_HAND;
    }
}
