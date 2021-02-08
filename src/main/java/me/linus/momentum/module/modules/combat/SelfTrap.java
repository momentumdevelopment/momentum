package me.linus.momentum.module.modules.combat;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.color.ColorPicker;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.render.builder.RenderBuilder;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.world.BlockUtil;
import me.linus.momentum.util.player.InventoryUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author linustouchtips & olliem5
 * @since 11/28/2020
 * more or less a paste of autotrap
 */

public class SelfTrap extends Module {
    public SelfTrap() {
        super("SelfTrap", Category.COMBAT, "Automatically traps yourself");
    }

    public static Mode mode = new Mode("Mode", "Head", "Anti-FacePlace", "Full");
    public static Slider delay = new Slider("Delay", 0.0D, 3.0D, 6.0D, 0);
    public static Slider blocksPerTick = new Slider("Blocks Per Tick", 0.0D, 1.0D, 6.0D, 0);
    public static Checkbox rotate = new Checkbox("Rotate", true);
    public static Checkbox disable = new Checkbox("Disables", false);

    public static Checkbox color = new Checkbox("Color", true);
    public static ColorPicker colorPicker = new ColorPicker(color, "Color Picker",  new Color(109, 231, 217, 121));

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(delay);
        addSetting(blocksPerTick);
        addSetting(rotate);
        addSetting(disable);
        addSetting(color);
    }

    BlockPos placeBlock;
    boolean hasPlaced;

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        super.onEnable();

        hasPlaced = false;
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (hasPlaced && disable.getValue())
            this.disable();

        int blocksPlaced = 0;

        for (Vec3d autoTrapBox : getTrap()) {
            if (BlockUtil.getBlockResistance(new BlockPos(autoTrapBox.add(mc.player.getPositionVector()))).equals(BlockUtil.blockResistance.Blank)) {
                InventoryUtil.switchToSlot(InventoryUtil.getBlockInHotbar(Blocks.OBSIDIAN));
                BlockUtil.placeBlock(new BlockPos(autoTrapBox.add(mc.player.getPositionVector())), rotate.getValue());
                placeBlock = new BlockPos(autoTrapBox.add(mc.player.getPositionVector()));
                blocksPlaced++;

                if (blocksPlaced == blocksPerTick.getValue())
                    return;
            }
        }

        if (blocksPlaced == 0)
            hasPlaced = true;
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        if (placeBlock != null)
            RenderUtil.drawBoxBlockPos(placeBlock, 0, colorPicker.getColor(), RenderBuilder.RenderMode.Fill);
    }

    public List<Vec3d> getTrap() {
        switch (mode.getValue()) {
            case 0:
                return headTrap;
            case 1:
                return faceTrap;
            case 2:
                return fullTrap;
        }

        return headTrap;
    }

    List<Vec3d> headTrap = new ArrayList<>(Arrays.asList(
            new Vec3d(0, 2, 0)
    ));

    List<Vec3d> faceTrap = new ArrayList<>(Arrays.asList(
            new Vec3d(0, 1, -1),
            new Vec3d(1, 1, 0),
            new Vec3d(0, 1, 1),
            new Vec3d(-1, 1, 0)
    ));

    List<Vec3d> fullTrap = new ArrayList<>(Arrays.asList(
            new Vec3d(0, -1, -1),
            new Vec3d(1, -1, 0),
            new Vec3d(0, -1, 1),
            new Vec3d(-1, -1, 0),
            new Vec3d(0, 0, -1),
            new Vec3d(1, 0, 0),
            new Vec3d(0, 0, 1),
            new Vec3d(-1, 0, 0),
            new Vec3d(0, 1, -1),
            new Vec3d(1, 1, 0),
            new Vec3d(0, 1, 1),
            new Vec3d(-1, 1, 0),
            new Vec3d(0, 2, -1),
            new Vec3d(0, 2, 1),
            new Vec3d(0, 2, 0)
    ));
}