package me.linus.momentum.gui.click.component;

import me.linus.momentum.managers.AnimationManager;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.modules.client.ClickGUI;
import me.linus.momentum.gui.click.Frame;
import me.linus.momentum.setting.Setting;
import me.linus.momentum.setting.SubSetting;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.color.ColorPicker;
import me.linus.momentum.setting.keybind.Keybind;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.client.color.ThemeColor;
import me.linus.momentum.util.render.FontUtil;
import me.linus.momentum.util.render.GUIUtil;
import me.linus.momentum.util.render.Render2DUtil;
import me.linus.momentum.util.render.builder.Render2DBuilder.Render2DMode;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linustouchtips & bon
 * @since 03/11/21
 */

public class SubComponent implements MixinInterface {

    public Frame frame;
    public Component component;
    public Setting setting;
    public List<RootComponent> rootComponents = new ArrayList<>();
    public AnimationManager animationManager;
    public AnimationManager animationComponentManager;

    public boolean binding = false;

    public SubComponent(Frame frame, Component component, Setting setting) {
        this.frame = frame;
        this.component = component;
        this.setting = setting;
        this.animationManager = new AnimationManager((int) ClickGUI.animationSpeed.getValue(), false);

        if (setting instanceof Checkbox) {
            if (((Checkbox) setting).hasSubSettings()) {
                for (SubSetting subSetting : ((Checkbox) setting).getSubSettings())
                    rootComponents.add(new RootComponent(frame, this, subSetting));
            }

            this.animationComponentManager = new AnimationManager(200, ((Checkbox) setting).getValue());
        }

        if (setting instanceof Mode) {
            if (((Mode) setting).hasSubSettings()) {
                for (SubSetting subSetting : ((Mode) setting).getSubSettings())
                    rootComponents.add(new RootComponent(frame, this, subSetting));
            }
        }

        if (setting instanceof Slider) {
            if (((Slider) setting).hasSubSettings()) {
                for (SubSetting subSetting : ((Slider) setting).getSubSettings())
                    rootComponents.add(new RootComponent(frame, this, subSetting));
            }
        }

        if (setting instanceof Keybind) {
            if (((Keybind) setting).hasSubSettings()) {
                for (SubSetting subSetting : ((Keybind) setting).getSubSettings())
                    rootComponents.add(new RootComponent(frame, this, subSetting));
            }
        }
    }

