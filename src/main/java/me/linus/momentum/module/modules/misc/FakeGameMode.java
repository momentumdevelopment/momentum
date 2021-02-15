package me.linus.momentum.module.modules.misc;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.util.client.MessageUtil;
import net.minecraft.world.GameType;

/**
 * @author linustouchtips
 * @since 11/29/2020
 */

public class FakeGameMode extends Module {
    public FakeGameMode() {
        super("FakeGameMode", Category.MISC, "Changes gamemode to creative client-side");
    }

    public static Mode mode = new Mode("Mode", "Creative", "Spectator", "Adventure", "Survival");
    public static Checkbox noHandshake = new Checkbox("No Handshake", true);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(noHandshake);
    }

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        MessageUtil.sendClientMessage("Changing gamemode to " + mode.getMode(mode.getValue()) + "!");
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        switch (mode.getValue()) {
            case 0:
                mc.playerController.setGameType(GameType.CREATIVE);
                break;
            case 1:
                mc.playerController.setGameType(GameType.SPECTATOR);
                break;
            case 2:
                mc.playerController.setGameType(GameType.ADVENTURE);
                break;
            case 3:
                mc.player.setGameType(GameType.SURVIVAL);
                break;
        }
    }

    @Override
    public void onDisable() {
        mc.playerController.setGameType(GameType.SURVIVAL);
    }
}
