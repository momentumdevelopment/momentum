package me.linus.momentum.module.modules.misc;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class AntiAFK extends Module {
    public AntiAFK() {
        super("AntiAFK", Category.MISC, "Prevents you from getting kicked for being AFK");
    }

    private static final Checkbox jump = new Checkbox("Jump", true);
    private static final Checkbox chat = new Checkbox("Chat", false);

    @Override
    public void setup() {
        addSetting(jump);
        addSetting(chat);
    }

    @Override
    public void onUpdate() {
        if (mc.player.ticksExisted % 50 == 0) {
            if (jump.getValue())
                mc.player.jump();

            if (chat.getValue())
                mc.player.sendChatMessage("!kd");
        }
    }
}
