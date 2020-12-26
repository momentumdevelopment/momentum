package me.linus.momentum.module.modules.render;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.combat.EnemyUtil;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.world.EntityUtil;
import net.minecraft.entity.player.EntityPlayer;
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

    public static Checkbox color = new Checkbox("Color", true);
    public static SubSlider r = new SubSlider(color, "Red", 0.0D, 250.0D, 255.0D, 0);
    public static SubSlider g = new SubSlider(color, "Green", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider b = new SubSlider(color, "Blue", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider a = new SubSlider(color, "Alpha", 0.0D, 30.0D, 255.0D, 0);

    @Override
    public void setup() {
        addSetting(range);
        addSetting(color);
    }

    List<BlockPos> cityBlocks = new ArrayList<>();

    @Override
    public void onUpdate() {
        cityBlocks.clear();

        EntityPlayer target = EntityUtil.getClosestPlayer(20);

        EnemyUtil.getCityBlocks(target, false).stream().filter(blockPos -> mc.player.getDistanceSq(blockPos) <= range.getValue()).forEach(blockPos -> {
            cityBlocks.add(blockPos);
        });
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        for (BlockPos cityPos : cityBlocks)
            RenderUtil.drawBoxBlockPos(cityPos, new Color((int) r.getValue(), (int) g.getValue(),  (int) b.getValue(), (int) a.getValue()));
    }
}
