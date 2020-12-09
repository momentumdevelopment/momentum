package me.linus.momentum.module.modules.movement;

import me.linus.momentum.module.Module;
import net.minecraft.init.Blocks;

/**
 * @author linustouchtips
 * @since 11/30/2020
 */

public class IceSpeed extends Module {
    public IceSpeed() {
        super("IceSpeed", Category.MOVEMENT, "Allows you to move faster on ice");
    }

    public void onUpdate() {
        if (nullCheck())
            return;

        Blocks.ICE.slipperiness = 0F;
        Blocks.PACKED_ICE.slipperiness = 0F;
        Blocks.FROSTED_ICE.slipperiness = 0F;
    }

    public void onDisable() {
        Blocks.ICE.slipperiness = 0.98F;
        Blocks.PACKED_ICE.slipperiness = 0.98F;
        Blocks.FROSTED_ICE.slipperiness = 0.98F;
    }
}
