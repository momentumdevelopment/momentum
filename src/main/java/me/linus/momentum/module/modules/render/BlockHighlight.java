package me.linus.momentum.module.modules.render;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.color.ColorPicker;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.util.client.color.ThemeColor;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.render.builder.RenderBuilder.RenderMode;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class BlockHighlight extends Module {
    public BlockHighlight() {
        super("BlockHighlight", Category.RENDER, "Highlights the block you're facing");
    }

    public static Mode mode = new Mode("Mode", "Claw", "Outline", "Fill", "Both");

    public static Checkbox color = new Checkbox("Color", true);
    public static ColorPicker colorPicker = new ColorPicker(color, "Color Picker",   new Color(250, 0, 250, 50));

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(color);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        if (mc.objectMouseOver.typeOfHit.equals(RayTraceResult.Type.BLOCK)) {
            switch (mode.getValue()) {
                case 0:
                    RenderUtil.drawBoxBlockPos(mc.objectMouseOver.getBlockPos(), 0, 0, 0, colorPicker.getColor(), RenderMode.Claw);
                    break;
                case 1:
                    RenderUtil.drawBoxBlockPos(mc.objectMouseOver.getBlockPos(), 0, 0, 0, new Color(colorPicker.getRed(), colorPicker.getGreen(), colorPicker.getBlue(), 144), RenderMode.Outline);
                    break;
                case 2:
                    RenderUtil.drawBoxBlockPos(mc.objectMouseOver.getBlockPos(), 0, 0, 0, colorPicker.getColor(), RenderMode.Fill);
                    break;
                case 3:
                    RenderUtil.drawBoxBlockPos(mc.objectMouseOver.getBlockPos(), 0, 0, 0, colorPicker.getColor(), RenderMode.Both);
                    break;
            }
        }
    }
}