    public void drawSubComponent(int x, int y, int height, int width) {
        if (setting instanceof Checkbox) {
            double preOffset = frame.offset;

            if (animationManager.getAnimationFactor() > 0.05) {
                for (RootComponent rootComponent : rootComponents) {
                    frame.offset += MathUtil.clamp((float) animationManager.getAnimationFactor(), 0, 1);

                    rootComponent.drawRootComponent(x, y, height, width);

                    if (rootComponent.subsetting instanceof ColorPicker)
                        frame.offset += MathUtil.clamp((float) (9 * animationManager.getAnimationFactor()), 0, 9);
                }
            }

            int color = 0xCC232323;
            if (GUIUtil.mouseOver(x + 4, (int) (y + height + (preOffset * height) + 2), (x + width) - 1, (int) ((y + height) + height + (preOffset * height)))) {
                color = 0xCC383838;

                if (GUIUtil.ldown) {
                    ((Checkbox) setting).toggleValue();
                    animationComponentManager.setState(((Checkbox) setting).getValue());

                    if (ClickGUI.sounds.getValue())
                        mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, (float) ClickGUI.pitch.getValue()));
                }

                if (GUIUtil.rdown) {
                    ((Checkbox) setting).toggleState();
                    animationManager.setState(((Checkbox) setting).isOpened());

                    if (ClickGUI.sounds.getValue())
                        mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, (float) ClickGUI.pitch.getValue()));
                }
            }

            Render2DUtil.drawRect(x, y + height + (preOffset * height), x + width, y + height * 2 + (preOffset * height), 0, 0x99202020, -1, false, Render2DMode.Normal);
            Render2DUtil.drawRect(x + 4, y + height + (preOffset * height), x + width, (y + height) + height + (preOffset * height), 0, color, -1, false, Render2DMode.Normal);
            Render2DUtil.drawRect(x + 4, y + height + (preOffset * height), (x + (animationComponentManager.getAnimationFactor() <= 0.05 ? 4 : 0) + (width * MathUtil.clamp((float) animationComponentManager.getAnimationFactor(), 0, 1))), (y + height) + height + (preOffset * height), 0, ThemeColor.COLOR, -1, false, Render2DMode.Normal);
            FontUtil.drawString(((Checkbox) setting).getName(), x + 7, (float) ((y + height) + 4 + (preOffset * height)), -1);

            if (((Checkbox) setting).hasSubSettings() && ClickGUI.indicators.getValue())
                FontUtil.drawString("...", x + width - 12, (float) (y + 1 + height + (preOffset * height)), -1);
        }

        else if (setting instanceof Mode) {
            double preOffset = frame.offset;

            if (animationManager.getAnimationFactor() > 0.05) {
                for (RootComponent rootComponent : rootComponents) {
                    frame.offset += MathUtil.clamp((float) animationManager.getAnimationFactor(), 0, 1);

                    rootComponent.drawRootComponent(x, y, height, width);

                    if (rootComponent.subsetting instanceof ColorPicker)
                        frame.offset += MathUtil.clamp((float) (9 * animationManager.getAnimationFactor()), 0, 9);
                }
            }

            int color = 0xCC232323;
            if (GUIUtil.mouseOver(x + 4, (int) (y + height + (preOffset * height) + 2), (x + width) - 1, (int) ((y + height) + height + (preOffset * height)))) {
                color = 0xCC383838;

                if (GUIUtil.ldown) {
                    ((Mode) setting).setMode(((Mode) setting).nextMode());
                    component.module.onValueChange();
                }

                if (GUIUtil.rdown) {
                    ((Mode) setting).toggleState();
                    animationManager.setState(((Mode) setting).isOpened());

                    if (ClickGUI.sounds.getValue())
                        mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, (float) ClickGUI.pitch.getValue()));
                }
            }

            Render2DUtil.drawRect(x, y + height + (preOffset * height), x + width, y + height * 2 + (preOffset * height), 0, 0x99202020, -1, false, Render2DMode.Normal);
            Render2DUtil.drawRect(x + 4, y + height + (preOffset * height), (x + width) - 1, (y + height) + height + (preOffset * height), 0, color, -1, false, Render2DMode.Normal);
            FontUtil.drawString(((Mode) setting).getName() + ": " + ((Mode) setting).getMode(((Mode) setting).getValue()), x + 7, (float) ((y + height) + 4 + (preOffset * height)), -1);

            if (((Mode) setting).hasSubSettings() && ClickGUI.indicators.getValue())
                FontUtil.drawString("...", x + width - 12, (float) (y + 1 + height + (preOffset * height)), -1);
        }

        else if (setting instanceof Slider) {
            double preOffset = frame.offset;

            if (animationManager.getAnimationFactor() > 0.05) {
                for (RootComponent rootComponent : rootComponents) {
                    frame.offset += MathUtil.clamp((float) animationManager.getAnimationFactor(), 0, 1);

                    rootComponent.drawRootComponent(x, y, height, width);

                    if (rootComponent.subsetting instanceof ColorPicker)
                        frame.offset += MathUtil.clamp((float) (9 * animationManager.getAnimationFactor()), 0, 9);
                }
            }

            int color = 0xCC232323;
            int rectAdd = (int) MathHelper.clamp((((x - 2) + width) - (x + 3)) * ((((Slider) setting).getValue() - ((Slider) setting).getMinValue()) / (((Slider) setting).getMaxValue() - ((Slider) setting).getMinValue())), 0.0D, (((x + 3) + width) - (x)));

            if (GUIUtil.mouseOver(x + 4, (int) (y + height + (preOffset * height) + 2), (x + width), (int) ((y + height) + height + (preOffset * height)))) {
                color = 0xCC383838;
                if (GUIUtil.lheld) {
                    int percentError = (GUIUtil.mX - (x + 4)) * 100 / (((x) + width) - (x + 4));
                    ((Slider) setting).setValue(MathUtil.roundDouble(percentError * ((((Slider) setting).getMaxValue() - ((Slider) setting).getMinValue()) / 100.0D) + ((Slider) setting).getMinValue(), ((Slider) setting).getRoundingScale()));
                }

                if (GUIUtil.rdown) {
                    animationManager.setState(((Slider) setting).isOpened());

                    if (ClickGUI.sounds.getValue())
                        mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, (float) ClickGUI.pitch.getValue()));
                }
            }

            if (GUIUtil.mouseOver(x + 4, (int) (y + height + (preOffset * height) + 2), (int) (x + ClickGUI.snapSub.getValue()), (int) ((y + height) + height + (preOffset * height)))) {
                if (ClickGUI.snapSlider.getValue() && GUIUtil.lheld) {
                    rectAdd = 0;
                    ((Slider) setting).setValue(((Slider) setting).getMinValue());
                }
            }

            if (GUIUtil.mouseOver((int) ((x + 4 + width) - ClickGUI.snapSub.getValue()), (int) (y + height + (preOffset * height) + 2), (x + width) - 1, (int) ((y + height) + height + (preOffset * height)))) {
                if (ClickGUI.snapSlider.getValue() && GUIUtil.lheld) {
                    rectAdd = ((x + 4 + width) - (x + 4));
                    ((Slider) setting).setValue(((Slider) setting).getMaxValue());
                }
            }

            Render2DUtil.drawRect(x, y + height + (preOffset * height), x + width, y + height * 2 + (preOffset * height), 0, 0x99202020, -1, false, Render2DMode.Normal);
            Render2DUtil.drawRect(x + 4, y + height + (preOffset * height), (x + width) - 1, (y + height) + height + (preOffset * height), 0, color, -1, false, Render2DMode.Normal);
            Render2DUtil.drawRect(x + 4, y + height + (preOffset * height), x + 4 + (rectAdd > width - 1 ? (width - 5) : rectAdd), (y + height) + height + (preOffset * height), 0, ThemeColor.COLOR, -1, false, Render2DMode.Normal);
            FontUtil.drawString(((Slider) setting).getName() + " " + ((Slider) setting).getValue(), x + 6, (int) ((y + height) + 4 + (preOffset * height)), -1);

            if (((Slider) setting).hasSubSettings() && ClickGUI.indicators.getValue())
                FontUtil.drawString("...", x + width - 12, (float) (y + 1 + height + (preOffset * height)), -1);
        }

        else if (setting instanceof Keybind) {
            double preOffset = frame.offset;

            if (animationManager.getAnimationFactor() > 0.05) {
                for (RootComponent rootComponent : rootComponents) {
                    frame.offset += MathUtil.clamp((float) animationManager.getAnimationFactor(), 0, 1);

                    rootComponent.drawRootComponent(x, y, height, width);

                    if (rootComponent.subsetting instanceof ColorPicker)
                        frame.offset += MathUtil.clamp((float) (9 * animationManager.getAnimationFactor()), 0, 9);
                }
            }

            int color = 0xCC232323;
            if (GUIUtil.mouseOver(x + 4, (int) (y + height + (preOffset * height) + 2), (x + width) - 1, (int) ((y + height) + height + (preOffset * height)))) {
                color = 0xCC383838;
                if (GUIUtil.ldown)
                    binding = !binding;

                if (GUIUtil.rdown) {
                    animationManager.setState(((Keybind) setting).isOpened());

                    if (ClickGUI.sounds.getValue())
                        mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, (float) ClickGUI.pitch.getValue()));
                }
            }

            if (binding && ((Keybind) setting).getKey() != -1 && ((Keybind) setting).getKey() != Keyboard.KEY_ESCAPE && ((Keybind) setting).getKey() != Keyboard.KEY_DELETE) {
                ((Keybind) setting).setKey(((Keybind) setting).getKey() == Keyboard.KEY_BACK ? Keyboard.KEY_NONE : ((Keybind) setting).getKey());
                ((Keybind) setting).setBinding(false);
            }

            if (binding && ((Keybind) setting).getKey() == Keyboard.KEY_ESCAPE)
                ((Keybind) setting).setBinding(false);

            Render2DUtil.drawRect(x, y + height + (preOffset * height), x + width, y + height * 2 + (preOffset * height), 0, 0x99202020, -1, false, Render2DMode.Normal);
            Render2DUtil.drawRect(x + 4, y + height + (preOffset * height), (x + width) - 1, (y + height) + height + (preOffset * height), 0, color, -1, false, Render2DMode.Normal);

            if (!binding)
                FontUtil.drawString(((Keybind) setting).getName() + ": " + (((Keybind) setting).getKey() == -2 ? "None" : Keyboard.getKeyName(((Keybind) setting).getKey())), x + 7, (float) ((y + height) + 4 + (preOffset * height)), -1);
            else
                FontUtil.drawString("Listening ...", x + 7, (int) ((y + height) + 4 + (preOffset * height)), -1);

            if (((Keybind) setting).hasSubSettings() && ClickGUI.indicators.getValue())
                FontUtil.drawString("...", x + width - 12, (float) (y + 1 + height + (preOffset * height)), -1);
        }
    }
}
