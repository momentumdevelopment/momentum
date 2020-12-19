package me.linus.momentum.module.modules.combat;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
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

    public static Checkbox elytra = new Checkbox("Prefer Elytra", false);

    @Override
    public void setup() {
        addSetting(elytra);
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (mc.player.ticksExisted % 2 == 0)
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

            ItemArmor armor = (ItemArmor) stack.getItem();
            int armorType = armor.armorType.ordinal() - 2;

            if (armorType == 2 && mc.player.inventory.armorItemInSlot(armorType).getItem().equals(Items.ELYTRA) && elytra.getValue())
                continue;

            int armorValue = armor.damageReduceAmount;

            if (armorValue > bestArmorValues[armorType]) {
                bestArmorSlots[armorType] = slot;
                bestArmorValues[armorType] = armorValue;
            }
        }

        for (int armorType = 0; armorType < 4; armorType++) {
            int slot = bestArmorSlots[armorType];

            if (slot == -1)
                continue;

            ItemStack oldArmor = mc.player.inventory.armorItemInSlot(armorType);
            if (oldArmor == null || oldArmor != ItemStack.EMPTY || mc.player.inventory.getFirstEmptyStack() != -1) {
                if (slot < 9)
                    slot += 36;

                mc.playerController.windowClick(0, 8 - armorType, 0, ClickType.QUICK_MOVE, mc.player);
                mc.playerController.windowClick(0, slot, 0, ClickType.QUICK_MOVE, mc.player);
                break;
            }
        }
    }
}
