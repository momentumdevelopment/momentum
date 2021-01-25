package me.linus.momentum.module.modules.combat;

import me.linus.momentum.module.Module;
import me.linus.momentum.module.ModuleManager;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.player.InventoryUtil;
import me.linus.momentum.util.player.PlayerUtil;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import org.lwjgl.input.Mouse;

/**
 * @author reap & linustouchtips
 * @since 12/22/2020
 */

public class Offhand extends Module {
    public Offhand() {
        super("Offhand", Category.COMBAT, "Switches items in the offhand to a totem when low on health");
    }

    public static Mode mode = new Mode("Mode", "Crystal", "Gapple", "Bed", "Chorus", "Totem");
    public static Mode fallbackMode = new Mode("Fallback", "Crystal", "Gapple", "Bed", "Chorus", "Totem");
    public static Slider health = new Slider("Health", 0.1, 16.0, 36.0, 1);

    public static Checkbox checks = new Checkbox("Checks", true);
    public static SubCheckbox caFunction = new SubCheckbox(checks, "AutoCrystal", false);
    public static SubCheckbox elytraCheck = new SubCheckbox(checks, "Elytra", false);
    public static SubCheckbox fallCheck = new SubCheckbox(checks, "Falling", false);

    public static Checkbox swordGap = new Checkbox("Sword Gapple", true);
    public static Checkbox forceGap = new Checkbox("Force Gapple", false);
    public static Checkbox hotbar = new Checkbox("Search Hotbar", false);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(fallbackMode);
        addSetting(health);
        addSetting(checks);
        addSetting(swordGap);
        addSetting(forceGap);
        addSetting(hotbar);
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        Item searching = Items.TOTEM_OF_UNDYING;

        if (mc.player.isElytraFlying() & elytraCheck.getValue())
            return;

        if (mc.player.fallDistance > 5 && fallCheck.getValue())
            return;

        if (!ModuleManager.getModuleByName("AutoCrystal").isEnabled() && caFunction.getValue())
            return;

        switch (mode.getValue()) {
            case 0:
                searching = Items.END_CRYSTAL;
                break;
            case 1:
                searching = Items.GOLDEN_APPLE;
                break;
            case 2:
                searching = Items.BED;
                break;
            case 3:
                searching = Items.CHORUS_FRUIT;
                break;
        }

        if (health.getValue() > PlayerUtil.getHealth())
            searching = Items.TOTEM_OF_UNDYING;

        else if (InventoryUtil.getHeldItem(Items.DIAMOND_SWORD) && swordGap.getValue())
            searching = Items.GOLDEN_APPLE;

        else if (forceGap.getValue() && Mouse.isButtonDown(1))
            searching = Items.GOLDEN_APPLE;

        if (mc.player.getHeldItemOffhand().getItem() == searching)
            return;

        if (mc.currentScreen != null)
            return;

        if (InventoryUtil.getInventoryItemSlot(searching, hotbar.getValue()) != -1) {
            InventoryUtil.moveItemToOffhand(InventoryUtil.getInventoryItemSlot(searching, hotbar.getValue()));
            return;
        }

        switch (fallbackMode.getValue()) {
            case 0:
                searching = Items.END_CRYSTAL;
                break;
            case 1:
                searching = Items.GOLDEN_APPLE;
                break;
            case 2:
                searching = Items.BED;
                break;
            case 3:
                searching = Items.CHORUS_FRUIT;
                break;
            case 4:
                searching = Items.TOTEM_OF_UNDYING;
                break;
        }

        if (mc.player.getHeldItemOffhand().getItem() == searching)
            return;

        if (InventoryUtil.getInventoryItemSlot(searching, hotbar.getValue()) != -1)
            InventoryUtil.moveItemToOffhand(InventoryUtil.getInventoryItemSlot(searching, hotbar.getValue()));
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}
