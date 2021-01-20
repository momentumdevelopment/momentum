package me.linus.momentum.module.modules.misc;

import me.linus.momentum.module.Module;
import me.linus.momentum.util.client.Pair;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author linustouchtips & dnger
 * @since 11/30/2020
 */

// TODO: rewrite this
public class Refill extends Module {
    public Refill() {
        super("Refill", Category.MISC, "Refills items in your hotbar");
    }

    int delayStep = 0;

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (mc.currentScreen instanceof GuiContainer)
            return;

        if (delayStep < 2) {
            delayStep++;
            return;
        }

        else
            delayStep = 0;

        Pair<Integer, Integer> slots = findReplenishableHotbarSlot();

        if (slots == null)
            return;

        mc.playerController.windowClick(0, slots.getKey(), 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(0, slots.getValue(), 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(0, slots.getKey(), 0, ClickType.PICKUP, mc.player);
    }

    Pair<Integer, Integer> findReplenishableHotbarSlot() {
        Pair<Integer, Integer> returnPair = null;

        for (Map.Entry<Integer, ItemStack> hotbarSlot : getHotbar().entrySet()) {
            if (hotbarSlot.getValue().isEmpty || hotbarSlot.getValue().getItem() == Items.AIR)
                continue;

            if (!hotbarSlot.getValue().isStackable())
                continue;

            if (hotbarSlot.getValue().stackSize >= hotbarSlot.getValue().getMaxStackSize())
                continue;

            if (hotbarSlot.getValue().stackSize > 32)
                continue;

            int inventorySlot = findCompatibleInventorySlot(hotbarSlot.getValue());

            if (inventorySlot == -1)
                continue;

            returnPair = new Pair<>(inventorySlot, hotbarSlot.getKey());
        }

        return returnPair;
    }

    int findCompatibleInventorySlot(ItemStack hotbarStack) {
        int inventorySlot = -1;
        int smallestStackSize = 999;

        for (Map.Entry<Integer, ItemStack> entry : getInventory().entrySet()) {
            if (entry.getValue().isEmpty || entry.getValue().getItem() == Items.AIR)
                continue;

            if (!isCompatibleStacks(hotbarStack, entry.getValue()))
                continue;

            int currentStackSize = mc.player.inventoryContainer.getInventory().get(entry.getKey()).stackSize;

            if (smallestStackSize > currentStackSize) {
                smallestStackSize = currentStackSize;
                inventorySlot = entry.getKey();
            }
        }

        return inventorySlot;
    }

    boolean isCompatibleStacks(ItemStack stack1, ItemStack stack2) {
        if (!stack1.getItem().equals(stack2.getItem()))
            return false;

        if ((stack1.getItem() instanceof ItemBlock) && (stack2.getItem() instanceof ItemBlock)) {
            if (!((ItemBlock) stack1.getItem()).getBlock().blockMaterial.equals(((ItemBlock) stack2.getItem()).getBlock().blockMaterial))
                return false;
        }

        if (!stack1.getDisplayName().equals(stack2.getDisplayName()))
            return false;

        return stack1.getItemDamage() == stack2.getItemDamage();
    }

    static Map<Integer, ItemStack> getInventory() {
        return getInventorySlots(9, 35);
    }

    static Map<Integer, ItemStack> getHotbar() {
        return getInventorySlots(36, 44);
    }

    static Map<Integer, ItemStack> getInventorySlots(int current, int last) {
        Map<Integer, ItemStack> fullInventorySlots = new HashMap<>();

        while (current <= last) {
            fullInventorySlots.put(current, mc.player.inventoryContainer.getInventory().get(current));
            current++;
        }

        return fullInventorySlots;
    }
}

