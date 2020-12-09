package me.linus.momentum.setting.mode;

import me.linus.momentum.setting.Setting;
import me.linus.momentum.setting.SubSetting;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.slider.Slider;

/**
 * @author bon
 * @since 11/16/20
 */

public class SubMode extends SubSetting {
	
	private Setting parent;
	private String name;
	private String[] modes;
	private int mode;

	public SubMode(Setting parent, String name, String... modes) {
		this.parent = parent;
		this.name = name;
		this.modes = modes;
		
		if(parent instanceof Checkbox) {
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
	
	public String getMode(int modeIndex) {
		return this.modes[modeIndex];
	}
	
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	public int getValue() {
		return this.mode;
	}
	
	public int nextMode() {
		return this.mode + 1 >= this.modes.length ? 0 : this.mode + 1;
	}
}
