package me.linus.momentum.module.modules.combat;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.client.external.MessageUtil;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.world.EntityUtil;
import me.linus.momentum.util.world.InventoryUtil;
import me.linus.momentum.util.world.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
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
 */

public class AutoTrap extends Module {
    public AutoTrap() {
        super("AutoTrap", Category.COMBAT, "Automatically traps nearby players");
    }

    private static final Mode mode = new Mode("Mode", "Full", "Feet", "Bed");
    public static Slider delay = new Slider("Delay", 0.0D, 3.0D, 6.0D, 0);
    public static Slider range = new Slider("Range", 0.0D, 7.0D, 10.0D, 0);
    public static Slider blocksPerTick = new Slider("Blocks Per Tick", 0.0D, 1.0D, 6.0D, 0);
    private static final Checkbox autoSwitch = new Checkbox("AutoSwitch", true);
    private static final Checkbox rotate = new Checkbox("Rotate", true);
    private static final Checkbox disable = new Checkbox("Disables", true);

    public static Checkbox color = new Checkbox("Color", true);
    public static SubSlider r = new SubSlider(color, "Red", 0.0D, 255.0D, 255.0D, 0);
    public static SubSlider g = new SubSlider(color, "Green", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider b = new SubSlider(color, "Blue", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider a = new SubSlider(color, "Alpha", 0.0D, 25.0D, 255.0D, 0);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(delay);
        addSetting(range);
        addSetting(autoSwitch);
        addSetting(blocksPerTick);
        addSetting(rotate);
        addSetting(disable);
        addSetting(color);
    }

    private final ArrayList<BlockPos> renderBlocks = new ArrayList<>();
    private boolean hasPlaced;

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

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
            final EntityPlayer target = EntityUtil.getClosestPlayer(range.getValue());

            if (target != null) {
                BlockPos blockPos = new BlockPos(autoTrapBox.add(EntityUtil.getClosestPlayer(range.getValue()).getPositionVector()));

                if (mc.world.getBlockState(blockPos).getBlock().equals(Blocks.AIR)) {
                    int oldInventorySlot = mc.player.inventory.currentItem;

                    if (autoSwitch.getValue())
                        mc.player.inventory.currentItem = InventoryUtil.getBlockInHotbar(Blocks.OBSIDIAN);

                    PlayerUtil.placeBlock(blockPos, rotate.getValue());
                    MessageUtil.sendClientMessage("Trapping " + target.getName() + "!");
                    renderBlocks.add(blockPos);

                    if (autoSwitch.getValue())
                        mc.player.inventory.currentItem = oldInventorySlot;

                    blocksPlaced++;

                    if (blocksPlaced == blocksPerTick.getValue())
                        return;
                }
            }
        }

        if (blocksPlaced == 0)
            hasPlaced = true;
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        for (BlockPos renderBlock : renderBlocks)
            RenderUtil.drawBoxBlockPos(renderBlock, new Color((int) r.getValue(), (int) g.getValue(),  (int) b.getValue(), (int) a.getValue()));
    }

    public List<Vec3d> getTrap() {
        switch (mode.getValue()) {
            case 0:
                return fullTrap;
            case 1:
                return feetTrap;
            case 2:
                return bedTrap;
        }

        return fullTrap;
    }

    private final List<Vec3d> fullTrap = new ArrayList<>(Arrays.asList(
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

    private final List<Vec3d> feetTrap = new ArrayList<>(Arrays.asList(
            new Vec3d(0, -1, 1),
            new Vec3d(0, 0, 1),
            new Vec3d(1, 0, 0),
            new Vec3d(0, 0, -1),
            new Vec3d(-1, 0, 0),
            new Vec3d(0, 1, -1),
            new Vec3d(1, 1, 0),
            new Vec3d(0, 1, 1),
            new Vec3d(-1, 1, 0),
            new Vec3d(0, 2, -1),
            new Vec3d(0, 2, 1),
            new Vec3d(0, 2, 0)
    ));

    private final List<Vec3d> bedTrap = new ArrayList<>(Arrays.asList(
            new Vec3d(0, -1, -1),
            new Vec3d(1, -1, 0),
            new Vec3d(0, -1, 1),
            new Vec3d(-1, -1, 0),
            new Vec3d(0, 0, -1),
            new Vec3d(1, 0, 0),
            new Vec3d(0, 0, 1),
            new Vec3d(-1, 0, 0),
            new Vec3d(0, 1, -1),
            new Vec3d(0, 2, -1),
            new Vec3d(0, 2, 1),
            new Vec3d(0, 2, 0)
    ));
}