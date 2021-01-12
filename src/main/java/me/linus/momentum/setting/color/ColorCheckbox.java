package me.linus.momentum.setting.color;

import me.linus.momentum.setting.Setting;
import me.linus.momentum.setting.SubSetting;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author linustouchtips
 * @since 01/12/2021
 */

public class ColorCheckbox extends Setting {

    private String name;
    private boolean checked;
    private boolean opened;
    private Color color;

    private List<SubSetting> subs = new ArrayList<>();

    public ColorCheckbox(String name, boolean checked, Color color) {
        this.name = name;
        this.checked = checked;
        this.opened = false;
        this.color = color;
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

    public boolean getChecked() {
        return this.checked;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setChecked(boolean newValue) {
        this.checked = newValue;
    }
}
