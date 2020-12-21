package me.linus.momentum.module.modules.combat;

import me.linus.momentum.event.events.render.Render3DEvent;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.world.BlockUtils;
import me.linus.momentum.util.world.InventoryUtil;
import me.linus.momentum.util.world.PlayerUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yoink
 * @since 12/17/2020
 */

// TODO: rewrite this

public class HoleFill extends Module {
    public HoleFill() {
        super("HoleFill", Category.COMBAT, "Automatically fills in nearby holes");
    }

    private static final Mode mode = new Mode("Mode", "Obsidian", "Ender Chest", "Web");
    public static Slider range = new Slider("Range", 0.0D, 2.0D, 10.0D, 0);
    private static final Checkbox autoSwitch = new Checkbox("AutoSwitch", true);
    private static final Checkbox rotate = new Checkbox("Rotate", true);
    private static final Checkbox disable = new Checkbox("Disables", false);

    public static Checkbox color = new Checkbox("Color", true);
    public static SubSlider r = new SubSlider(color, "Red", 0.0D, 250.0D, 255.0D, 0);
    public static SubSlider g = new SubSlider(color, "Green", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider b = new SubSlider(color, "Blue", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider a = new SubSlider(color, "Alpha", 0.0D, 25.0D, 255.0D, 0);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(range);
        addSetting(autoSwitch);
        addSetting(rotate);
        addSetting(disable);
        addSetting(color);
    }

    private int oldHand;
    BlockPos renderBlock;

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        oldHand = mc.player.inventory.currentItem;
    }

    @Override
    public void onDisable() {
        mc.player.inventory.currentItem = oldHand;
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        List<BlockPos> blocks = getHoles(range.getValue());

        int slot = getItem();
        List<BlockPos> blocksToRemove = new ArrayList<>();

        if (slot == -1) {
            disable();
            return;
        }

        for (BlockPos block : blocks) {
            if (PlayerUtil.inPlayer(block)) blocksToRemove.add(block);
        }

        for (BlockPos blockPos : blocksToRemove) {
            blocks.remove(blockPos);
        }

        blocksToRemove.clear();

        if (blocks.size() == 0) {
            if (disable.getValue())
                disable();
                return;
        }

        renderBlock = blocks.get(0);

        if (autoSwitch.getValue())
            mc.player.inventory.currentItem = getItem();

        PlayerUtil.placeBlock(blocks.get(0), rotate.getValue());
    }

    private int getItem() {
        switch (mode.getValue()) {
            case 0:
                return InventoryUtil.getBlockInHotbar(Blocks.OBSIDIAN);
            case 1:
                return InventoryUtil.getBlockInHotbar(Blocks.ENDER_CHEST);
            case 3:
                return InventoryUtil.getBlockInHotbar(Blocks.WEB);
        }

        return InventoryUtil.getBlockInHotbar(Blocks.OBSIDIAN);
    }

    @Override
    public void onRender3D(Render3DEvent eventRender) {
        if (renderBlock != null)
            RenderUtil.drawBoxBlockPos(renderBlock, new Color((int) r.getValue(), (int) g.getValue(),  (int) b.getValue(), (int) a.getValue()));
    }

    public List<BlockPos> getHoles(double range) {
        NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll(BlockUtils.getSphere(new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ)), (int) range, (int) range, false, true, 0).stream().filter(BlockUtils::isHole).collect(Collectors.toList()));
        return positions;
    }
}
