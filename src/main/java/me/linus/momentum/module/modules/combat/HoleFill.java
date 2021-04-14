package me.linus.momentum.module.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.linus.momentum.managers.notification.Notification;
import me.linus.momentum.managers.notification.Notification.Type;
import me.linus.momentum.managers.notification.NotificationManager;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.color.ColorPicker;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.client.color.ThemeColor;
import me.linus.momentum.util.player.InventoryUtil;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.render.builder.RenderBuilder;
import me.linus.momentum.util.world.BlockUtil;
import me.linus.momentum.util.world.hole.HoleUtil;
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
    public static Mode autoSwitch = new Mode("Switch", "SwitchBack", "Normal", "Packet", "None");
    public static Checkbox raytrace = new Checkbox("Raytrace", true);
    public static Checkbox packet = new Checkbox("Packet", false);
    public static Checkbox swingArm = new Checkbox("Swing Arm", true);
    public static Checkbox antiGlitch = new Checkbox("Anti-Glitch", false);

    public static Checkbox rotate = new Checkbox("Rotate", false);
    public static SubCheckbox strict = new SubCheckbox(rotate, "Strict", false);

    public static Checkbox disable = new Checkbox("Disables", true);
    public static SubCheckbox completion = new SubCheckbox(disable, "Completion", true);

    public static Checkbox color = new Checkbox("Color", true);
    public static ColorPicker colorPicker = new ColorPicker(color, "Color Picker",  new Color(250, 0, 250, 50));

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
        addSetting(disable);
        addSetting(color);
    }

    int obsidianSlot;
    public static boolean processing = false;
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

        processing = !fillHoles.isEmpty();

        if (fillHoles.isEmpty() && completion.getValue())
            this.disable();

        for (BlockPos hole : fillHoles) {
            if (mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(hole)).isEmpty())
                currentFill = hole;
        }

        fillBlock = currentFill;

        int previousSlot = mc.player.inventory.currentItem;

        switch (autoSwitch.getValue()) {
            case 0:
            case 1:
                InventoryUtil.switchToSlot(InventoryUtil.getBlockInHotbar(Blocks.OBSIDIAN));
                break;
            case 2:
                InventoryUtil.switchToSlotGhost(InventoryUtil.getBlockInHotbar(Blocks.OBSIDIAN));
                break;
        }

        if (obsidianSlot != -1)
            BlockUtil.placeBlock(fillBlock, rotate.getValue(), strict.getValue(), raytrace.getValue(), packet.getValue(), swingArm.getValue(), antiGlitch.getValue());

        if (autoSwitch.getValue() == 0)
            InventoryUtil.switchToSlot(previousSlot);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        if (fillBlock != null)
            RenderUtil.drawBoxBlockPos(fillBlock, 0, 0, 0, colorPicker.getColor(), RenderBuilder.RenderMode.Fill);
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

    public List<BlockPos> getHoles() {
        switch (mode.getValue()) {
            case 0:
                return BlockUtil.getNearbyBlocks(fillTarget, smartRange.getValue(), false).stream().filter(HoleUtil::isHole).filter(blockPos -> mc.player.getDistanceSq(blockPos) < Math.pow(range.getValue(), 2)).collect(Collectors.toList());
            case 1:
                return BlockUtil.getNearbyBlocks(mc.player, range.getValue(), false).stream().filter(HoleUtil::isHole).collect(Collectors.toList());
        }

        return null;
    }
}