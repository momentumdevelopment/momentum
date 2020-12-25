package me.linus.momentum.module.modules.movement;

import me.linus.momentum.module.Module;

/**
 * @author linustouchtips
 * @since 11/26/2020
 */

public class Parkour extends Module {
    public Parkour() {
        super("Parkour", Category.MOVEMENT, "Automatically jumps at the edge of a block");
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (mc.player.onGround && !mc.player.isSneaking() && !mc.gameSettings.keyBindSneak.isPressed() && !mc.gameSettings.keyBindJump.isPressed() && mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, -0.5, 0.0).expand(-0.001, 0.0, -0.001)).isEmpty())
            mc.player.jump();
    }
}
