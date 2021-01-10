package me.linus.momentum.module.modules.combat;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.world.BlockUtil;
import me.linus.momentum.util.world.HoleUtil;
import me.linus.momentum.util.player.InventoryUtil;
import me.linus.momentum.util.player.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
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
    public static SubSlider a = new SubSlider(color, "Alpha", 0.0D, 40.0D, 255.0D, 0);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(range);
        addSetting(autoSwitch);
        addSetting(rotate);
        addSetting(disable);
        addSetting(color);
    }

    BlockPos renderBlock;

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        List<BlockPos> blocks = getHoles();

        for (BlockPos block : blocks)
            blocks.removeIf(blockPos -> PlayerUtil.inPlayer(block));

        if (blocks.size() == 0) {
            if (disable.getValue())
                disable();
                return;
        }

        renderBlock = blocks.get(0);

        if (autoSwitch.getValue())
            InventoryUtil.switchToSlot(getItem());

        BlockUtil.placeBlock(blocks.get(0), rotate.getValue());
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        if (renderBlock != null)
            RenderUtil.drawBoxBlockPos(renderBlock, 0, new Color((int) r.getValue(), (int) g.getValue(),  (int) b.getValue(), (int) a.getValue()));
    }

    private Block getItem() {
        switch (mode.getValue()) {
            case 0:
                return Blocks.OBSIDIAN;
            case 1:
                return Blocks.ENDER_CHEST;
            case 3:
                return Blocks.WEB;
        }

        return Blocks.OBSIDIAN;
    }

    private List<BlockPos> getHoles() {
        return BlockUtil.getNearbyBlocks(mc.player, range.getValue(), false).stream().filter(blockPos -> HoleUtil.isHole(blockPos)).collect(Collectors.toList());
    }
}
