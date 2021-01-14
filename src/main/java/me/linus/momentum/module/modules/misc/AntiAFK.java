package me.linus.momentum.module.modules.misc;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.world.Timer;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class AntiAFK extends Module {
    public AntiAFK() {
        super("AntiAFK", Category.MISC, "Prevents you from getting kicked for being AFK");
    }

    public static Slider delay = new Slider("Delay", 0.0D, 50.0D, 100.0D, 0);
    public static Checkbox jump = new Checkbox("Jump", true);
    public static Checkbox chat = new Checkbox("Chat", false);

    @Override
    public void setup() {
        addSetting(delay);
        addSetting(jump);
        addSetting(chat);
    }

    Timer afkTimer = new Timer();

    @Override
    public void onUpdate() {
        if (afkTimer.passed((long) delay.getValue(), Timer.Format.Ticks)) {
            if (jump.getValue())
                mc.player.jump();

            if (chat.getValue())
                mc.player.sendChatMessage("!kd");
        }
    }
}
