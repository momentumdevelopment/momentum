package me.linus.momentum.module.modules.misc;

import me.linus.momentum.module.Module;
import net.minecraft.entity.player.EnumPlayerModelParts;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class SkinBlinker extends Module {
    public SkinBlinker() {
        super("SkinBlinker", Category.MISC, "Switches skin model parts");
    }

    private static final EnumPlayerModelParts[] PARTS_HORIZONTAL;

    static {
        PARTS_HORIZONTAL = new EnumPlayerModelParts[] { EnumPlayerModelParts.LEFT_SLEEVE, EnumPlayerModelParts.JACKET, EnumPlayerModelParts.HAT, EnumPlayerModelParts.LEFT_PANTS_LEG, EnumPlayerModelParts.RIGHT_PANTS_LEG, EnumPlayerModelParts.RIGHT_SLEEVE };
    }

    @Override
    public void onUpdate() {
        int i = mc.player.ticksExisted / 1 % (PARTS_HORIZONTAL.length * 2);
        boolean on = false;

        if (i >= PARTS_HORIZONTAL.length) {
            on = true;
            i -= PARTS_HORIZONTAL.length;
        }

        mc.gameSettings.setModelPartEnabled(PARTS_HORIZONTAL[i], on);
    }
}
