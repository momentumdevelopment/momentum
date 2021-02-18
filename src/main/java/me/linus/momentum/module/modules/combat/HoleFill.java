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
import me.linus.momentum.util.render.builder.RenderBuilder;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.player.InventoryUtil;
import me.linus.momentum.util.world.BlockUtil;
import me.linus.momentum.util.world.HoleUtil;
import me.linus.momentum.util.world.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
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

    public static Mode mode = new Mode("Mode", "Smart", "All");
    public static Mode block = new Mode("Block", "Obsidian", "EnderChest", "Web", "Pressure Plate");
    public static Slider range = new Slider("Range", 0.0D, 5.0D, 10.0D, 0);
    public static Slider smartRange = new Slider("Smart Range", 0.0D, 2.0D, 10.0D, 0);
    public static Checkbox autoSwitch = new Checkbox("AutoSwitch", true);
    public static Checkbox raytrace = new Checkbox("Raytrace", false);
    public static Checkbox packet = new Checkbox("Packet", false);
    public static Checkbox swingArm = new Checkbox("Swing Arm", true);
    public static Checkbox antiGlitch = new Checkbox("Anti-Glitch", false);
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
        addSetting(raytrace);
        addSetting(packet);
        addSetting(swingArm);
        addSetting(antiGlitch);
        addSetting(rotate);
        addSetting(strict);
        addSetting(disable);
        addSetting(color);
    }

    int obsidianSlot;

    BlockPos fillBlock;
    EntityPlayer fillTarget;

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        super.onEnable();

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

        fillTarget = WorldUtil.getClosestPlayer(15);

        List<BlockPos> fillHoles = getHoles();
        BlockPos currentFill = null;

        for (BlockPos hole : fillHoles) {
            if (mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(hole)).isEmpty())
                currentFill = hole;
        }

        fillBlock = currentFill;

        if (autoSwitch.getValue())
            InventoryUtil.switchToSlot(getItem());

        if (obsidianSlot != -1)
            BlockUtil.placeBlock(fillBlock, rotate.getValue(), strict.getValue(), raytrace.getValue(), packet.getValue(), swingArm.getValue(), antiGlitch.getValue());
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        if (fillBlock != null)
            RenderUtil.drawBoxBlockPos(fillBlock, 0, colorPicker.getColor(), RenderBuilder.RenderMode.Fill);
    }

    public Block getItem() {
        switch (mode.getValue()) {
            case 0:
                return Blocks.OBSIDIAN;
            case 1:
                return Blocks.ENDER_CHEST;
            case 3:
                return Blocks.WEB;
            case 4:
                return Blocks.WOODEN_PRESSURE_PLATE;
        }

        return Blocks.OBSIDIAN;
    }

    List<BlockPos> getHoles() {
        switch (mode.getValue()) {
            case 0:
                return BlockUtil.getNearbyBlocks(fillTarget, smartRange.getValue(), false).stream().filter(HoleUtil::isHole).filter(blockPos -> mc.player.getDistanceSq(blockPos) < MathUtil.square(range.getValue())).collect(Collectors.toList());
            case 1:
                return BlockUtil.getNearbyBlocks(mc.player, range.getValue(), false).stream().filter(HoleUtil::isHole).collect(Collectors.toList());
        }

        return null;
    }
}