package me.linus.momentum.module.modules.movement;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.util.client.MessageUtil;
import net.minecraft.util.text.TextFormatting;

/**
 * @author linustouchtips
 * @since 12/03/2020
 */

public class AntiVoid extends Module {
    public AntiVoid() {
        super("AntiVoid", Category.MOVEMENT, "Pulls you out of the void");
    }

    public static Mode mode = new Mode("Mode", "Float", "Freeze", "SlowFall", "Teleport");

    @Override
    public void setup() {
        addSetting(mode);
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (mc.player.posY <= 0) {
            MessageUtil.sendClientMessage(TextFormatting.RED + "Attempting to get you out of the void!");

            switch (mode.getValue()) {
                case 0:
                    mc.player.motionY = 0.5;
                    break;
                case 1:
                    mc.player.motionY = 0;
                    break;
                case 2:
                    mc.player.motionY /= 4;
                    break;
                case 3:
                    mc.player.setPosition(mc.player.posX, mc.player.posY + 2, mc.player.posZ);
                    break;
            }
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}
