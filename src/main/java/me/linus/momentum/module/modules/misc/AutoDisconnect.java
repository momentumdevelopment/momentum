package me.linus.momentum.module.modules.misc;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.keybind.Keybind;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.world.InventoryUtil;
import me.linus.momentum.util.world.WorldUtil;
import net.minecraft.init.Items;
import org.lwjgl.input.Keyboard;

/**
 * @author linustouchtips
 * @since 11/30/2020
 */

public class AutoDisconnect extends Module {
    public AutoDisconnect() {
        super("AutoDisconnect", Category.MISC, "Automatically logs you out when you're low on health");
    }

    public static Slider health = new Slider("Health", 0.0D, 7.0D, 36.0D, 0);
    public static Checkbox noTotems = new Checkbox("No Totems", false);
    public static Checkbox visualRange = new Checkbox("Player in Range", false);
    private static Keybind disconnectKey = new Keybind("Disconnect Key", -2);

    @Override
    public void setup() {
        addSetting(health);
        addSetting(noTotems);
        addSetting(visualRange);
        addSetting(disconnectKey);
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (mc.player.getHealth() <= health.getValue())
            WorldUtil.disconnectFromWorld(this);

        if (InventoryUtil.getItemCount(Items.TOTEM_OF_UNDYING) == 0 && noTotems.getValue())
            WorldUtil.disconnectFromWorld(this);

        if (WorldUtil.getNearbyPlayers(20).size() > 0 && visualRange.getValue())
            WorldUtil.disconnectFromWorld(this);

        if (Keyboard.isKeyDown(disconnectKey.getKey()))
            WorldUtil.disconnectFromWorld(this);
    }

    @Override
    public String getHUDData() {
        return String.valueOf(health.getValue());
    }
}
