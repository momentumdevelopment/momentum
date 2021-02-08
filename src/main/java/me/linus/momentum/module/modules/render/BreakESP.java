package me.linus.momentum.module.modules.render;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.color.ColorPicker;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.render.builder.RenderBuilder;
import me.linus.momentum.util.render.RenderUtil;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 11/30/2020
 */

public class BreakESP extends Module {
    public BreakESP() {
        super("BreakESP", Category.RENDER, "Highlights blocks being broken");
    }

    public static Slider range = new Slider("Range", 0.0D, 12.0D, 20.0D, 0);
    public static Checkbox outline = new Checkbox("Outline", false);
    public static Checkbox showInfo = new Checkbox("Render Info", true);

    public static Checkbox color = new Checkbox("Color", true);
    public static ColorPicker colorPicker = new ColorPicker(color, "Color Picker", new Color(255, 0, 0, 55));

    @Override
    public void setup() {
        addSetting(range);
        addSetting(outline);
        addSetting(showInfo);
        addSetting(color);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        if (nullCheck())
            return;

        mc.renderGlobal.damagedBlocks.forEach((integer, destroyBlockProgress) -> {
            if (destroyBlockProgress != null && destroyBlockProgress.getPosition().getDistance((int) mc.player.posX,(int)  mc.player.posY,(int)  mc.player.posZ) <= range.getValue()) {
                RenderUtil.drawBoxBlockPos(destroyBlockProgress.getPosition(), 0, colorPicker.getColor(), outline.getValue() ? RenderBuilder.RenderMode.Both : RenderBuilder.RenderMode.Fill);

                if (showInfo.getValue()) {
                    RenderUtil.drawNametagFromBlockPos(destroyBlockProgress.getPosition(), 0.6f, mc.world.getEntityByID(integer).getName());
                    RenderUtil.drawNametagFromBlockPos(destroyBlockProgress.getPosition(), 0.2f,(destroyBlockProgress.getPartialBlockDamage() * 10) + "%");
                }
            }
        });
    }
}