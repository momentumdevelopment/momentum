package me.linus.momentum.module.modules.combat;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.world.InventoryUtil;
import me.linus.momentum.util.world.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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

    public static Slider delay = new Slider("Delay", 0.0D, 3.0D, 6.0D, 0);
    public static Slider blocksPerTick = new Slider("Blocks Per Tick", 0.0D, 1.0D, 6.0D, 0);
    private static final Checkbox rotate = new Checkbox("Rotate", true);
    private static final Checkbox disable = new Checkbox("Disables", true);

    public static Checkbox color = new Checkbox("Color", true);
    public static SubSlider r = new SubSlider(color, "Red", 0.0D, 255.0D, 255.0D, 0);
    public static SubSlider g = new SubSlider(color, "Green", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider b = new SubSlider(color, "Blue", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider a = new SubSlider(color, "Alpha", 0.0D, 30.0D, 255.0D, 0);

    @Override
    public void setup() {
        addSetting(delay);
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

        for (Vec3d autoTrapBox : fullTrap) {
            final EntityPlayer target = mc.player;


            BlockPos blockPos = new BlockPos(autoTrapBox.add(mc.player.getPositionVector()));

            if (mc.world.getBlockState(blockPos).getBlock().equals(Blocks.AIR)) {
                int oldInventorySlot = mc.player.inventory.currentItem;
                mc.player.inventory.currentItem = InventoryUtil.getBlockInHotbar(Blocks.OBSIDIAN);
                PlayerUtil.placeBlock(blockPos, rotate.getValue());
                renderBlocks.add(blockPos);
                mc.player.inventory.currentItem = oldInventorySlot;
                blocksPlaced++;

                if (blocksPlaced == blocksPerTick.getValue())
                    return;
            }
        }

        if (blocksPlaced == 0)
            hasPlaced = true;
    }

    @SubscribeEvent
    public void onRender3D(RenderWorldLastEvent renderEvent) {
        for (BlockPos renderBlock : renderBlocks) {
            RenderUtil.drawVanillaBoxFromBlockPos(renderBlock, (float) r.getValue() / 255f, (float) g.getValue() / 255f, (float) b.getValue() / 255f, (float) a.getValue() / 255f);
        }
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
}