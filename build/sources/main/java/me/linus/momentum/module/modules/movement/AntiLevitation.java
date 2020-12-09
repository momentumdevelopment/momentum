package me.linus.momentum.module.modules.movement;

import me.linus.momentum.module.Module;
import net.minecraft.potion.Potion;

import java.util.Objects;

/**
 * @author linustouchtips
 * @since 12/07/2020
 */

public class AntiLevitation extends Module {
    public AntiLevitation() {
        super("AntiLevitation", Category.MOVEMENT, "Removes the levitation potion effect");
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (mc.player.isPotionActive(Objects.requireNonNull(Potion.getPotionFromResourceLocation("levitation"))))
            mc.player.removeActivePotionEffect(Potion.getPotionFromResourceLocation("levitation"));
    }
}
