package me.linus.momentum.module.modules.render;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.color.SubColor;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 11/30/2020
 */

public class SkyColor extends Module {
    public SkyColor() {
        super("SkyColor", Category.RENDER, "Changes the color of the sky");
    }

    public static Checkbox color = new Checkbox("Color", true);
    public static SubColor colorPicker = new SubColor(color, new Color(255, 0, 0, 255));

    @Override
    public void setup() {
        addSetting(color);
    }

    @SubscribeEvent
    public void onFogRender(EntityViewRenderEvent.FogColors event) {
        event.setRed(colorPicker.getColor().getRed() / 255f);
        event.setGreen(colorPicker.getColor().getGreen() / 255f);
        event.setBlue(colorPicker.getColor().getBlue() / 255f);
    }

    @SubscribeEvent
    public void fog(EntityViewRenderEvent.FogDensity event) {
        event.setDensity(0);
        event.setCanceled(true);
    }
}
