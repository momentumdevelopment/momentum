package me.linus.momentum.setting.slider;

import me.linus.momentum.setting.Setting;
import me.linus.momentum.setting.SubSetting;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.mode.Mode;

/**
 * @author bon
 * @since 11/16/20
 */

public class SubSlider extends SubSetting {
	
	private Setting parent;
	private String name;
	private double min;
	private double value;
	private double max;
	private int scale;

	public SubSlider(Setting parent, String name, double min, double value, double max, int scale) {
		this.parent = parent;
		this.name = name;
		this.min = min;
		this.value = scale == 0 ? (int) value : value;
		this.max = max;
		this.scale = scale;
		
		if (parent instanceof Checkbox) {
			Checkbox p = (Checkbox) parent;
			p.addSub(this);
		} else if(parent instanceof Mode) {
			Mode p = (Mode) parent;
			p.addSub(this);
		} else if(parent instanceof Slider) {
			Slider p = (Slider) parent;
			p.addSub(this);
		}
	}
	
	public String getName() {
		return this.name;
	}
	
	public Setting getParent() {
		return this.parent;
	}
	
	public int getRoundingScale() {
		return this.scale;
	}
	
	public double getValue() {
		return this.value;
	}
	
	public double getMaxValue() {
		return this.max;
	}
	
	public double getMinValue() {
		return this.min;
	}
	
	public void setValue(double value) {
		this.value = value;
	}
}