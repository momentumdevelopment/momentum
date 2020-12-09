package me.linus.momentum.module.modules.combat;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.world.PlayerUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * @author linustouchtips
 * @since 11/28/2020
 */

public class AutoTotem extends Module {
    public AutoTotem() {
        super("AutoTotem", Category.COMBAT, "Automatically replaces totems");
    }

    public static Slider health = new Slider("Health", 0.0D, 20.0D, 36.0D, 0);
    private static Checkbox soft = new Checkbox("Soft", true);

    @Override
    public void setup() {
        addSetting(health);
        addSetting(soft);
    }

    int totems;
    boolean moving = false;
    boolean returnI = false;

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        Item item = getItem();

        if (mc.currentScreen instanceof GuiContainer)
            return;

        if (returnI) {
            int t = -1;
            for (int i = 0; i < 45; i++)
                if (mc.player.inventory.getStackInSlot(i).isEmpty()){
                    t = i;
                    break;
                }

            if (t == -1)
                return;

            mc.playerController.windowClick(0, t < 9 ? t + 36 : t, 0, ClickType.PICKUP, mc.player);
            returnI = false;
        }

        totems = mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == item).mapToInt(ItemStack::getCount).sum();
        if (mc.player.getHeldItemOffhand().getItem() == item)
            totems++;

        else {
            if (soft.getValue() && !mc.player.getHeldItemOffhand().isEmpty())
                return;

            if (moving) {
                mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
                moving = false;

                if (!mc.player.inventory.getItemStack().isEmpty())
                    returnI = true;

                return;
            }

            if (mc.player.inventory.getItemStack().isEmpty()){
                if (totems == 0) return;
                int t = -1;
                for (int i = 0; i < 45; i++)
                    if (mc.player.inventory.getStackInSlot(i).getItem() == item) {
                        t = i;
                        break;
                    }

                if (t == -1)
                    return;

                    mc.playerController.windowClick(0, t < 9 ? t + 36 : t, 0, ClickType.PICKUP, mc.player);
                    moving = true;

            } else if (!soft.getValue()) {
                int t = -1;
                for (int i = 0; i < 45; i++)
                    if (mc.player.inventory.getStackInSlot(i).isEmpty()) {
                        t = i;
                        break;
                    } if (t == -1)
                        return;

                    mc.playerController.windowClick(0, t < 9 ? t + 36 : t, 0, ClickType.PICKUP, mc.player);
            }
        }
    }

    public Item getItem() {
        if (PlayerUtil.getHealth() <= health.getValue())
            return Items.TOTEM_OF_UNDYING;

        else return Items.AIR;
    }

    @Override
    public String getHUDData() {
        return String.valueOf(totems);
    }
}
