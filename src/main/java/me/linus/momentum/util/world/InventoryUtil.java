package me.linus.momentum.util.world;

import me.linus.momentum.mixin.MixinInterface;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.client.CPacketHeldItemChange;

/**
 * @author max & linustouchtips
 * @since 11/26/2020
 */

public class InventoryUtil implements MixinInterface {

    public static int getHotbarItemSlot(Item item) {
        int slot = 0;
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == item) {
                slot = i;
                break;
            }
        }

        return slot;
    }

    public static void switchToSlot(int slot){
        mc.player.inventory.currentItem = slot;
    }

    public static void switchToSlot(Item item) {
        mc.player.inventory.currentItem = getHotbarItemSlot(item);
    }

    public static void switchToSlotGhost(int slot) {
        mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
    }

    public static void switchToSlotGhost(Item item) {
        switchToSlotGhost(getHotbarItemSlot(item));
    }

    public static int getItemCount(Item item) {
        int count = 0;
        count = mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == item).mapToInt(ItemStack::getCount).sum();
        return count;
    }

    public static int getInventoryItemSlot(Item item) {
        int slot = -1;
        for (int i = 0; i < 45; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == item) {
                slot = i;
                break;
            }
        }

        return slot;
    }

    public static int getBlockInHotbar(Block block) {
        for (int i = 0; i < 9; i++) {
            Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (item instanceof ItemBlock && ((ItemBlock) item).getBlock().equals(block))
                return i;
        }

        return -1;
    }

    public static void moveItemToOffhand(int slot) {
        boolean moving = false;
        boolean returning = false;
        int returnSlot = -1;

        if (slot == -1)
            return;

        mc.playerController.windowClick(0, slot < 9 ? slot + 36 : slot, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);

        for (int i = 0; i < 45; i++) {
            if (mc.player.inventory.getStackInSlot(i).isEmpty()) {
                returnSlot = i;
                break;
            }
        }

        if (returnSlot != -1) {
            mc.playerController.windowClick(0, returnSlot < 9 ? returnSlot + 36 : returnSlot, 0, ClickType.PICKUP, mc.player);
        }
    }

    public static int getAnyBlockInHotbar() {
        for (int i = 0; i < 9; i++) {
            Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (item instanceof ItemBlock)
                return i;
        }

        return -1;
    }


    public static void moveItemToOffhand(Item item) {
        int slot = getInventoryItemSlot(item);
        if (slot != -1) {
            moveItemToOffhand(slot);
        }
    }

    public static boolean Is32k(ItemStack stack) {
        if (stack.getEnchantmentTagList() != null) {
            final NBTTagList tags = stack.getEnchantmentTagList();
            for (int i = 0; i < tags.tagCount(); i++) {
                final NBTTagCompound tagCompound = tags.getCompoundTagAt(i);
                if (tagCompound != null && Enchantment.getEnchantmentByID(tagCompound.getByte("id")) != null) {
                    final Enchantment enchantment = Enchantment.getEnchantmentByID(tagCompound.getShort("id"));
                    final short lvl = tagCompound.getShort("lvl");
                    if (enchantment != null) {
                        if (enchantment.isCurse())
                            continue;

                        if (lvl >= 1000)
                            return true;
                    }
                }
            }
        }

        return false;
    }
}
