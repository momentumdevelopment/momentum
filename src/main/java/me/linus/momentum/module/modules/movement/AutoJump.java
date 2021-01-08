package me.linus.momentum.module.modules.movement;

import me.linus.momentum.module.Module;
import net.minecraft.client.settings.KeyBinding;

/**
 * @author linustouchtips
 * @since 11/26/2020
 */

public class AutoJump extends Module {
    public AutoJump() {
        super("AutoJump", Category.MOVEMENT, "Automatically jumps");
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), true);
    }
}
