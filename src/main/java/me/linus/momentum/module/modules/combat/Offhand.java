package me.linus.momentum.module.modules.combat;

import me.linus.momentum.module.Module;
import me.linus.momentum.module.ModuleManager;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.world.PlayerUtil;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;

/**
 * @author linustouchtips
 * @since 11/28/2020
 */

public class Offhand extends Module {
    public Offhand() {
        super("Offhand", Category.COMBAT, "Switches items in the offhand to a totem when low on health");
    }

    private static Mode mode = new Mode("Mode", "Gapple", "Crystal", "Bed", "Chorus", "Pearl", "Potion", "Creeper", "Totem");
    private static Mode fallback = new Mode("Fall-Back", "Totem", "Pearl");
    public static Slider health = new Slider("Health", 0.0D, 16.0D, 36.0D, 0);
    private static Checkbox swordGap = new Checkbox("Sword Gapple", true);
    private static Checkbox chorusTrap = new Checkbox("Chorus on Trap", true);
    private static Checkbox caFunction = new Checkbox("Switch Only when Crystalling", false);
    private static Checkbox holeFunction = new Checkbox("Switch Only In Hole", false);
    public static SubSlider holeHealth = new SubSlider(holeFunction, "Hole Health", 0.0D, 10.0D, 36.0D, 0);
    private static Checkbox hotbar = new Checkbox("Search Hotbar", false);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(fallback);
        addSetting(health);
        addSetting(swordGap);
        addSetting(chorusTrap);
        addSetting(caFunction);
        addSetting(holeFunction);
        addSetting(hotbar);
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        int itemToSwitch = getItem();

        if (itemToSwitch < 0)
            return;

        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, itemToSwitch, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, itemToSwitch, 0, ClickType.PICKUP, mc.player);
    }

    private int getItem() {
        Item toSwitch = Items.AIR;
        if (fallback.getValue() == 0)
          toSwitch = Items.TOTEM_OF_UNDYING;
        if (fallback.getValue() == 1)
            toSwitch = Items.ENDER_PEARL;

        if (!PlayerUtil.isInHole() && holeFunction.getValue())
            return findFirstItemSlot(toSwitch);

        if (!ModuleManager.getModuleByName("AutoCrystal").isEnabled() && caFunction.getValue())
            return findFirstItemSlot(toSwitch);

        if (!(PlayerUtil.getHealth() <= health.getValue())) {
            if (mc.player.getHeldItemMainhand().getItem() instanceof ItemSword && swordGap.getValue())
                toSwitch = Items.GOLDEN_APPLE;

            else if (PlayerUtil.isPlayerTrapped() && chorusTrap.getValue())
                toSwitch = Items.CHORUS_FRUIT;

            else {
                switch (mode.getValue()) {
                    case 0:
                        toSwitch = Items.GOLDEN_APPLE;
                        break;
                    case 1:
                        toSwitch = Items.END_CRYSTAL;
                        break;
                    case 2:
                        toSwitch = Items.BED;
                        break;
                    case 3:
                        toSwitch = Items.CHORUS_FRUIT;
                        break;
                    case 4:
                        toSwitch = Items.ENDER_PEARL;
                        break;
                    case 5:
                        toSwitch = Items.POTIONITEM;
                        break;
                    case 6:
                        toSwitch = Items.SPAWN_EGG;
                        break;
                }
            }

        } else if (PlayerUtil.isInHole() && holeFunction.getValue() && !(PlayerUtil.getHealth() <= holeHealth.getValue())) {
            if (mc.player.getHeldItemMainhand().getItem() instanceof ItemSword && swordGap.getValue())
                toSwitch = Items.GOLDEN_APPLE;

            else if (PlayerUtil.isPlayerTrapped() && chorusTrap.getValue())
                toSwitch = Items.CHORUS_FRUIT;

            else if (!PlayerUtil.isInHole() && holeFunction.getValue())
                return findFirstItemSlot(toSwitch);

            else {
                switch (mode.getValue()) {
                    case 0:
                        toSwitch = Items.GOLDEN_APPLE;
                        break;
                    case 1:
                        toSwitch = Items.END_CRYSTAL;
                        break;
                    case 2:
                        toSwitch = Items.BED;
                        break;
                    case 3:
                        toSwitch = Items.CHORUS_FRUIT;
                        break;
                    case 4:
                        toSwitch = Items.ENDER_PEARL;
                        break;
                    case 5:
                        toSwitch = Items.POTIONITEM;
                        break;
                    case 6:
                        toSwitch = Items.SPAWN_EGG;
                        break;
                }
            }
        }

        if (mc.player.getHeldItemOffhand().getItem() == toSwitch)
            return -2;

        return findFirstItemSlot(toSwitch);
    }

    private int findFirstItemSlot(Item item) {
        if (hotbar.getValue()) {
            for (int i = 0; i < 45; i++)
                if (mc.player.inventory.getStackInSlot(i).getItem() == item)
                    return i < 9 ? i + 36 : i;
        } else {
            for (int i = 9; i < 45; i++)
                if (mc.player.inventory.getStackInSlot(i).getItem() == item)
                    return i < 9 ? i + 36 : i;
        }

        if (hotbar.getValue()) {
            for (int i = 0; i < 45; i++)
                if (mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING)
                    return i < 9 ? i + 36 : i;
        } else {
            for (int i = 9; i < 45; i++)
                if (mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING)
                    return i < 9 ? i + 36 : i;
        }

        return -1;
    }

    @Override
    public String getHUDData() {
        return mode.getMode(mode.getValue());
    }
}
