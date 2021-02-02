package me.linus.momentum.module.modules.client;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.color.ColorPicker;
import me.linus.momentum.setting.slider.SubSlider;

import java.awt.*;

/**
 * @author bon & linustouchtips
 * @since 12/17/2020
 */

public class Colors extends Module {
	public Colors() {
		super("Colors", Category.CLIENT, "The client-wide color scheme.");
		this.enable();
		this.setDrawn(false);
	}

	public static Checkbox rainbow = new Checkbox("Rainbow", false);
	public static SubCheckbox gradient = new SubCheckbox(rainbow, "Gradient", true);
	public static SubSlider saturation = new SubSlider(rainbow, "Saturation", 0.0D, 0.8D, 1.0D, 2);
	public static SubSlider brightness = new SubSlider(rainbow, "Brightness", 0.0D, 0.8D, 1.0D, 2);
	public static SubSlider difference = new SubSlider(rainbow, "Difference", 1.0D, 30.0D, 100.0D, 0);
	public static SubSlider speed = new SubSlider(rainbow, "Speed", 1.0D, 30.0D, 100.0D, 0);

	public static Checkbox clientColor = new Checkbox("Color", true);
	public static ColorPicker clientPicker = new ColorPicker(clientColor, "Color Picker", new Color(48, 191, 215, 130));

	@Override
	public void setup() {
		addSetting(rainbow);
		addSetting(clientColor);
	}

	@Override
	public void onDisable() {
		this.enable();
	}
}
