package me.linus.momentum.module.modules.combat;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.keybind.Keybind;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.world.InventoryUtil;
import me.linus.momentum.util.world.PlayerUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.network.play.client.CPacketPlayer;
import org.lwjgl.input.Keyboard;

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
    private static Keybind mendKey = new Keybind("Mend Key", -2);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(delay);
        addSetting(footEXP);
        addSetting(mendKey);
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (mc.player.getHeldItemMainhand().getItem() instanceof ItemExpBottle)
            mc.rightClickDelayTimer = (int) delay.getValue();

        if (Keyboard.isKeyDown(mendKey.getKey()) && 0 < PlayerUtil.getArmorDurability() && (mode.getValue() == 0 || mode.getValue() == 1)) {
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