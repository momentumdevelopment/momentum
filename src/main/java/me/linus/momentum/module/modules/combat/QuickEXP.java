package me.linus.momentum.module.modules.combat;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.world.InventoryUtil;
import me.linus.momentum.util.world.PlayerUtil;
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

    private static final Mode mode = new Mode("Mode", "Packet", "AutoMend", "Throw");
    public static Slider delay = new Slider("Throw Delay", 0.0D, 0.0D, 4.0D, 0);
    private static final Checkbox footEXP = new Checkbox("FootEXP", true);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(delay);
        addSetting(footEXP);
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        Item itemMainHand = mc.player.getHeldItemMainhand().getItem();
        Item itemONotMainHand = mc.player.getHeldItemOffhand().getItem();
        boolean expInMainHand = itemMainHand instanceof ItemExpBottle;
        boolean expNotInMainHand = itemONotMainHand instanceof ItemExpBottle;

        if (expInMainHand || expNotInMainHand)
            mc.rightClickDelayTimer = (int) delay.getValue();

        if (mc.player.isSneaking() && 0 < PlayerUtil.getArmorDurability() && (mode.getValue() == 0 || mode.getValue() == 1)) {
            switch (mode.getValue()) {
                case 1:
                    mc.player.inventory.currentItem = InventoryUtil.getHotbarItemSlot(Items.EXPERIENCE_BOTTLE);
                    break;
                case 0:
                    InventoryUtil.switchToSlotGhost(InventoryUtil.getHotbarItemSlot(Items.EXPERIENCE_BOTTLE));
                    break;
            }

            if (footEXP.getValue())
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(0, 90, true));

            mc.rightClickMouse();
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}