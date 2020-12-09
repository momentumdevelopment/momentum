package me.linus.momentum.module.modules.combat;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.render.GeometryMasks;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.world.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HoleFill extends Module {
    public HoleFill() {
        super("HoleFill", Category.COMBAT, "Automatically fills in nearby holes");
    }

    private static Mode mode = new Mode("Mode", "Obsidian", "Ender Chest", "Web");
    public static Slider range = new Slider("Range", 0.0D, 2.0D, 10.0D, 0);
    private static Checkbox rotate = new Checkbox("Rotate", false);
    private static Checkbox disable = new Checkbox("Disables", false);

    public static Checkbox color = new Checkbox("Color", true);
    public static SubSlider r = new SubSlider(color, "Red", 0.0D, 250.0D, 255.0D, 0);
    public static SubSlider g = new SubSlider(color, "Green", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider b = new SubSlider(color, "Blue", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider a = new SubSlider(color, "Alpha", 0.0D, 30.0D, 255.0D, 0);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(range);
        addSetting(rotate);
        addSetting(disable);
        addSetting(color);
    }

    private int oldHand;
    private BlockPos renderBlock;

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
        List<BlockPos> blocks = getHoles();
        if (nullCheck())
            return;

        int slot = getSlot();
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
        PlayerUtil.placeBlock(blocks.get(0));
    }

    private int getSlot() {
        Block block = Blocks.AIR;

        switch (mode.getValue()) {
            case 0:
                block = Blocks.OBSIDIAN;
                break;
            case 1:
                block = Blocks.ENDER_CHEST;
                break;
            case 3:
                block = Blocks.WEB;
                break;
        }

        int slot = mc.player.getHeldItemMainhand().getItem() == Item.getItemFromBlock(block) ? mc.player.inventory.currentItem : -1;
        if (slot == -1) {
            for (int i = 0; i < 9; i++) {
                if (mc.player.inventory.getStackInSlot(i).getItem() == Item.getItemFromBlock(block)) {
                    slot = i;
                    break;
                }
            }
        }
        return slot;
    }

    @SubscribeEvent
    public void onRender3D(RenderWorldLastEvent eventRender) {
        if (renderBlock != null) {
            RenderUtil.prepareRender(GL11.GL_QUADS);
            RenderUtil.drawBoxFromBlockPos(renderBlock, new Color((int) r.getValue(), (int) g.getValue(), (int) b.getValue(), (int) a.getValue()), GeometryMasks.Quad.ALL);
            RenderUtil.releaseRender();
        }
    }

    private List<BlockPos> getHoles() {
        NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll(getSphere(new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ)), (int) range.getValue(), (int) range.getValue(), false, true, 0).stream().filter(this::isHole).collect(Collectors.toList()));
        return positions;
    }

    public List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        List<BlockPos> blocks = new ArrayList<>();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();
        for (int x = cx - (int) r; x <= cx + r; x++) {
            for (int z = cz - (int) r; z <= cz + r; z++) {
                for (int y = (sphere ? cy - (int) r : cy); y < (sphere ? cy + r : cy + h); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        blocks.add(l);
                    }
                }
            }
        }
        return blocks;
    }

    private boolean isHole(BlockPos blockPos) {
        BlockPos b1 = blockPos.add(0, 1, 0);
        BlockPos b2 = blockPos.add(0, 0, 0);
        BlockPos b3 = blockPos.add(0, 0, -1);
        BlockPos b4 = blockPos.add(1, 0, 0);
        BlockPos b5 = blockPos.add(-1, 0, 0);
        BlockPos b6 = blockPos.add(0, 0, 1);
        BlockPos b7 = blockPos.add(0, 2, 0);
        BlockPos b8 = blockPos.add(0.5, 0.5, 0.5);
        BlockPos b9 = blockPos.add(0, -1, 0);
        return mc.world.getBlockState(b1).getBlock() == Blocks.AIR && (mc.world.getBlockState(b2).getBlock() == Blocks.AIR) && (mc.world.getBlockState(b7).getBlock() == Blocks.AIR) && ((mc.world.getBlockState(b3).getBlock() == Blocks.OBSIDIAN) || (mc.world.getBlockState(b3).getBlock() == Blocks.BEDROCK)) && ((mc.world.getBlockState(b4).getBlock() == Blocks.OBSIDIAN) || (mc.world.getBlockState(b4).getBlock() == Blocks.BEDROCK)) && ((mc.world.getBlockState(b5).getBlock() == Blocks.OBSIDIAN) || (mc.world.getBlockState(b5).getBlock() == Blocks.BEDROCK)) && ((mc.world.getBlockState(b6).getBlock() == Blocks.OBSIDIAN) || (mc.world.getBlockState(b6).getBlock() == Blocks.BEDROCK)) && (mc.world.getBlockState(b8).getBlock() == Blocks.AIR) && ((mc.world.getBlockState(b9).getBlock() == Blocks.OBSIDIAN) || (mc.world.getBlockState(b9).getBlock() == Blocks.BEDROCK));
    }
}
