package me.linus.momentum.module.modules.player;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import net.minecraft.block.Block;
import net.minecraft.item.ItemEndCrystal;

/**
 * @author linustouchtips
 * @since 12/03/2020
 */

public class FastPlace extends Module {
    public FastPlace() {
        super("FastPlace", Category.PLAYER, "Allows you to place blocks & crystals faster");
    }

    private static final Checkbox blocks = new Checkbox("Blocks", false);
    private static final Checkbox crystal = new Checkbox("Crystals", true);

    @Override
    public void setup() {
        addSetting(blocks);
        addSetting(crystal);
    }

    @Override
    public void onUpdate() {
        if (mc.player.getHeldItemMainhand().getItem() instanceof ItemEndCrystal && crystal.getValue()) {
            mc.rightClickDelayTimer = 0;
        }

        if (Block.getBlockFromItem(mc.player.getHeldItemMainhand().getItem()).getDefaultState().isFullBlock() && blocks.getValue()) {
            mc.rightClickDelayTimer = 0;
        }
    }
}
