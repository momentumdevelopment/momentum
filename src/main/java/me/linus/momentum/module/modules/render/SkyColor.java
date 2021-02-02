package me.linus.momentum.module.modules.render;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.color.ColorPicker;
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

    public static Checkbox color = new Checkbox("Sky", true);
    public static ColorPicker skyPicker = new ColorPicker(color, new Color(255, 0, 0, 255));

    public static Checkbox fog = new Checkbox("Fog", false);
    public static ColorPicker fogPicker = new ColorPicker(fog, new Color(255, 0, 0, 255));
    public static SubCheckbox fogCancel = new SubCheckbox(fog, "No Fog", true);

    @Override
    public void setup() {
        addSetting(color);
        addSetting(fog);
    }

    @SubscribeEvent
    public void onFogRender(EntityViewRenderEvent.FogColors event) {
        if (fog.getValue()) {
            event.setRed(fogPicker.getColor().getRed() / 255f);
            event.setGreen(fogPicker.getColor().getGreen() / 255f);
            event.setBlue(fogPicker.getColor().getBlue() / 255f);
        }
    }

    @SubscribeEvent
    public void fog(EntityViewRenderEvent.FogDensity event) {
        if (fog.getValue() && fogCancel.getValue()) {
            event.setDensity(0);
            event.setCanceled(true);
        }
    }
}