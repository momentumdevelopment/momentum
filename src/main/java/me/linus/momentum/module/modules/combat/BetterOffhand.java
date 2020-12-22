package me.linus.momentum.module.modules.combat;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.world.InventoryUtil;
import me.linus.momentum.util.world.PlayerUtil;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import org.lwjgl.input.Mouse;

public class BetterOffhand extends Module {
    public static final Mode mode = new Mode("Mode", "Crystal", "Gapple", "Bed", "Chorus", "Totem");
    public static final Mode fallbackMode = new Mode("Fallback", "Crystal", "Gapple", "Bed", "Chorus", "Totem");
    public static final Slider hp = new Slider("MinHP", 0.1, 16.0, 36.0, 1);
    public static final Checkbox forceGap = new Checkbox("ForceGap", true);

    public BetterOffhand() {
        super("BetterOffhand", Category.COMBAT, "Old offhand is bad lol");

        addSetting(mode);
        addSetting(fallbackMode);
        addSetting(hp);
        addSetting(forceGap);
    }

    @Override
    public void onUpdate() {
        if (nullCheck()) return;

        Item searching = Items.TOTEM_OF_UNDYING;

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
        if (hp.getValue() > PlayerUtil.getHealth()) searching = Items.TOTEM_OF_UNDYING;
        if (forceGap.getValue() && Mouse.isButtonDown(1)) searching = Items.GOLDEN_APPLE;

        if (mc.player.getHeldItemOffhand().getItem() == searching) return;

        int slot = InventoryUtil.getInventoryItemSlot(searching);
        if (slot != -1) {
            InventoryUtil.moveItemToOffhand(slot);

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
        if (mc.player.getHeldItemOffhand().getItem() == searching) return;

        slot = InventoryUtil.getInventoryItemSlot(searching);
        if (slot != -1) InventoryUtil.moveItemToOffhand(slot);
    }
}
