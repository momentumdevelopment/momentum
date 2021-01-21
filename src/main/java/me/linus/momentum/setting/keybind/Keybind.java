package me.linus.momentum.setting.keybind;

import me.linus.momentum.setting.Setting;
import me.linus.momentum.setting.SubSetting;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linustouchtips
 * @since 12/25/2020
 */

public class Keybind extends Setting {

    private String name;
    private int key;
    private boolean opened;
    private boolean binding;

    private List<SubSetting> subs = new ArrayList<>();

    public Keybind(String name, int key) {
        this.name = name;
        this.key = key;
        this.opened = false;
        this.binding = false;
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

    public String getName() {
        return this.name;
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public boolean isBinding() {
        return this.binding;
    }

    public void setBinding(boolean in) {
        this.binding = in;
    }
}
