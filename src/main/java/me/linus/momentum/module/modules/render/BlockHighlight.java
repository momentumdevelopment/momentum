package me.linus.momentum.module.modules.render;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.color.SubColor;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.util.render.builder.RenderBuilder;
import me.linus.momentum.util.render.builder.RenderUtil;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class BlockHighlight extends Module {
    public BlockHighlight() {
        super("BlockHighlight", Category.RENDER, "Highlights the block you're facing");
    }

    public static Mode mode = new Mode("Mode", "Outline", "Fill", "Both");

    public static Checkbox color = new Checkbox("Color", true);
    public static SubColor colorPicker = new SubColor(color, new Color(255, 0, 255, 55));

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
                    RenderUtil.drawBoxBlockPos(mc.objectMouseOver.getBlockPos(), 0, new Color(colorPicker.getColor().getRed(), colorPicker.getColor().getGreen(), colorPicker.getColor().getBlue(), 144), RenderBuilder.renderMode.Outline);
                    break;
                case 1:
                    RenderUtil.drawBoxBlockPos(mc.objectMouseOver.getBlockPos(), 0, colorPicker.getColor(), RenderBuilder.renderMode.Fill);
                    break;
                case 2:
                    RenderUtil.drawBoxBlockPos(mc.objectMouseOver.getBlockPos(), 0, colorPicker.getColor(), RenderBuilder.renderMode.Both);
                    break;
            }
        }
    }
}