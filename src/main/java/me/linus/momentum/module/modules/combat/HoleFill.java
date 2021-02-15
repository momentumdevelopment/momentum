package me.linus.momentum.module.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.linus.momentum.managers.notification.Notification;
import me.linus.momentum.managers.notification.Notification.Type;
import me.linus.momentum.managers.notification.NotificationManager;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.color.ColorPicker;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.client.MessageUtil;
import me.linus.momentum.util.player.PlayerUtil;
import me.linus.momentum.util.render.builder.RenderBuilder;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.player.InventoryUtil;
import me.linus.momentum.util.world.BlockUtil;
import me.linus.momentum.util.world.HoleUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author linustouchtips & olliem5
 * @since 12/17/2020
 */

public class HoleFill extends Module {
    public HoleFill() {
        super("HoleFill", Category.COMBAT, "Automatically fills in nearby holes");
    }

    public static Mode mode = new Mode("Mode", "Target", "All");
    public static Mode block = new Mode("Block", "Obsidian", "Ender Chest", "Web", "Pressure Plate");
    public static Slider range = new Slider("Range", 0.0D, 5.0D, 10.0D, 0);
    public static Checkbox autoSwitch = new Checkbox("AutoSwitch", true);
    public static Checkbox rotate = new Checkbox("Rotate", false);
    public static Checkbox strict = new Checkbox("NCP Strict", true);
    public static Checkbox disable = new Checkbox("Disables", false);

    public static Checkbox color = new Checkbox("Color", true);
    public static ColorPicker colorPicker = new ColorPicker(color, "Color Picker",  new Color(255, 0, 0, 55));

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(block);
        addSetting(range);
        addSetting(autoSwitch);
        addSetting(rotate);
        addSetting(strict);
        addSetting(disable);
        addSetting(color);
    }

    int obsidianSlot;
    BlockPos renderBlock;

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        obsidianSlot = InventoryUtil.getBlockInHotbar(Blocks.OBSIDIAN);

        if (obsidianSlot == -1) {
            NotificationManager.addNotification(new Notification("No Obsidian, " + ChatFormatting.RED + "Disabling!", Type.Info));
            this.disable();
        }
    }

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

        if (obsidianSlot != -1) {
            BlockUtil.placeBlock(blocks.get(0), rotate.getValue(), strict.getValue());
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        if (renderBlock != null)
            RenderUtil.drawBoxBlockPos(renderBlock, 0, colorPicker.getColor(), RenderBuilder.RenderMode.Fill);
    }

    public Block getItem() {
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

    List<BlockPos> getHoles() {
        return BlockUtil.getNearbyBlocks(mc.player, range.getValue(), false).stream().filter(blockPos -> HoleUtil.isHole(blockPos)).collect(Collectors.toList());
    }
}