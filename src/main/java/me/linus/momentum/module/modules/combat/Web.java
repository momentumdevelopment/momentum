package me.linus.momentum.module.modules.combat;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.util.client.MessageUtil;
import me.linus.momentum.util.player.InventoryUtil;
import me.linus.momentum.util.player.PlayerUtil;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

/**
 * @author linustouchtips
 * @since 12/19/2020
 */

// TODO: make an aura mode for this
public class Web extends Module {
    public Web() {
        super("Web", Category.COMBAT, "Places webs at your feet");
    }

    public static Mode mode = new Mode("Mode", "Self", "Aura");
    public static Checkbox autoSwitch = new Checkbox("AutoSwitch", true);
    public static Checkbox rotate = new Checkbox("Rotate", true);
    public static Checkbox disable = new Checkbox("Disables", true);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(autoSwitch);
        addSetting(rotate);
        addSetting(disable);
    }

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        MessageUtil.sendClientMessage("Placing Web!");
    }

    @Override
    public void onUpdate() {
        if (autoSwitch.getValue())
            InventoryUtil.switchToSlot(InventoryUtil.getBlockInHotbar(Blocks.WEB));

        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(new BlockPos(PlayerUtil.getCenter(mc.player.posX, mc.player.posY, mc.player.posZ)), EnumFacing.UP, EnumHand.MAIN_HAND, 0, 0, 0));

        if (disable.getValue())
            this.disable();
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode(mode.getValue());
    }
}
