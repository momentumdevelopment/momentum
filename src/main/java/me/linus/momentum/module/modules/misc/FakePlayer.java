package me.linus.momentum.module.modules.misc;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.util.client.MessageUtil;
import me.linus.momentum.util.world.WorldUtil;

/**
 * @author linustouchtips
 * @since 11/26/2020
 */

public class FakePlayer extends Module {
    public FakePlayer() {
        super("FakePlayer", Category.MISC, "Creates a fake motionless player");
    }

    public static Mode mode = new Mode("Mode", "Single", "Multi");
    public static Mode name = new Mode("Name", "linustouchtips24", "popbob", "Fit", "GrandOlive", "S8N", "Papa_Quill");
    public static Checkbox inventory = new Checkbox("Copy Inventory", true);
    public static Checkbox angles = new Checkbox("Copy Angles", true);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(name);
        addSetting(inventory);
        addSetting(angles);
    }

    public void onEnable() {
        if (nullCheck())
            return;

        switch (mode.getValue()) {
            case 0:
                WorldUtil.createFakePlayer(name.getMode(name.getValue()), inventory.getValue(), angles.getValue(), true, false, mc.player.getPosition());
                break;
            case 1:
                WorldUtil.createFakePlayer(name.getMode(name.getValue()), inventory.getValue(), angles.getValue(), true, false, mc.player.getPosition().north());
                WorldUtil.createFakePlayer(name.getMode(name.getValue()), inventory.getValue(), angles.getValue(), true, false, mc.player.getPosition().east());
                WorldUtil.createFakePlayer(name.getMode(name.getValue()), inventory.getValue(), angles.getValue(), true, false, mc.player.getPosition().west());
                WorldUtil.createFakePlayer(name.getMode(name.getValue()), inventory.getValue(), angles.getValue(), true, false, mc.player.getPosition().south());
                break;
        }
        
        MessageUtil.sendClientMessage("Spawning fake player(s)!");
    }

    @Override
    public void onDisable() {
        mc.world.removeEntityFromWorld(69420);
    }
}