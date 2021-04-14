package me.linus.momentum.gui.hud.component;

import me.linus.momentum.managers.AnimationManager;
import me.linus.momentum.module.modules.client.ClickGUI;
import me.linus.momentum.gui.hud.HUDFrame;
import me.linus.momentum.setting.Setting;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.keybind.Keybind;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.client.color.ThemeColor;
import me.linus.momentum.util.render.FontUtil;
import me.linus.momentum.util.render.GUIUtil;
import me.linus.momentum.util.render.Render2DUtil;
import me.linus.momentum.util.render.builder.Render2DBuilder.Render2DMode;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;

/**
 * @author linustouchtips & bon
 * @since 03/11/21
 */

public class HUDSubComponent {

    public Setting setting;
    public HUDFrame frame;
    public AnimationManager animationComponentManager;

    public HUDSubComponent(HUDFrame frame, Setting setting) {
        this.frame = frame;
        this.setting = setting;

        if (setting instanceof Checkbox)
            this.animationComponentManager = new AnimationManager(200, ((Checkbox) setting).getValue());
    }

    public void drawSubComponent(int x, int y, int height, int width) {
        if (setting instanceof Checkbox) {
            int color = 0xCC232323;
            if (GUIUtil.mouseOver(x + 4, (int) (y + height + (frame.offset * height) + 2), (x + width) - 1, (int) ((y + height) + height + (frame.offset * height)))) {
                color = 0xCC383838;
                if (GUIUtil.ldown) {
                    ((Checkbox) setting).toggleValue();
                    animationComponentManager.setState(((Checkbox) setting).getValue());
                }

                if (GUIUtil.rdown)
                    ((Checkbox) setting).toggleState();
            }

            Render2DUtil.drawRect(x, y + height + (frame.offset * height), x + width, y + height * 2 + (frame.offset * height), 0, 0x99202020, -1, false, Render2DMode.Normal);
            Render2DUtil.drawRect(x + 4, y + height + (frame.offset * height), x + width, (y + height) + height + (frame.offset * height), 0, color, -1, false, Render2DMode.Normal);
            Render2DUtil.drawRect(x + 4, y + height + (frame.offset * height), (x + (animationComponentManager.getAnimationFactor() <= 0.05 ? 4 : 0) + (width * MathUtil.clamp((float) animationComponentManager.getAnimationFactor(), 0, 1))), (y + height) + height + (frame.offset * height), 0, ThemeColor.COLOR, -1, false, Render2DMode.Normal);
            FontUtil.drawString(((Checkbox) setting).getName(), x + 7, (float) ((y + height) + 4 + (frame.offset * height)), -1);
        }

        else if (setting instanceof Mode) {
            int color = 0xCC232323;
            if (GUIUtil.mouseOver(x + 4, (int) (y + height + (frame.offset * height) + 2), (x + width) - 1, (int) ((y + height) + height + (frame.offset * height)))) {
                color = 0xCC383838;

                if (GUIUtil.ldown)
                    ((Mode) setting).setMode(((Mode) setting).nextMode());

                if (GUIUtil.rdown)
                    ((Mode) setting).toggleState();
            }

            Render2DUtil.drawRect(x, y + height + (frame.offset * height), x + width, y + height * 2 + (frame.offset * height), 0, 0x99202020, -1, false, Render2DMode.Normal);
            Render2DUtil.drawRect(x + 4, y + height + (frame.offset * height), (x + width) - 1, (y + height) + height + (frame.offset * height), 0, color, -1, false, Render2DMode.Normal);
            FontUtil.drawString(((Mode) setting).getName() + ": " + ((Mode) setting).getMode(((Mode) setting).getValue()), x + 7, (float) ((y + height) + 4 + (frame.offset * height)), -1);
        }

        else if (setting instanceof Slider) {
            int color = 0xCC232323;
            int rectAdd = (int) MathHelper.clamp((((x - 2) + width) - (x + 3)) * ((((Slider) setting).getValue() - ((Slider) setting).getMinValue()) / (((Slider) setting).getMaxValue() - ((Slider) setting).getMinValue())), 0.0D, (((x + 3) + width) - (x)));

            if (GUIUtil.mouseOver(x + 4, (int) (y + height + (frame.offset * height) + 2), (x + width), (int) ((y + height) + height + (frame.offset * height)))) {
                color = 0xCC383838;
                if (GUIUtil.lheld) {
                    int percentError = (GUIUtil.mX - (x + 4)) * 100 / (((x) + width) - (x + 4));
                    ((Slider) setting).setValue(MathUtil.roundDouble(percentError * ((((Slider) setting).getMaxValue() - ((Slider) setting).getMinValue()) / 100.0D) + ((Slider) setting).getMinValue(), ((Slider) setting).getRoundingScale()));
                }
            }

            if (GUIUtil.mouseOver(x + 4, (int) (y + height + (frame.offset * height) + 2), (int) (x + ClickGUI.snapSub.getValue()), (int) ((y + height) + height + (frame.offset * height)))) {
                if (ClickGUI.snapSlider.getValue() && GUIUtil.lheld) {
                    rectAdd = 0;
                    ((Slider) setting).setValue(((Slider) setting).getMinValue());
                }
            }

            if (GUIUtil.mouseOver((int) ((x + 4 + width) - ClickGUI.snapSub.getValue()), (int) (y + height + (frame.offset * height) + 2), (x + width) - 1, (int) ((y + height) + height + (frame.offset * height)))) {
                if (ClickGUI.snapSlider.getValue() && GUIUtil.lheld) {
                    rectAdd = ((x + 4 + width) - (x + 4));
                    ((Slider) setting).setValue(((Slider) setting).getMaxValue());
                }
            }

            Render2DUtil.drawRect(x, y + height + (frame.offset * height), x + width, y + height * 2 + (frame.offset * height), 0, 0x99202020, -1, false, Render2DMode.Normal);
            Render2DUtil.drawRect(x + 4, y + height + (frame.offset * height), (x + width) - 1, (y + height) + height + (frame.offset * height), 0, color, -1, false, Render2DMode.Normal);
            Render2DUtil.drawRect(x + 4, y + height + (frame.offset * height), x + 4 + (rectAdd > width - 1 ? (width - 5) : rectAdd), (y + height) + height + (frame.offset * height), 0, ThemeColor.COLOR, -1, false, Render2DMode.Normal);
            FontUtil.drawString(((Slider) setting).getName() + " " + ((Slider) setting).getValue(), x + 6, (int) ((y + height) + 4 + (frame.offset * height)), -1);
        }

        else if (setting instanceof Keybind) {
            int color = 0xCC232323;
            if (GUIUtil.mouseOver(x + 4, (int) (y + height + (frame.offset * height) + 2), (x + width) - 1, (int) ((y + height) + height + (frame.offset * height)))) {
                color = 0xCC383838;
                if (GUIUtil.ldown)
                    ((Keybind) setting).setBinding(true);
            }

            if (((Keybind) setting).isBinding() && ((Keybind) setting).getKey() != -1 && ((Keybind) setting).getKey() != Keyboard.KEY_ESCAPE && ((Keybind) setting).getKey() != Keyboard.KEY_DELETE) {
                ((Keybind) setting).setKey(((Keybind) setting).getKey() == Keyboard.KEY_BACK ? Keyboard.KEY_NONE : ((Keybind) setting).getKey());
                ((Keybind) setting).setBinding(false);
            }

            if (((Keybind) setting).isBinding() && ((Keybind) setting).getKey() == Keyboard.KEY_ESCAPE)
                ((Keybind) setting).setBinding(false);

            Render2DUtil.drawRect(x, y + height + (frame.offset * height), x + width, y + height * 2 + (frame.offset * height), 0, 0x99202020, -1, false, Render2DMode.Normal);
            Render2DUtil.drawRect(x + 4, y + height + (frame.offset * height), (x + width) - 1, (y + height) + height + (frame.offset * height), 0, color, -1, false, Render2DMode.Normal);

            if (!((Keybind) setting).isBinding())
                FontUtil.drawString(((Keybind) setting).getName() + ": " + (((Keybind) setting).getKey() == -2 ? "None" : Keyboard.getKeyName(((Keybind) setting).getKey())), x + 7, (float) ((y + height) + 4 + (frame.offset * height)), -1);
            else
                FontUtil.drawString("Listening ...", x + 7, (int) ((y + height) + 4 + (frame.offset * height)), -1);
        }
    }
}
