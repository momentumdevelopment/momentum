package me.linus.momentum.setting.checkbox;

import me.linus.momentum.setting.Setting;
import me.linus.momentum.setting.SubSetting;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bon
 * @since 11/12/20
 */

public class Checkbox extends Setting {
	
	private String name;
	private boolean checked;
	private boolean opened;
	
	private List<SubSetting> subs = new ArrayList<>();
	
	public Checkbox(String name, boolean checked) {
		this.name = name;
		this.checked = checked;
		this.opened = false;
	}
	
	public List<SubSetting> getSubSettings(){
		return this.subs;
	}
	
	public boolean hasSubSettings() {
		return this.subs.size() > 0;
	}
	
	public void addSub(SubSetting s) {
		this.subs.add(s);
	}
	
	public void toggleState() {
		this.opened = !this.opened;
	}
	
	public boolean isOpened() {
		return this.opened;
	}
	
	public void toggleValue() {
		this.checked = !this.checked;
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
}
