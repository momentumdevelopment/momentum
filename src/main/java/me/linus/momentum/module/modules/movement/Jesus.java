package me.linus.momentum.module.modules.movement;

import me.linus.momentum.event.events.player.MoveEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.util.world.EntityUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author linustouchtips
 * @since 12/19/2020
 */

public class Jesus extends Module {
    public Jesus() {
        super("Jesus", Category.MOVEMENT, "Allows you to walk on water");
    }

    @SubscribeEvent
    public void onMoveEvent(MoveEvent event) {
        if (EntityUtil.isInWater(mc.player) && !mc.gameSettings.keyBindJump.isPressed()) {
            event.setY(0);
        }
    }
}
