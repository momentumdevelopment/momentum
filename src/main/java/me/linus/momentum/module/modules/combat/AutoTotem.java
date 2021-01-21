package me.linus.momentum.module.modules.combat;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.player.InventoryUtil;
import me.linus.momentum.util.player.PlayerUtil;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import org.lwjgl.input.Mouse;

/**
 * @author linustouchtips
 * @since 11/28/2020
 */

public class AutoTotem extends Module {
    public AutoTotem() {
        super("AutoTotem", Category.COMBAT, "Automatically replaces totems");
    }

    public static Slider health = new Slider("Health", 0.1, 24.0, 36.0, 1);
    public static Checkbox swordGap = new Checkbox("Sword Gapple", true);
    public static Checkbox forceGap = new Checkbox("Force Gapple", false);
    public static Checkbox hotbar = new Checkbox("Search Hotbar", false);

    @Override
    public void setup() {
        addSetting(health);
        addSetting(swordGap);
        addSetting(forceGap);
        addSetting(hotbar);
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        Item searching = Items.AIR;

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

        if (InventoryUtil.getInventoryItemSlot(searching, hotbar.getValue()) != -1)
            InventoryUtil.moveItemToOffhand(InventoryUtil.getInventoryItemSlot(searching, hotbar.getValue()));
    }

    @Override
    public String getHUDData() {
        return " " + InventoryUtil.getItemCount(Items.TOTEM_OF_UNDYING);
    }
}
