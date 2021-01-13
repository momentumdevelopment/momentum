package me.linus.momentum.module.modules.combat;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.util.client.MessageUtil;
import me.linus.momentum.util.player.InventoryUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

/**
 * @author linustouchtips
 * @since 11/27/2020
 */

public class Quiver extends Module {
    public Quiver() {
        super("Quiver", Category.COMBAT, "Shoots arrows at you");
    }

    public static Mode mode = new Mode("Mode", "Automatic", "Manual");
    public static SubCheckbox speed = new SubCheckbox(mode, "Speed", true);
    public static SubCheckbox strength = new SubCheckbox(mode, "Strength", true);

    public static Checkbox toggle = new Checkbox("Disables", false);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(toggle);
    }

    private int randomVariation;

    public void onUpdate() {
        PotionEffect speedEffect = mc.player.getActivePotionEffect(Potion.getPotionById(1));
        PotionEffect strengthEffect = mc.player.getActivePotionEffect(Potion.getPotionById(5));

        boolean hasSpeed;
        boolean hasStrength;

        hasSpeed = speedEffect != null;

        hasStrength = strengthEffect != null;

        if (InventoryUtil.getHeldItem(Items.BOW))
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(0, -90, true));

        if (mode.getValue() == 0) {
            if (strength.getValue() && !hasStrength) {
                if (mc.player.inventory.getCurrentItem().getItem() == Items.BOW && isArrowInInventory("Arrow of Strength")) {
                    if (mc.player.getItemInUseMaxCount() >= getBowCharge()) {
                        mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
                        mc.player.stopActiveHand();
                        mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                        mc.player.setActiveHand(EnumHand.MAIN_HAND);
                    }

                    else if (mc.player.getItemInUseMaxCount() == 0) {
                        mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                        mc.player.setActiveHand(EnumHand.MAIN_HAND);
                    }
                }
            }

            if (speed.getValue() && !hasSpeed) {
                if (mc.player.inventory.getCurrentItem().getItem() == Items.BOW && isArrowInInventory("Arrow of Speed")) {
                    if (mc.player.getItemInUseMaxCount() >= getBowCharge()) {
                        mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
                        mc.player.stopActiveHand();
                        mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                        mc.player.setActiveHand(EnumHand.MAIN_HAND);
                    }

                    else if (mc.player.getItemInUseMaxCount() == 0) {
                        mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                        mc.player.setActiveHand(EnumHand.MAIN_HAND);
                    }
                }
            }
        }

        if (toggle.getValue())
            this.disable();
    }

    private boolean isArrowInInventory(String type) {
        boolean inInv = false;
        for (int i = 0; i < 36; i++) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if (itemStack.getItem() == Items.TIPPED_ARROW) {
                if (itemStack.getDisplayName().equalsIgnoreCase(type)) {
                    inInv = true;
                    switchArrow(i);
                    break;
                }
            }
        }

        return inInv;
    }

    private void switchArrow(int oldSlot) {
        MessageUtil.sendClientMessage("Switching arrows!");
        int bowSlot = mc.player.inventory.currentItem;
        int placeSlot = bowSlot +1;

        if (placeSlot > 8)
            placeSlot = 1;

        if (placeSlot != oldSlot) {
            if (mc.currentScreen instanceof GuiContainer)
                return;

            mc.playerController.windowClick(0, oldSlot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, placeSlot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, oldSlot, 0, ClickType.PICKUP, mc.player);
        }
    }

    public int getBowCharge() {
        if (randomVariation == 0)
            randomVariation = 1;

        return 1 + randomVariation;
    }

    @Override
    public String getHUDData() {
        return mode.getMode(mode.getValue());
    }
}
