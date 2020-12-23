package me.linus.momentum.module.modules.movement;

import me.linus.momentum.event.events.player.MoveEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.world.PlayerUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author linustouchtips
 * @since 12/19/2020
 */

public class Jesus extends Module {
    public Jesus() {
        super("Jesus", Category.MOVEMENT, "Allows you to walk on water");
    }

    private static final Mode mode = new Mode("Mode", "Normal", "Packet", "Push", "Freeze");
    public static Slider offset = new Slider("Offset", 0.0D, 0.2D, 1.0D, 2);
    private static final Slider delay = new Slider("Delay", 0.0D, 2.0D, 10.0D, 0);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(offset);
        addSetting(delay);
    }

    @SubscribeEvent
    public void onMoveEvent(MoveEvent event) {
        if (nullCheck())
            return;

        if (PlayerUtil.isInLiquid() && !mc.gameSettings.keyBindJump.isPressed())
            event.setY(0);
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}
