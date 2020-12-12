package me.linus.momentum.module.modules.movement;

import me.linus.momentum.module.Module;

/**
 * @author linustouchtips
 * @since 12/11/2020
 */

public class SafeWalk extends Module {
    public SafeWalk() {
        super("SafeWalk", Category.MOVEMENT, "Prevents you from walking off edges");
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (mc.player.onGround && !mc.player.isSneaking() && !mc.gameSettings.keyBindSneak.isPressed() && !mc.gameSettings.keyBindJump.isPressed() && mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, -0.5, 0.0).expand(-0.001, 0.0, -0.001)).isEmpty()) {
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
            mc.player.setSneaking(true);
        }
    }
}
