package me.linus.momentum.module.modules.combat;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.render.GeometryMasks;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.world.EntityUtil;
import me.linus.momentum.util.world.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author linustouchtips
 * @since 11/28/2020
 */

public class AutoTrap extends Module {
    public AutoTrap() {
        super("AutoTrap", Category.COMBAT, "Automatically traps nearby players");
    }

    public static Slider delay = new Slider("Delay", 0.0D, 3.0D, 6.0D, 0);
    public static Slider range = new Slider("Range", 0.0D, 7.0D, 10.0D, 0);
    private static Checkbox rotate = new Checkbox("Rotate", false);
    private static Checkbox disable = new Checkbox("Disables", true);

    public static Checkbox color = new Checkbox("Color", true);
    public static SubSlider r = new SubSlider(color, "Red", 0.0D, 255.0D, 255.0D, 0);
    public static SubSlider g = new SubSlider(color, "Green", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider b = new SubSlider(color, "Blue", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider a = new SubSlider(color, "Alpha", 0.0D, 30.0D, 255.0D, 0);

    @Override
    public void setup() {
        addSetting(delay);
        addSetting(range);
        addSetting(rotate);
        addSetting(disable);
        addSetting(color);
    }

    private final ArrayList<BlockPos> renderBlocks = new ArrayList<>();
    private int ticksOn;

    @Override
    public void onEnable() {
        ticksOn = 0;
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        EntityPlayer closestPlayer = EntityUtil.getClosestPlayer(range.getValue());

        if (closestPlayer == null) {
            if (disable.getValue())
                disable();

            renderBlocks.clear();
            return;
        }

        List<BlockPos> fullPos = new ArrayList<>(Arrays.asList(
                (new BlockPos(closestPlayer.getPositionVector())).add(0, -1, -1),
                (new BlockPos(closestPlayer.getPositionVector())).add(1, -1, 0),
                (new BlockPos(closestPlayer.getPositionVector())).add(0, -1, 1),
                (new BlockPos(closestPlayer.getPositionVector())).add(-1, -1, 0),
                (new BlockPos(closestPlayer.getPositionVector())).add(0, 0, -1),
                (new BlockPos(closestPlayer.getPositionVector())).add(1, 0, 0),
                (new BlockPos(closestPlayer.getPositionVector())).add(0, 0, 1),
                (new BlockPos(closestPlayer.getPositionVector())).add(-1, 0, 0),
                (new BlockPos(closestPlayer.getPositionVector())).add(0, 1, -1),
                (new BlockPos(closestPlayer.getPositionVector())).add(1, 1, 0),
                (new BlockPos(closestPlayer.getPositionVector())).add(0, 1, 1),
                (new BlockPos(closestPlayer.getPositionVector())).add(-1, 1, 0),
                (new BlockPos(closestPlayer.getPositionVector())).add(0, 2, -1),
                (new BlockPos(closestPlayer.getPositionVector())).add(0, 2, 1),
                (new BlockPos(closestPlayer.getPositionVector())).add(0, 2, 0)
        ));

        renderBlocks.clear();

        for (Object object : new ArrayList<>(fullPos)) {
            BlockPos blockPos = (BlockPos) object;
            fullPos.add(0, blockPos.down());

            if (mc.world.getBlockState(blockPos).getBlock().equals(Blocks.AIR))
                renderBlocks.add(blockPos);
        }


        int slot = getObsidianSlot();

        if (slot != -1) {
            if (disable.getValue())
                ticksOn++;

            int i = 0;
            int hand = mc.player.inventory.currentItem;
            for (BlockPos blockPos : fullPos) {
                if (PlayerUtil.placeBlock(blockPos, slot, rotate.getValue(), rotate.getValue())) i++;

                int BPT = (int) (Math.round(delay.getValue() / 300f) + 1);
                if (i >= BPT) break;
            }

            mc.player.inventory.currentItem = hand;

            if (ticksOn > 30) {
                if (disable.getValue())
                    disable();

                renderBlocks.clear();
            }
        }

        else {
            if (disable.getValue())
                disable();

            renderBlocks.clear();
        }
    }

    @SubscribeEvent
    public void onRender3D(RenderWorldLastEvent eventRender) {
        for (BlockPos blockPos : renderBlocks) {
            if (renderBlocks != null) {
                RenderUtil.prepareRender(GL11.GL_QUADS);
                RenderUtil.drawBoxFromBlockPos(blockPos, new Color((int) r.getValue(), (int) g.getValue(), (int) b.getValue(), (int) a.getValue()), GeometryMasks.Quad.ALL);
                RenderUtil.releaseRender();
            }
        }
    }

    public int getObsidianSlot() {
        int slot = -1;

        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);

            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemBlock))
                continue;

            Block block = ((ItemBlock) stack.getItem()).getBlock();

            if (block instanceof BlockObsidian) {
                slot = i;
                break;
            }
        }

        return slot;
    }

}
