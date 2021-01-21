package me.linus.momentum.setting.checkbox;

import me.linus.momentum.setting.Setting;
import me.linus.momentum.setting.SubSetting;
import me.linus.momentum.setting.keybind.Keybind;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;

/**
 * @author bon
 * @since 11/16/20
 */

public class SubCheckbox extends SubSetting {

	private Setting parent;
	private String name;
	private boolean checked;

	public SubCheckbox(Setting parent, String name, boolean checked) {
		this.parent = parent;
		this.name = name;
		this.checked = checked;
		
		if (parent instanceof Checkbox) {
			Checkbox p = (Checkbox) parent;
			p.addSub(this);
		}

		else if (parent instanceof Mode) {
			Mode p = (Mode) parent;
			p.addSub(this);
		}

		else if (parent instanceof Slider) {
			Slider p = (Slider) parent;
			p.addSub(this);
		}

		else if (parent instanceof Keybind) {
			Keybind p = (Keybind) parent;
			p.addSub(this);
		}
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean getValue() {
		return this.checked;
	}

	public void setChecked(boolean newValue) {
		this.checked = newValue;
	}
	
	public void toggleValue() {
		this.checked = !this.checked;
	}
	
	public Setting getParent() {
		return this.parent;
	}
}
