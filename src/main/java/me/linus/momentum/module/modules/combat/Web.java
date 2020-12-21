package me.linus.momentum.module.modules.combat;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.util.client.MessageUtil;
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

    private static final Checkbox rotate = new Checkbox("Rotate", true);
    private static final Checkbox center = new Checkbox("Center", true);

    @Override
    public void setup() {
        addSetting(rotate);
        addSetting(center);
    }

    BlockPos originalPos;

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        originalPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
        MessageUtil.sendClientMessage("Placing Web!");
    }

    @Override
    public void onUpdate() {
        final int oldSlot = mc.player.inventory.currentItem;
        mc.player.inventory.currentItem = PlayerUtil.getHotbarSlot(Blocks.OBSIDIAN);

        PlayerUtil.placeBlock(originalPos, rotate.getValue());
        mc.player.inventory.currentItem = oldSlot;

        this.disable();
    }
}
