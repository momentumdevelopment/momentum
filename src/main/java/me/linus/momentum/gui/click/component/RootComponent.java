package me.linus.momentum.gui.click.component;

import me.linus.momentum.managers.AnimationManager;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.modules.client.ClickGUI;
import me.linus.momentum.gui.click.Frame;
import me.linus.momentum.setting.SubSetting;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.color.ColorPicker;
import me.linus.momentum.setting.keybind.SubKeybind;
import me.linus.momentum.setting.mode.SubMode;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.client.color.ColorUtil;
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

import java.awt.*;

/**
 * @author linustouchtips & bon
 * @since 03/11/21
 */

public class RootComponent implements MixinInterface {

    public Frame frame;
    public SubComponent subComponent;
    public SubSetting subsetting;

    public Color finalColor;
    public AnimationManager animationComponentManager;

    public boolean binding = false;

    public RootComponent(Frame frame, SubComponent subComponent, SubSetting subsetting) {
        this.frame = frame;
        this.subsetting = subsetting;
        this.subComponent = subComponent;

        if (subsetting instanceof SubCheckbox)
            this.animationComponentManager = new AnimationManager(200, ((SubCheckbox) subsetting).getValue());
    }

    public void drawRootComponent(int x, int y, int height, int width) {
        if (subsetting instanceof SubCheckbox) {
            int color = 0xCC232323;
            if (GUIUtil.mouseOver(x + 8, (int) (y + height + (frame.offset * height) + 2), (x + width) - 1, (int) ((y + height) + height + (frame.offset * height)))) {
                color = 0xCC383838;

                if (GUIUtil.ldown) {
                    ((SubCheckbox) subsetting).toggleValue();
                    animationComponentManager.setState(((SubCheckbox) subsetting).getValue());

                    if (ClickGUI.sounds.getValue())
                        mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, (float) ClickGUI.pitch.getValue()));
                }
            }

