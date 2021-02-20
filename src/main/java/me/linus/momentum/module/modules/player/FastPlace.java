package me.linus.momentum.module.modules.player;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.util.player.InventoryUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Items;

/**
 * @author linustouchtips
 * @since 12/03/2020
 */

public class FastPlace extends Module {
    public FastPlace() {
        super("FastPlace", Category.PLAYER, "Allows you to place blocks & crystals faster");
    }

    public static Checkbox blocks = new Checkbox("Blocks", false);
    public static Checkbox crystal = new Checkbox("Crystals", true);
    public static Checkbox fireworks = new Checkbox("Fireworks", false);
    public static Checkbox spawnEggs = new Checkbox("Spawn Eggs", false);

    @Override
    public void setup() {
        addSetting(blocks);
        addSetting(crystal);
        addSetting(fireworks);
        addSetting(spawnEggs);
    }

    @Override
    public void onUpdate() {
        if (InventoryUtil.getHeldItem(Items.END_CRYSTAL) && crystal.getValue() || InventoryUtil.getHeldItem(Items.FIREWORKS) && fireworks.getValue() || InventoryUtil.getHeldItem(Items.SPAWN_EGG) && spawnEggs.getValue())
            mc.rightClickDelayTimer = 0;

        if (Block.getBlockFromItem(mc.player.getHeldItemMainhand().getItem()).getDefaultState().isFullBlock() && blocks.getValue())
            mc.rightClickDelayTimer = 0;
    }
}
