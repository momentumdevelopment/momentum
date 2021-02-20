package me.linus.momentum.module.modules.render;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.color.ColorPicker;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.render.builder.RenderBuilder;
import me.linus.momentum.util.world.WorldUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author linustouchtips
 * @since 11/30/2020
 */

public class BurrowESP extends Module {
    public BurrowESP() {
        super("BurrowESP", Category.RENDER, "Highlights players that are burrowed");
    }

    public static Slider range = new Slider("Range", 0.0D, 7.0D, 10.0D, 0);
    public static Checkbox outline = new Checkbox("Outline", false);

    public static Checkbox color = new Checkbox("Color", true);
    public static ColorPicker colorPicker = new ColorPicker(color, "Color Picker", new Color(255, 0, 0, 55));

    @Override
    public void setup() {
        addSetting(range);
        addSetting(outline);
        addSetting(color);
    }

    List<BlockPos> burrowList = new ArrayList<>();

    @Override
    public void onUpdate() {
        burrowList.clear();

        WorldUtil.getNearbyPlayers(20).forEach(entityPlayer -> {
            if (mc.world.getBlockState(new BlockPos(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ)).getBlock().equals(Blocks.OBSIDIAN))
                burrowList.add(new BlockPos(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ));
        });
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        for (BlockPos burrowPos : burrowList)
            RenderUtil.drawBoxBlockPos(burrowPos, 0, colorPicker.getColor(), outline.getValue() ? RenderBuilder.RenderMode.Both : RenderBuilder.RenderMode.Fill);
    }
}