            Render2DUtil.drawRect(x, y + height + (frame.offset * height), x + width, y + height * 2 + (frame.offset * height), 0, 0x99202020, -1, false, Render2DMode.Normal);
            Render2DUtil.drawRect(x + 8, y + height + (frame.offset * height), x + width, (y + height) + height + (frame.offset * height), 0, color, -1, false, Render2DMode.Normal);
            Render2DUtil.drawRect(x + 8, y + height + (frame.offset * height), (x + (animationComponentManager.getAnimationFactor() <= 0.05 ? 8 : 0) + (width * MathUtil.clamp((float) animationComponentManager.getAnimationFactor(), 0, 1))), (y + height) + height + (frame.offset * height), 0, ThemeColor.COLOR, -1, false, Render2DMode.Normal);
            FontUtil.drawString(((SubCheckbox) subsetting).getName(), x + 10, (float) ((y + height) + 4 + (frame.offset * height)), -1);
        }

        if (subsetting instanceof SubMode) {
            int color = 0xCC232323;

            if (GUIUtil.mouseOver(x + 8, (int) (y + height + (frame.offset * height) + 2), (x + width) - 1, (int) ((y + height) + height + (frame.offset * height)))) {
                color = 0xCC383838;

                if (GUIUtil.ldown)
                    ((SubMode) subsetting).setMode(((SubMode) subsetting).nextMode());
            }

            Render2DUtil.drawRect(x, y + height + (frame.offset * height), x + width, y + height * 2 + (frame.offset * height), 0, 0x99202020, -1, false, Render2DMode.Normal);
            Render2DUtil.drawRect(x + 8, y + height + (frame.offset * height), (x + width) - 1, (y + height) + height + (frame.offset * height), 0, color, -1, false, Render2DMode.Normal);
            FontUtil.drawString(((SubMode) subsetting).getName() + ": " + ((SubMode) subsetting).getMode(((SubMode) subsetting).getValue()), x + 12, (float) ((y + height) + 4 + (frame.offset * height)), -1);
        }

        if (subsetting instanceof SubSlider) {
            int color = 0xCC232323;
            int rectAdd = (int) MathHelper.clamp((((x - 2) + width) - (x + 7)) * ((((SubSlider) subsetting).getValue() - ((SubSlider) subsetting).getMinValue()) / (((SubSlider) subsetting).getMaxValue() - ((SubSlider) subsetting).getMinValue())), 0.0D, (((x + 7) + width) - (x)));

            if (GUIUtil.mouseOver(x + 8, (int) (y + height + (frame.offset * height) + 2), (x + width), (int) ((y + height) + height + (frame.offset * height)))) {
                color = 0xCC383838;
                if (GUIUtil.lheld) {
                    int percentError = (GUIUtil.mX - (x + 8)) * 100 / (((x) + width) - (x + 8));
                    ((SubSlider) subsetting).setValue(MathUtil.roundDouble(percentError * ((((SubSlider) subsetting).getMaxValue() - ((SubSlider) subsetting).getMinValue()) / 100.0D) + ((SubSlider) subsetting).getMinValue(), ((SubSlider) subsetting).getRoundingScale()));
                }
            }

            if (GUIUtil.mouseOver(x + 8, (int) (y + height + (frame.offset * height) + 2), (int) (x + ClickGUI.snapSub.getValue()), (int) ((y + height) + height + (frame.offset * height)))) {
                if (ClickGUI.snapSlider.getValue() && GUIUtil.lheld) {
                    rectAdd = 0;
                    ((SubSlider) subsetting).setValue(((SubSlider) subsetting).getMinValue());
                }
            }

            if (GUIUtil.mouseOver((int) ((x + 8 + width) - ClickGUI.snapSub.getValue()), (int) (y + height + (frame.offset * height) + 2), (x + width) - 1, (int) ((y + height) + height + (frame.offset * height)))) {
                if (ClickGUI.snapSlider.getValue() && GUIUtil.lheld) {
                    rectAdd = ((x + 8 + width) - (x + 8));
                    ((SubSlider) subsetting).setValue(((SubSlider) subsetting).getMaxValue());
                }
            }

            Render2DUtil.drawRect(x, y + height + (frame.offset * height), x + width, y + height * 2 + (frame.offset * height), 0, 0x99202020, -1, false, Render2DMode.Normal);
            Render2DUtil.drawRect(x + 8, y + height + (frame.offset * height), (x + width) - 1, (y + height) + height + (frame.offset * height), 0, color, -1, false, Render2DMode.Normal);
            Render2DUtil.drawRect(x + 8, y + height + (frame.offset * height), x + 8 + (rectAdd > width ? (width - 8) : rectAdd), (y + height) + height + (frame.offset * height), 0, ThemeColor.COLOR, -1, false, Render2DMode.Normal);
            FontUtil.drawString(((SubSlider) subsetting).getName() + " " + ((SubSlider) subsetting).getValue(), x + 10, (float) ((y + height) + 4 + (frame.offset * height)), -1);
        }
        
        if (subsetting instanceof SubKeybind) {
            int color = 0xCC232323;
            if (GUIUtil.mouseOver(x + 4, (int) (y + height + (frame.offset * height) + 2), (x + width) - 1, (int) ((y + height) + height + (frame.offset * height)))) {
                color = 0xCC383838;
                if (GUIUtil.ldown)
                    binding = !binding;
            }

            if (binding && ((SubKeybind) subsetting).getKey() != -1 && ((SubKeybind) subsetting).getKey() != Keyboard.KEY_ESCAPE && ((SubKeybind) subsetting).getKey() != Keyboard.KEY_DELETE) {
                ((SubKeybind) subsetting).setKey(((SubKeybind) subsetting).getKey() == Keyboard.KEY_BACK ? Keyboard.KEY_NONE : ((SubKeybind) subsetting).getKey());
                ((SubKeybind) subsetting).setBinding(false);
            }

            if (binding && ((SubKeybind) subsetting).getKey() == Keyboard.KEY_ESCAPE)
                ((SubKeybind) subsetting).setBinding(false);

            Render2DUtil.drawRect(x, y + height + (frame.offset * height), x + width, y + height * 2 + (frame.offset * height), 0, 0x99202020, -1, false, Render2DMode.Normal);
            Render2DUtil.drawRect(x + 4, y + height + (frame.offset * height), (x + width) - 1, (y + height) + height + (frame.offset * height), 0, color, -1, false, Render2DMode.Normal);

            if (!binding)
                FontUtil.drawString(((SubKeybind) subsetting).getName() + ": " + (((SubKeybind) subsetting).getKey() == -2 ? "None" : Keyboard.getKeyName(((SubKeybind) subsetting).getKey())), x + 7, (float) ((y + height) + 4 + (frame.offset * height)), -1);
            else
                FontUtil.drawString("Listening...", x + 7, (float) ((y + height) + 4 + (frame.offset * height)), -1);
        }
        
        if (subsetting instanceof ColorPicker) {
            drawPicker(((ColorPicker) subsetting), x + 3, (int) (y + height + (frame.offset * height) + 2), x + 3, (int) (y + height + (frame.offset * height) + 105), x + 3, (int) (y + height + (frame.offset * height) + 124));
            ((ColorPicker) subsetting).setColor(finalColor);
        }
    }

    public void drawPicker(ColorPicker subColor, int pickerX, int pickerY, int hueSliderX, int hueSliderY, int alphaSliderX, int alphaSliderY) {
        float[] color = new float[] {
                0, 0, 0, 0
        };

        try {
            color = new float[] {
                    Color.RGBtoHSB(subColor.getColor().getRed(), subColor.getColor().getGreen(), subColor.getColor().getBlue(), null)[0], Color.RGBtoHSB(subColor.getColor().getRed(), subColor.getColor().getGreen(), subColor.getColor().getBlue(), null)[1], Color.RGBtoHSB(subColor.getColor().getRed(), subColor.getColor().getGreen(), subColor.getColor().getBlue(), null)[2], subColor.getAlpha() / 255f
            };
        } catch (Exception ignored) {

        }

        boolean pickingColor = false;
        boolean pickingHue = false;
        boolean pickingAlpha = false;

        int pickerWidth = 100;
        int pickerHeight = 100;
        int hueSliderWidth = pickerWidth + 3;
        int hueSliderHeight = 10;
        int alphaSliderHeight = 10;

        if (GUIUtil.lheld && GUIUtil.mouseOver(pickerX, pickerY, pickerX + pickerWidth, pickerY + pickerHeight))
            pickingColor = true;
        if (GUIUtil.lheld && GUIUtil.mouseOver(hueSliderX, hueSliderY, hueSliderX + hueSliderWidth, hueSliderY + hueSliderHeight))
            pickingHue = true;
        if (GUIUtil.lheld && GUIUtil.mouseOver(alphaSliderX, alphaSliderY, alphaSliderX + pickerWidth, alphaSliderY + alphaSliderHeight))
            pickingAlpha = true;

        if (pickingHue) {
            float restrictedX = (float) Math.min(Math.max(hueSliderX, GUIUtil.mX), hueSliderX + hueSliderWidth);
            color[0] = (restrictedX - (float) hueSliderX) / hueSliderWidth;
        }

        if (pickingAlpha) {
            float restrictedX = (float) Math.min(Math.max(alphaSliderX, GUIUtil.mX), alphaSliderX + pickerWidth);
            color[3] = 1 - (restrictedX - (float) alphaSliderX) / pickerWidth;
        }

        if (pickingColor) {
            float restrictedX = (float) Math.min(Math.max(pickerX, GUIUtil.mX), pickerX + pickerWidth);
            float restrictedY = (float) Math.min(Math.max(pickerY, GUIUtil.mY), pickerY + pickerHeight);
            color[1] = (restrictedX - (float) pickerX) / pickerWidth;
            color[2] = 1 - (restrictedY - (float) pickerY) / pickerHeight;
        }

        Render2DUtil.drawRect(pickerX - 2, pickerY - 2, pickerX + pickerWidth + 2, pickerY + pickerHeight + 50, 0, 0xCC232323, -1, false, Render2DMode.Normal);

        int selectedColor = Color.HSBtoRGB(color[0], 1.0f, 1.0f);

        float selectedRed = (selectedColor >> 16 & 0xFF) / 255.0f;
        float selectedGreen = (selectedColor >> 8 & 0xFF) / 255.0f;
        float selectedBlue = (selectedColor & 0xFF) / 255.0f;

        Render2DUtil.drawPickerBase(pickerX, pickerY, pickerWidth, pickerHeight, selectedRed, selectedGreen, selectedBlue, color[3]);

        drawHueSlider(hueSliderX, hueSliderY, hueSliderWidth, hueSliderHeight, color[0]);

        int cursorX = (int) (pickerX + color[1] * pickerWidth);
        int cursorY = (int) ((pickerY + pickerHeight) - color[2] * pickerHeight);

        Render2DUtil.drawRect(cursorX - 2, cursorY - 2, cursorX + 2, cursorY + 2, 0, -1, -1, false, Render2DMode.Normal);

        drawAlphaSlider(alphaSliderX, alphaSliderY, pickerWidth, alphaSliderHeight, selectedRed, selectedGreen, selectedBlue, color[3]);

        finalColor = ColorUtil.alphaIntegrate(new Color(Color.HSBtoRGB(color[0], color[1], color[2])), color[3]);
    }

    public void drawHueSlider(int x, int y, int width, int height, float hue) {
        int step = 0;

        if (height > width) {
            Render2DUtil.drawRect(x, y, x + width, y + 4, 0, 0xFFFF0000, -1, false, Render2DMode.Normal);
            y += 4;

            for (int colorIndex = 0; colorIndex < 6; colorIndex++) {
                int previousStep = Color.HSBtoRGB((float) step / 6, 1.0f, 1.0f);
                int nextStep = Color.HSBtoRGB((float) (step + 1) / 6, 1.0f, 1.0f);
                Render2DUtil.drawGradientRect(x, y + step * (height / 6), x + width, y + (step + 1) * (height / 6), previousStep, nextStep);
                step++;
            }

            int sliderMinY = (int) (y + (height * hue)) - 4;
            Render2DUtil.drawRect(x, sliderMinY - 1, x + width, sliderMinY + 1, 0, -1, -1, false, Render2DMode.Normal);
        } else {
            for (int colorIndex = 0; colorIndex < 6; colorIndex++) {
                int previousStep = Color.HSBtoRGB((float) step / 6, 1.0f, 1.0f);
                int nextStep = Color.HSBtoRGB((float) (step + 1) / 6, 1.0f, 1.0f);
                Render2DUtil.gradient(x + step * (width / 6), y, x + (step + 1) * (width / 6), y + height, previousStep, nextStep, true);
                step++;
            }

            int sliderMinX = (int) (x + (width * hue));
            Render2DUtil.drawRect(sliderMinX - 1, y, sliderMinX + 1, y + height, 0, -1, -1, false, Render2DMode.Normal);
        }
    }

    public void drawAlphaSlider(int x, int y, int width, int height, float red, float green, float blue, float alpha) {
        boolean left = true;
        int checkerBoardSquareSize = height / 2;

        for (int squareIndex = -checkerBoardSquareSize; squareIndex < width; squareIndex += checkerBoardSquareSize) {
            if (!left) {
                Render2DUtil.drawRect(x + squareIndex, y, x + squareIndex + checkerBoardSquareSize, y + height, 0, 0xFFFFFFFF, -1, false, Render2DMode.Normal);
                Render2DUtil.drawRect(x + squareIndex, y + checkerBoardSquareSize, x + squareIndex + checkerBoardSquareSize, y + height, 0, 0xFF909090, -1, false, Render2DMode.Normal);

                if (squareIndex < width - checkerBoardSquareSize) {
                    int minX = x + squareIndex + checkerBoardSquareSize;
                    int maxX = Math.min(x + width, x + squareIndex + checkerBoardSquareSize * 2);
                    Render2DUtil.drawRect(minX, y, maxX, y + height, 0, 0xFF909090, -1, false, Render2DMode.Normal);
                    Render2DUtil.drawRect(minX, y + checkerBoardSquareSize, maxX, y + height, 0, 0xFFFFFFFF, -1, false, Render2DMode.Normal);
                }
            }

            left = !left;
        }

        Render2DUtil.drawLeftGradientRect(x, y, x + width, y + height, new Color(red, green, blue, 1).getRGB(), 0);
        int sliderMinX = (int) (x + width - (width * alpha));
        Render2DUtil.drawRect(sliderMinX - 1, y, sliderMinX + 1, y + height, 0, -1, -1, false, Render2DMode.Normal);
    }
}
