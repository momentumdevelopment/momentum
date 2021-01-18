package me.linus.momentum.module.modules.render;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.color.SubColor;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.combat.EnemyUtil;
import me.linus.momentum.util.render.builder.RenderBuilder;
import me.linus.momentum.util.render.builder.RenderUtil;
import me.linus.momentum.util.world.WorldUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author linustouchtips
 * @since 11/29/2020
 */

public class CityESP extends Module {
    public CityESP() {
        super("CityESP", Category.RENDER, "Highlights blocks where nearby players can be citied");
    }

    public static Slider range = new Slider("Range", 0.0D, 12.0D, 20.0D, 0);
    public static Checkbox outline = new Checkbox("Outline", false);

    public static Checkbox color = new Checkbox("Color", true);
    public static SubColor colorPicker = new SubColor(color, new Color(255, 0, 0, 55));

    @Override
    public void setup() {
        addSetting(range);
        addSetting(outline);
        addSetting(color);
    }

    List<BlockPos> cityBlocks = new ArrayList<>();

    @Override
    public void onUpdate() {
        cityBlocks.clear();

        WorldUtil.getNearbyPlayers(20).stream().forEach(entityPlayer -> {
            EnemyUtil.getCityBlocks(entityPlayer, false).stream().filter(blockPos -> mc.player.getDistanceSq(blockPos) <= range.getValue()).forEach(blockPos -> {
                cityBlocks.add(blockPos);
            });
        });
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        for (BlockPos cityPos : cityBlocks)
            RenderUtil.drawBoxBlockPos(cityPos, 0, colorPicker.getColor(), outline.getValue() ? RenderBuilder.renderMode.Both : RenderBuilder.renderMode.Fill);
    }
}