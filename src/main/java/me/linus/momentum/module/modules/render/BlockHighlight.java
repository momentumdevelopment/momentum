package me.linus.momentum.module.modules.render;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.render.RenderUtil;
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
    public static SubSlider r = new SubSlider(color, "Red", 0.0D, 210.0D, 255.0D, 0);
    public static SubSlider g = new SubSlider(color, "Green", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider b = new SubSlider(color, "Blue", 0.0D, 255.0D, 255.0D, 0);
    public static SubSlider a = new SubSlider(color, "Alpha", 0.0D, 30.0D, 255.0D, 0);

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
                    RenderUtil.drawBoundingBoxBlockPos(mc.objectMouseOver.getBlockPos(), 0, new Color((int) r.getValue(), (int) g.getValue(), (int) b.getValue(), 144));
                    break;
                case 1:
                    RenderUtil.drawBoxBlockPos(mc.objectMouseOver.getBlockPos(), 0, new Color((int) r.getValue(), (int) g.getValue(), (int) b.getValue(), (int) a.getValue()));
                    break;
                case 2:
                    RenderUtil.drawBoundingBoxBlockPos(mc.objectMouseOver.getBlockPos(), 0, new Color((int) r.getValue(), (int) g.getValue(), (int) b.getValue(), 144));
                    RenderUtil.drawBoxBlockPos(mc.objectMouseOver.getBlockPos(), 0, new Color((int) r.getValue(), (int) g.getValue(), (int) b.getValue(), (int) a.getValue()));
                    break;
            }
        }
    }
}
