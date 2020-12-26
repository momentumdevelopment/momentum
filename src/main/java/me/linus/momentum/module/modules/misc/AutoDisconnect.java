package me.linus.momentum.module.modules.misc;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.keybind.Keybind;
import me.linus.momentum.setting.slider.Slider;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
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

        int totems = mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();

        if (mc.player.getHealth() <= health.getValue())
            disconnectFromWorld();

        if (totems == 0 && noTotems.getValue())
            disconnectFromWorld();

        if (mc.world.playerEntities.size() != 0 && visualRange.getValue())
            disconnectFromWorld();

        if (Keyboard.isKeyDown(disconnectKey.getKey()))
            disconnectFromWorld();
    }

    public void disconnectFromWorld() {
        this.disable();
        mc.world.sendQuittingDisconnectingPacket();
        mc.loadWorld(null);
        mc.displayGuiScreen(new GuiMainMenu());
    }

    @Override
    public String getHUDData() {
        return String.valueOf(health.getValue());
    }
}
