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

    static final EnumPlayerModelParts[] PARTS_HORIZONTAL;

    @Override
    public void onUpdate() {
        int delay = mc.player.ticksExisted % (PARTS_HORIZONTAL.length * 2);
        boolean renderLayer = false;

        if (delay >= PARTS_HORIZONTAL.length) {
            renderLayer = true;
            delay -= PARTS_HORIZONTAL.length;
        }

        mc.gameSettings.setModelPartEnabled(PARTS_HORIZONTAL[delay], renderLayer);
    }

    static {
        PARTS_HORIZONTAL = new EnumPlayerModelParts[] { EnumPlayerModelParts.LEFT_SLEEVE, EnumPlayerModelParts.JACKET, EnumPlayerModelParts.HAT, EnumPlayerModelParts.LEFT_PANTS_LEG, EnumPlayerModelParts.RIGHT_PANTS_LEG, EnumPlayerModelParts.RIGHT_SLEEVE };
    }
}