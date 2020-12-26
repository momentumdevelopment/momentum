package me.linus.momentum.setting.keybind;

import me.linus.momentum.setting.Setting;
import me.linus.momentum.setting.SubSetting;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;

public class SubKeybind extends SubSetting {

    private final Setting parent;
    private final String name;
    private int key;
    private boolean binding;

    public SubKeybind(Setting parent, String name, int key) {
        this.parent = parent;
        this.name = name;
        this.key = key;
        this.binding = false;

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

    public Setting getParent() {
        return this.parent;
    }
}
