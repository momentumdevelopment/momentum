package me.linus.momentum.module.modules.render;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.client.color.ColorUtil;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author linustouchtips
 * @since 11/30/2020
 */

public class SkyColor extends Module {
    public SkyColor() {
        super("SkyColor", Category.RENDER, "Changes the color of the sky");
    }

    public static Checkbox color = new Checkbox("Color", true);
    public static SubSlider r = new SubSlider(color, "Red", 0.0D, 250.0D, 255.0D, 0);
    public static SubSlider g = new SubSlider(color, "Green", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider b = new SubSlider(color, "Blue", 0.0D, 0.0D, 255.0D, 0);
    public static SubCheckbox rainbow = new SubCheckbox(color, "Rainbow", false);

    @Override
    public void setup() {
        addSetting(color);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onFogColorRender(EntityViewRenderEvent.FogColors event) {
        if (rainbow.getValue()) {
            event.setRed(ColorUtil.staticRainbow().getRed() / 255f);
            event.setGreen(ColorUtil.staticRainbow().getBlue() / 255f);
            event.setBlue(ColorUtil.staticRainbow().getGreen() / 255f);
        }

        else {
            event.setRed((float) (r.getValue() / 255f));
            event.setGreen((float) (g.getValue() / 255f));
            event.setBlue((float) (b.getValue() / 255f));
        }
    }

    @SubscribeEvent
    public void fog(EntityViewRenderEvent.FogDensity event) {
        event.setDensity(0);
        event.setCanceled(true);
    }
}
