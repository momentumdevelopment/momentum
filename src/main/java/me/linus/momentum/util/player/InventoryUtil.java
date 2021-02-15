package me.linus.momentum.util.player;

import me.linus.momentum.mixin.MixinInterface;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.client.CPacketHeldItemChange;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author max & linustouchtips & reap
 * @since 11/26/2020
 */

public class InventoryUtil implements MixinInterface {

    public static boolean getHeldItem(Item item) {
        return mc.player.getHeldItemMainhand().getItem().equals(item) || mc.player.getHeldItemOffhand().getItem().equals(item);
    }

    public static void switchToSlot(int slot) {
        if (slot != -1 && mc.player.inventory.currentItem != slot)
            mc.player.inventory.currentItem = slot;
    }

    public static void switchToSlot(Block block) {
        if (getBlockInHotbar(block) != -1 && mc.player.inventory.currentItem != getBlockInHotbar(block))
            mc.player.inventory.currentItem = getBlockInHotbar(block);
    }

    public static void switchToSlot(Item item) {
        if (getHotbarItemSlot(item) != -1 && mc.player.inventory.currentItem != getHotbarItemSlot(item))
            mc.player.inventory.currentItem = getHotbarItemSlot(item);
    }

    public static void switchToSlotGhost(int slot) {
        if (slot != -1 && mc.player.inventory.currentItem != slot)
            mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
    }

    public static void switchToSlotGhost(Block block) {
        if (getBlockInHotbar(block) != -1 && mc.player.inventory.currentItem != getBlockInHotbar(block))
            mc.player.connection.sendPacket(new CPacketHeldItemChange(getBlockInHotbar(block)));
    }

    public static void switchToSlotGhost(Item item) {
        if (getHotbarItemSlot(item) != -1 && mc.player.inventory.currentItem != getHotbarItemSlot(item))
            switchToSlotGhost(getHotbarItemSlot(item));
    }

    public static void moveItemToOffhand(int slot) {
        int returnSlot = -1;

        if (slot == -1)
            return;

        mc.playerController.windowClick(0, slot < 9 ? slot + 36 : slot, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);

        for (int i = 9; i < 45; i++) {
            if (mc.player.inventory.getStackInSlot(i).isEmpty()) {
                returnSlot = i;
                break;
            }
        }

        if (returnSlot != -1)
            mc.playerController.windowClick(0, returnSlot < 9 ? returnSlot + 36 : returnSlot, 0, ClickType.PICKUP, mc.player);
    }

    public static int getHotbarItemSlot(Item item) {
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == item)
                return i;
        }

        return -1;
    }

    public static int getInventoryItemSlot(Item item, boolean hotbar) {
        for (int i = 0; hotbar ? i < 36 : i < 45; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == item)
                return i;
        }

        return -1;
    }

    public static int getBlockInHotbar(Block block) {
        for (int i = 0; i < 9; i++) {
            Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (item instanceof ItemBlock && ((ItemBlock) item).getBlock().equals(block))
                return i;
        }

        return -1;
    }

    public static int getAnyBlockInHotbar() {
        for (int i = 0; i < 9; i++) {
            Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (item instanceof ItemBlock)
                return i;
        }

        return -1;
    }

    public static int getItemCount(Item item) {
        return mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == item).mapToInt(ItemStack::getCount).sum();
    }

    public static int getItemCount(EntityPlayer entityPlayer, Item item) {
        return entityPlayer.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == item).mapToInt(ItemStack::getCount).sum();
    }

    public static boolean Is32k(ItemStack stack) {
        if (stack.getEnchantmentTagList() != null) {
            for (int i = 0; i < stack.getEnchantmentTagList().tagCount(); i++) {
                if (stack.getEnchantmentTagList().getCompoundTagAt(i)!= null && Enchantment.getEnchantmentByID(stack.getEnchantmentTagList().getCompoundTagAt(i).getByte("id")) != null) {
                    if (Enchantment.getEnchantmentByID(stack.getEnchantmentTagList().getCompoundTagAt(i).getShort("id")) != null) {
                        if (Enchantment.getEnchantmentByID(stack.getEnchantmentTagList().getCompoundTagAt(i).getShort("id")).isCurse())
                            continue;

                        if (stack.getEnchantmentTagList().getCompoundTagAt(i).getShort("lvl") >= 1000)
                            return true;
                    }
                }
            }
        }

        return false;
    }
}