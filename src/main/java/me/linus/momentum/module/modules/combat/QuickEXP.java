package me.linus.momentum.module.modules.combat;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.keybind.Keybind;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.combat.EnemyUtil;
import me.linus.momentum.util.player.InventoryUtil;
import net.minecraft.init.Items;
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

    public static Mode mode = new Mode("Mode", "Packet", "AutoMend", "Throw");
    public static Slider delay = new Slider("Throw Delay", 0.0D, 0.0D, 4.0D, 0);
    public static Checkbox stopEXP = new Checkbox("Stop EXP", false);
    public static Checkbox footEXP = new Checkbox("FootEXP", false);
    public static Keybind mendKey = new Keybind("Mend Key", -2);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(delay);
        addSetting(stopEXP);
        addSetting(footEXP);
        addSetting(mendKey);
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (stopEXP.getValue() && EnemyUtil.getArmor(mc.player) == 100) {
            mc.player.stopActiveHand();
            return;
        }

        if (InventoryUtil.getHeldItem(Items.EXPERIENCE_BOTTLE))
            mc.rightClickDelayTimer = (int) delay.getValue();

        if (Keyboard.isKeyDown(mendKey.getKey())) {
            switch (mode.getValue()) {
                case 1:
                    InventoryUtil.switchToSlot(Items.EXPERIENCE_BOTTLE);
                    break;
                case 0:
                    InventoryUtil.switchToSlotGhost(Items.EXPERIENCE_BOTTLE);
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