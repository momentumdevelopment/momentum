package me.linus.momentum.module.modules.misc;

import com.mojang.authlib.GameProfile;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.util.client.MessageUtil;
import me.linus.momentum.util.world.WorldUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;

import java.util.UUID;

/**
 * @author linustouchtips
 * @since 11/26/2020
 */

public class FakePlayer extends Module {
    public FakePlayer() {
        super("FakePlayer", Category.MISC, "Creates a fake motionless player");
    }

    public static Mode name = new Mode("Name", "GrandOlive", "popbob", "Fit", "S8N", "Papa_Quill", "linustouchtips24");
    public static Checkbox inventory = new Checkbox("Copy Inventory", true);
    public static Checkbox angles = new Checkbox("Copy Angles", true);

    @Override
    public void setup() {
        addSetting(name);
        addSetting(inventory);
        addSetting(angles);
    }

    String fakeName = "GrandOlive";

    public void onEnable() {
        if (nullCheck())
            return;

        WorldUtil.createFakePlayer(false, fakeName, inventory.getValue(), angles.getValue(), true);
        MessageUtil.sendClientMessage("Spawning fake player!");
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        switch (name.getValue()) {
            case 1:
                break;
            case 2:
                fakeName = "popbob";
                break;
            case 3:
                fakeName = "Fit";
                break;
            case 4:
                fakeName = "S8N";
                break;
            case 5:
                fakeName = "Papa_Quill";
                break;
            case 0:
                fakeName = "linustouchtips24";
                break;
        }
    }

    @Override
    public void onDisable() {
        mc.world.removeEntityFromWorld(69420);
    }
}