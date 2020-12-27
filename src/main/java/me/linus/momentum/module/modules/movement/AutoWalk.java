package me.linus.momentum.module.modules.movement;

import me.linus.momentum.module.Module;
import net.minecraft.client.settings.KeyBinding;

/**
 * @author linustouchtips
 * @since 11/26/2020
 */

public class AutoWalk extends Module {
    public AutoWalk() {
        super("AutoWalk", Category.MOVEMENT, "Automatically walks");
    }

    @Override
    public void onUpdate() {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
    }
}