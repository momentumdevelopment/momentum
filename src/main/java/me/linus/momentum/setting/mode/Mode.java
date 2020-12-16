package me.linus.momentum.setting.mode;

import me.linus.momentum.setting.Setting;
import me.linus.momentum.setting.SubSetting;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bon
 * @since 11/12/20
 */

public class Mode extends Setting {
	
	private final String name;
	private final String[] modes;
	private int mode;
	private boolean opened;
	
	private final List<SubSetting> subs = new ArrayList<>();
	
	public Mode(String name, String... modes) {
		this.name = name;
		this.modes = modes;
		this.opened = false; //gui
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
	
	public String getMode(int modeIndex) {
		return this.modes[modeIndex];
	}
	
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	public void toggleState() {
		this.opened = !this.opened;
	}
	
	public boolean isOpened() {
		return this.opened;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getValue() {
		return this.mode;
	}
	
	public int nextMode() {
		return this.mode + 1 >= this.modes.length ? 0 : this.mode + 1;
	}
}
