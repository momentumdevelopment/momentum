package me.linus.momentum.module.modules.misc;

import me.linus.momentum.module.Module;
import me.linus.momentum.util.client.Pair;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author linustouchtips
 * @since 11/30/2020
 */

public class Refill extends Module {
    public Refill() {
        super("Refill", Category.MISC, "Refills items in your hotbar");
    }

    private int delayStep = 0;

    private static Map<Integer, ItemStack> getInventory() {
        return getInventorySlots(9, 35);
    }
    private static Map<Integer, ItemStack> getHotbar() {
        return getInventorySlots(36, 44);
    }
    private static Map<Integer, ItemStack> getInventorySlots(int current, int last) {
        Map<Integer, ItemStack> fullInventorySlots = new HashMap<>();

        while (current <= last) {
            fullInventorySlots.put(current, mc.player.inventoryContainer.getInventory().get(current));
            current++;
        }

        return fullInventorySlots;
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (mc.currentScreen instanceof GuiContainer)
            return;

        if (delayStep < 2) {
            delayStep++;
            return;
        } else
            delayStep = 0;


        Pair<Integer, Integer> slots = findReplenishableHotbarSlot();

        if (slots == null) {
            return;
        }

        int inventorySlot = slots.getKey();
        int hotbarSlot = slots.getValue();

        mc.playerController.windowClick(0, inventorySlot, 0, ClickType.PICKUP, mc.player);

        mc.playerController.windowClick(0, hotbarSlot, 0, ClickType.PICKUP, mc.player);

        mc.playerController.windowClick(0, inventorySlot, 0, ClickType.PICKUP, mc.player);
    }

    private Pair<Integer, Integer> findReplenishableHotbarSlot() {

        Pair<Integer, Integer> returnPair = null;

        for (Map.Entry<Integer, ItemStack> hotbarSlot : getHotbar().entrySet()) {

            ItemStack stack = hotbarSlot.getValue();

            if (stack.isEmpty || stack.getItem() == Items.AIR) {
                continue;
            } if (!stack.isStackable()) {
                continue;
            } if (stack.stackSize >= stack.getMaxStackSize()) {
                continue;
            } if (stack.stackSize > 32) {
                continue;
            }

            int inventorySlot = findCompatibleInventorySlot(stack);

            if (inventorySlot == -1) {
                continue;
            }

            returnPair = new Pair<>(inventorySlot, hotbarSlot.getKey());
        }
        return returnPair;
    }

    private int findCompatibleInventorySlot(ItemStack hotbarStack) {

        int inventorySlot = -1;
        int smallestStackSize = 999;

        for (Map.Entry<Integer, ItemStack> entry : getInventory().entrySet()) {

            ItemStack inventoryStack = entry.getValue();

            if (inventoryStack.isEmpty || inventoryStack.getItem() == Items.AIR) {
                continue;
            } if (!isCompatibleStacks(hotbarStack, inventoryStack)) {
                continue;
            }

            int currentStackSize = mc.player.inventoryContainer.getInventory().get(entry.getKey()).stackSize;

            if (smallestStackSize > currentStackSize) {
                smallestStackSize = currentStackSize;
                inventorySlot = entry.getKey();
            }
        } return inventorySlot;
    }

    private boolean isCompatibleStacks(ItemStack stack1, ItemStack stack2) {

        if (!stack1.getItem().equals(stack2.getItem())) {
            return false;
        } if ((stack1.getItem() instanceof ItemBlock) && (stack2.getItem() instanceof ItemBlock)) {
            Block block1 = ((ItemBlock) stack1.getItem()).getBlock();
            Block block2 = ((ItemBlock) stack2.getItem()).getBlock();
            if (!block1.blockMaterial.equals(block2.blockMaterial)) {
                return false;
            }
        } if (!stack1.getDisplayName().equals(stack2.getDisplayName())) {
            return false;
        } if (stack1.getItemDamage() != stack2.getItemDamage()) {
            return false;
        }
        return true;
    }
}

