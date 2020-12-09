package me.linus.momentum.module.modules.combat;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.world.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;

/**
 * @author linustouchtips
 * @since 11/26/2020
 */

public class QuickEXP extends Module {
    public QuickEXP() {
        super("QuickEXP", Category.COMBAT, "Throws EXP much faster");
    }

    private static Mode mode = new Mode("Mode", "Packet", "AutoMend", "Throw");
    public static Slider delay = new Slider("Throw Delay", 0.0D, 0.0D, 4.0D, 0);
    private static Checkbox footEXP = new Checkbox("FootEXP", true);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(delay);
        addSetting(footEXP);
    }

    @Override
    public void onUpdate() {
        Item itemMainHand = mc.player.getHeldItemMainhand().getItem();
        Item itemONotMainHand = mc.player.getHeldItemOffhand().getItem();
        boolean expInMainHand = itemMainHand instanceof ItemExpBottle;
        boolean expNotInMainHand = itemONotMainHand instanceof ItemExpBottle;
        int armorDurability = getArmorDurability();

        if (expInMainHand || expNotInMainHand)
            mc.rightClickDelayTimer = (int) delay.getValue();

        if (mode.getValue() == 0) {
            if (mc.player.isSneaking() && 0 < armorDurability) {
                if (mode.getValue() == 1)
                    mc.player.inventory.currentItem = findEXPInHotbar();

                if (mode.getValue() == 0)
                    InventoryUtil.switchToSlotGhost(findEXPInHotbar());

                if (footEXP.getValue())
                    mc.player.connection.sendPacket(new CPacketPlayer.Rotation(0, 90, true));

                mc.rightClickMouse();
            }
        }
    }

    private int findEXPInHotbar() {
        int slot = 0;
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.EXPERIENCE_BOTTLE) {
                slot = i;
                break;
            }
        }

        return slot;
    }

    private int getArmorDurability() {
        int TotalDurability = 0;

        for (ItemStack itemStack : mc.player.inventory.armorInventory) {
            TotalDurability = TotalDurability + itemStack.getItemDamage();
        }
        return TotalDurability;
    }

    @Override
    public String getHUDData() {
        return mode.getMode(mode.getValue());
    }
}