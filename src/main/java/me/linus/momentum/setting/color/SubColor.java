package me.linus.momentum.setting.color;

import me.linus.momentum.setting.Setting;
import me.linus.momentum.setting.SubSetting;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.keybind.Keybind;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;

import java.awt.*;

/**
 * @author linustouchtips
 * @since 01/03/2021
 */

public class SubColor extends SubSetting {

    private Setting parent;
    private Color color;
    private boolean opened;

    public SubColor(Setting parent, Color color) {
        this.parent = parent;
        this.color = color;
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

    public Color getColor() {
        return this.color;
    }

    public int getRed() {
        return this.color.getRed();
    }

    public int getGreen() {
        return this.color.getRed();
    }

    public int getBlue() {
        return this.color.getRed();
    }

    public int getAlpha() {
        return this.color.getAlpha();
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void toggleState() {
        this.opened = !this.opened;
    }

    public boolean isOpened() {
        return this.opened;
    }
}
