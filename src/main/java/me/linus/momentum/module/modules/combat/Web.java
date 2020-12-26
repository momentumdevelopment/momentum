package me.linus.momentum.module.modules.combat;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.util.client.external.MessageUtil;
import me.linus.momentum.util.world.PlayerUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

/**
 * @author linustouchtips
 * @since 12/19/2020
 */

// TODO: make an auto mode for this
public class Web extends Module {
    public Web() {
        super("Web", Category.COMBAT, "Places webs at your feet");
    }

    private static final Mode mode = new Mode("Mode", "Self", "Aura");
    private static final Checkbox autoSwitch = new Checkbox("AutoSwitch", true);
    private static final Checkbox rotate = new Checkbox("Rotate", true);
    private static final Checkbox disable = new Checkbox("Disables", true);

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
            mc.player.inventory.currentItem = PlayerUtil.getHotbarSlot(Blocks.WEB);

        PlayerUtil.placeBlock(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ), rotate.getValue());

        if (disable.getValue())
            this.disable();
    }
}
