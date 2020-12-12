package me.linus.momentum.module.modules.combat;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.util.client.MessageUtil;
import me.linus.momentum.util.world.BlockUtils;
import me.linus.momentum.util.world.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

/**
 * @author yoink & linustouchtips
 * @since 11/29/2020
 */

public class Burrow extends Module {
    public Burrow() {
        super("Burrow", Category.COMBAT, "Rubberbands you into a block");
    }

    private static Mode mode = new Mode("Mode", "Rubberband", "Teleport");
    private static Checkbox rotate = new Checkbox("Rotate", true);
    private static Checkbox center = new Checkbox("Center", true);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(rotate);
        addSetting(center);
    }

    BlockPos originalPos;

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        originalPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);

        if (BlockUtils.isCollidedBlocks(originalPos)) {
            this.disable();
        }

        MessageUtil.sendClientMessage("Attempting to trigger a rubberband!");
        mc.player.jump();
    }

    @Override
    public void onUpdate() {
        if (mc.player.posY > originalPos.getY() + 1.2) {
            final int oldSlot = mc.player.inventory.currentItem;
            mc.player.inventory.currentItem = PlayerUtil.getHotbarSlot(getBlock());

            PlayerUtil.placeBlock(originalPos, rotate.getValue());
            mc.player.inventory.currentItem = oldSlot;

            if (mode.getValue() == 0)
                mc.player.jump();
            else
                mc.player.setPosition(0, -1, 0);

            this.disable();
        }
    }

    public Block getBlock() {
        if (mode.getValue() == 0)
            return Blocks.OBSIDIAN;
        else
            return Blocks.WEB;
    }

    @Override
    public String getHUDData() {
        return mode.getMode(mode.getValue());
    }
}
