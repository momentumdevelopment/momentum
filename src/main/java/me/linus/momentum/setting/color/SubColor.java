package me.linus.momentum.setting.color;

import me.linus.momentum.setting.Setting;
import me.linus.momentum.setting.SubSetting;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.keybind.Keybind;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;

/**
 * @author linustouchtips
 * @since 01/03/2021
 */

public class SubColor extends SubSetting {

    private Setting parent;
    private String name;
    private double red;
    private double green;
    private double blue;
    private int alpha;
    private boolean opened;

    public SubColor(Setting parent, String name, double red, double green, double blue, int alpha) {
        this.name = name;
        this.parent = parent;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        this.opened = false;

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

    public Setting getParent() {
        return this.parent;
    }

    public String getName() {
        return this.name;
    }

    public double getRed() {
        return this.red;
    }

    public double getGreen() {
        return this.green;
    }

    public double getBlue() {
        return this.blue;
    }

    public int getAlpha() {
        return this.alpha;
    }

    public void setColor(int red, int green, int blue, int alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public void toggleState() {
        this.opened = !this.opened;
    }

    public boolean isOpened() {
        return this.opened;
    }
}
