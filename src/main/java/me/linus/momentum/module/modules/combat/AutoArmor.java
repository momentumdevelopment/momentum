package me.linus.momentum.module.modules.combat;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.world.Timer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

/**
 * @author linustouchtips
 * @since 11/30/2020
 */

public class AutoArmor extends Module {
    public AutoArmor() {
        super("AutoArmor", Category.COMBAT, "Automatically replaces armor");
    }

    private static final Slider delay = new Slider("Delay", 0.0D, 2.0D, 10.0D, 0);
    public static Checkbox curse = new Checkbox("Ignore Curse", true);
    public static Checkbox elytra = new Checkbox("Prefer Elytra", false);

    @Override
    public void setup() {
        addSetting(delay);
        addSetting(curse);
        addSetting(elytra);
    }

    Timer armorTimer = new Timer();

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (armorTimer.passed((long) delay.getValue(), Timer.Format.Ticks))
            return;

        if (mc.currentScreen instanceof GuiContainer && !(mc.currentScreen instanceof InventoryEffectRenderer))
            return;

        int[] bestArmorSlots = new int[4];
        int[] bestArmorValues = new int[4];

        for (int armorType = 0; armorType < 4; armorType++) {
            ItemStack oldArmor = mc.player.inventory.armorItemInSlot(armorType);

            if (oldArmor != null && oldArmor.getItem() instanceof ItemArmor)
                bestArmorValues[armorType] = ((ItemArmor) oldArmor.getItem()).damageReduceAmount;

            bestArmorSlots[armorType] = -1;
        }

        for (int slot = 0; slot < 36; slot++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(slot);

            if (stack.getCount() > 1)
                continue;

            if (stack == null || !(stack.getItem() instanceof ItemArmor))
                continue;

            int armorType = ((ItemArmor) stack.getItem()).armorType.ordinal() - 2;

            if (armorType == 2 && mc.player.inventory.armorItemInSlot(armorType).getItem().equals(Items.ELYTRA) && elytra.getValue())
                continue;

            int armorValue = ((ItemArmor) stack.getItem()).damageReduceAmount;

            if (armorValue > bestArmorValues[armorType]) {
                bestArmorSlots[armorType] = slot;
                bestArmorValues[armorType] = armorValue;
            }
        }

        for (int armorType = 0; armorType < 4; armorType++) {
            int slot = bestArmorSlots[armorType];

            if (slot == -1)
                continue;

            if (mc.player.inventory.armorItemInSlot(armorType) == null || mc.player.inventory.armorItemInSlot(armorType) != ItemStack.EMPTY || mc.player.inventory.getFirstEmptyStack() != -1) {
                if (slot < 9)
                    slot += 36;

                mc.playerController.windowClick(0, 8 - armorType, 0, ClickType.QUICK_MOVE, mc.player);
                mc.playerController.windowClick(0, slot, 0, ClickType.QUICK_MOVE, mc.player);
                break;
            }
        }
    }
}
