package me.linus.momentum.module.modules.client;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;

/**
 * @author bon & linustouchtips
 * @since 12/17/2020
 */

public class Colors extends Module {
	public Colors() {
		super("Colors", Category.CLIENT, "The client-wide color scheme.");
		this.enable();
	}
	
	public static Checkbox rainbow = new Checkbox("Rainbow", false);
	public static SubCheckbox gradient = new SubCheckbox(rainbow, "Gradient", true);
	public static SubSlider saturation = new SubSlider(rainbow, "Saturation", 0.0D, 0.8D, 1.0D, 2);
	public static SubSlider brightness = new SubSlider(rainbow, "Brightness", 0.0D, 0.8D, 1.0D, 2);
	public static SubSlider difference = new SubSlider(rainbow, "Difference", 1.0D, 30.0D, 100.0D, 0);
	public static SubSlider speed = new SubSlider(rainbow, "Speed", 1.0D, 30.0D, 100.0D, 0);
	
	public static Slider r = new Slider("Red", 0.0D, 220.0D, 255.0D, 0);
	public static Slider g = new Slider("Green", 0.0D, 20.0D, 255.0D, 0);
	public static Slider b = new Slider("Blue", 0.0D, 220.0D, 255.0D, 0);
	public static Slider a = new Slider("Alpha", 0.0D, 130.0D, 255.0D, 0);
	
	@Override
	public void setup() {
		addSetting(rainbow);
		addSetting(r);
		addSetting(g);
		addSetting(b);
		addSetting(a);
	}

	@Override
	public void onDisable() {
		this.enable();
	}
}
