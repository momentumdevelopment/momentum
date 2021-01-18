package me.linus.momentum.gui.theme.themes;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.gui.theme.ThemeColor;
import me.linus.momentum.gui.theme.Theme;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.Module;
import me.linus.momentum.module.ModuleManager;
import me.linus.momentum.module.modules.client.ClickGUI;
import me.linus.momentum.setting.Setting;
import me.linus.momentum.setting.SubSetting;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.color.SubColor;
import me.linus.momentum.setting.keybind.Keybind;
import me.linus.momentum.setting.keybind.SubKeybind;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.mode.SubMode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.client.ColorUtil;
import me.linus.momentum.util.render.FontUtil;
import me.linus.momentum.util.render.GUIUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

/**
 * @author linustouchtips
 * @since 12/01/2020
 */

public class DefaultTheme extends Theme implements MixinInterface {
    public static int boost = 0;

    public static String name = "Default";
    public static int width = 105;
    public static int height = 14;

    public static Color finalColor;
    public static float finalAlpha = 0.2f;

    public DefaultTheme() {
        super(name, width, height);
    }

    @Override
    public void updateColors() {
        ThemeColor.updateColors();
    }

    @Override
    public void drawTitles(String name, int x, int y) {
        GuiScreen.drawRect(x - 2, y, (x + width + 2), y + height, ThemeColor.GRADIENT ? ColorUtil.rainbow(boost) : ThemeColor.COLOR);
        FontUtil.drawString(name, (x + ((x + width) - x) / 2 - (ModuleManager.getModuleByName("Font").isEnabled() ? Momentum.fontManager.getCustomFont().getStringWidth(name) : mc.fontRenderer.getStringWidth(name)) / 2), y + 3, -1);
    }

    @Override
    public void drawModules(List<Module> modules, int x, int y, int mouseX, int mouseY, float partialTicks) {
        boost = 0;
        for (Module m : modules) {
            int color = 0xCC232323;
            if (GUIUtil.mouseOver(x, y + height + 1 + (boost * height), (x + width) - 1, y + height*2 + (boost * height))) {
                color = 0xCC383838;
                if (GUIUtil.ldown)
                    m.toggle();

                if (GUIUtil.rdown)
                    m.toggleState();
            }

            GuiScreen.drawRect(x, y + height + (boost * height), x + width, y + height * 2 + (boost * height), m.isEnabled() ? (ThemeColor.GRADIENT ? ColorUtil.rainbow(boost) : ThemeColor.COLOR) : color);
            FontUtil.drawString(m.getName(), x + 4, y + height + 4 + (boost * height), -1);

            if (m.hasSettings() && !m.isOpened())
                FontUtil.drawString("...", (x + width) - 12, y + 1 + height + (boost * height), -1);

            if (m.isOpened()) {
                if (m.hasSettings()) {
                    FontUtil.drawString("...", (x + width) - 12, y + 1 + height + (boost * height), -1);
                    drawDropdown(m, x, y, mouseX, mouseY);
                }

                if (!m.hasSettings())
                    boost++;

                drawBind(m, GUIUtil.keydown, x, y);
            }

            boost++;
        }
    }

    public static void drawDropdown(Module m, int x, int y, int mouseX, int mouseY) {
        if (m.hasSettings()) {
            for (Setting s : m.getSettings()) {
                boost++;
                if (s instanceof Checkbox) {
                    Checkbox c = (Checkbox) s;
                    drawCheckbox(c, x, y);
                    for (SubSetting ss : c.getSubSettings()) {
                        if (c.isOpened()) {
                            boost++;

                            if (ss instanceof SubCheckbox) {
                                SubCheckbox sc = (SubCheckbox) ss;
                                drawSubCheckbox(sc, x, y);
                            }

                            if (ss instanceof SubMode) {
                                SubMode sm = (SubMode) ss;
                                drawSubMode(sm, x, y);
                            }

                            if (ss instanceof SubSlider) {
                                SubSlider ssl = (SubSlider) ss;
                                drawSubSlider(ssl, x, y);
                            }

                            if (ss instanceof SubKeybind) {
                                SubKeybind skb = (SubKeybind) ss;
                                drawSubModuleBind(skb, GUIUtil.keydown, x, y);
                            }

                            if (ss instanceof SubColor) {
                                SubColor sc = (SubColor) ss;
                                drawColorPicker(sc, x, y, mouseX, mouseY);
                                boost += 9;
                            }

                            if (!(ss instanceof SubColor))
                                GuiScreen.drawRect(x + 4, y + ((boost + 1) * height) + 1, x + 5, y + height * 2 + (boost * height), 0xFF202020);
                        }
                    }
                }

                if (s instanceof Mode) {
                    Mode mode = (Mode) s;
                    drawMode(mode, x, y);
                    for (SubSetting ss : mode.getSubSettings()) {
                        if (mode.isOpened()) {
                            boost++;

                            if (ss instanceof SubCheckbox) {
                                SubCheckbox sc = (SubCheckbox) ss;
                                drawSubCheckbox(sc, x, y);
                            }

                            if (ss instanceof SubMode) {
                                SubMode sm = (SubMode) ss;
                                drawSubMode(sm, x, y);
                            }

                            if (ss instanceof SubSlider) {
                                SubSlider ssl = (SubSlider) ss;
                                drawSubSlider(ssl, x, y);
                            }

                            if (ss instanceof SubKeybind) {
                                SubKeybind skb = (SubKeybind) ss;
                                drawSubModuleBind(skb, GUIUtil.keydown, x, y);
                            }

                            if (ss instanceof SubColor) {
                                SubColor sc = (SubColor) ss;
                                drawColorPicker(sc, x, y, mouseX, mouseY);
                                boost += 9;
                            }

                            if (!(ss instanceof SubColor))
                                GuiScreen.drawRect(x + 4, y + ((boost + 1) * height) + 1, x + 5, y + height * 2 + (boost * height), 0xFF202020);
                        }
                    }
                }

                if (s instanceof Slider) {
                    Slider sl = (Slider) s;
                    drawSlider(sl, x, y);
                    for (SubSetting ss : sl.getSubSettings()) {
                        if (sl.isOpened()) {
                            boost++;

                            if (ss instanceof SubCheckbox) {
                                SubCheckbox sc = (SubCheckbox) ss;
                                drawSubCheckbox(sc, x, y);
                            }

                            if (ss instanceof SubMode) {
                                SubMode sm = (SubMode) ss;
                                drawSubMode(sm, x, y);
                            }

                            if (ss instanceof SubSlider) {
                                SubSlider ssl = (SubSlider) ss;
                                drawSubSlider(ssl, x, y);
                            }

                            if (ss instanceof SubKeybind) {
                                SubKeybind skb = (SubKeybind) ss;
                                drawSubModuleBind(skb, GUIUtil.keydown, x, y);
                            }

                            if (ss instanceof SubColor) {
                                SubColor sc = (SubColor) ss;
                                drawColorPicker(sc, x, y, mouseX, mouseY);
                                boost += 9;
                            }

                            if (!(ss instanceof SubColor))
                                GuiScreen.drawRect(x + 4, y + ((boost + 1) * height) + 1, x + 5, y + height * 2 + (boost * height), 0xFF202020);
                        }
                    }
                }

                if (s instanceof Keybind) {
                    Keybind kb = (Keybind) s;
                    drawModuleBind(kb, GUIUtil.keydown, x, y);
                    for (SubSetting ss : kb.getSubSettings()) {
                        if (kb.isOpened()) {
                            boost++;

                            if (ss instanceof SubCheckbox) {
                                SubCheckbox sc = (SubCheckbox) ss;
                                drawSubCheckbox(sc, x, y);
                            }

                            if (ss instanceof SubMode) {
                                SubMode sm = (SubMode) ss;
                                drawSubMode(sm, x, y);
                            }

                            if (ss instanceof SubSlider) {
                                SubSlider ssl = (SubSlider) ss;
                                drawSubSlider(ssl, x, y);
                            }

                            if (ss instanceof SubKeybind) {
                                SubKeybind skb = (SubKeybind) ss;
                                drawSubModuleBind(skb, GUIUtil.keydown, x, y);
                            }

                            if (ss instanceof SubColor) {
                                SubColor sc = (SubColor) ss;
                                drawColorPicker(sc, x, y, mouseX, mouseY);
                                boost += 9;
                            }

                            if (!(ss instanceof SubColor))
                                GuiScreen.drawRect(x + 4, y + ((boost + 1) * height) + 1, x + 5, y + height * 2 + (boost * height), 0xFF202020);
                        }
                    }
                }
            }
        }

        boost++;
    }

    public static void drawHUDDropdown(HUDComponent m, int x, int y) {
        if (m.hasSettings()) {
            for (Setting s : m.getSettings()) {
                boost++;

                if (s instanceof Checkbox) {
                    Checkbox c = (Checkbox) s;
                    drawCheckbox(c, x, y);
                }

                if (s instanceof Mode) {
                    Mode mode = (Mode) s;
                    drawMode(mode, x, y);
                }

                if (s instanceof Slider) {
                    Slider sl = (Slider) s;
                    drawSlider(sl, x, y);
                }

                if (s instanceof Keybind) {
                    Keybind kb = (Keybind) s;
                    drawModuleBind(kb, kb.getKey(), x, y);
                }
            }
        }
    }

    private static void drawCheckbox(Checkbox checkbox, int x, int y) {
        int color = 0xCC232323;
        if (GUIUtil.mouseOver(x + 4, y + height + (boost * height) + 2, (x + width) - 1, (y + height) + height + (boost * height))) {
            color = 0xCC383838;
            if (GUIUtil.ldown)
                checkbox.toggleValue();

            if (GUIUtil.rdown)
                checkbox.toggleState();
        }

        GuiScreen.drawRect(x, y + height + (boost * height), x + width, y + height*2 + (boost * height), 0x99202020);
        GuiScreen.drawRect(x + 4, y + height + (boost * height) + 1, (x + width) - 1, (y + height) + height + (boost * height), checkbox.getValue() ? (ThemeColor.GRADIENT ? ColorUtil.rainbow(boost) : ThemeColor.COLOR) : color);
        FontUtil.drawString(checkbox.getName(), x + 7, (y + height) + 4 + (boost * height), -1);

        if (checkbox.hasSubSettings() && !checkbox.isOpened())
            FontUtil.drawString("...", (x + width) - 12, (y + height + 1) + (boost * height), -1);
    }

    private static void drawSubCheckbox(SubCheckbox sc, int x, int y) {
        int color = 0xCC232323;
        if (GUIUtil.mouseOver(x + 8, y + height + (boost * height) + 2, (x + width) - 1, (y + height) + height + (boost * height))) {
            color = 0xCC383838;

            if (GUIUtil.ldown)
                sc.toggleValue();
        }

        GuiScreen.drawRect(x, y + height + (boost * height), x + width, y + height*2 + (boost * height), 0x99202020);
        GuiScreen.drawRect(x + 8, y + height + (boost * height) + 1, (x + width) - 1, (y + height) + height + (boost * height), sc.getValue() ? (ThemeColor.GRADIENT ? ColorUtil.rainbow(boost) : ThemeColor.COLOR) : color);
        FontUtil.drawString(sc.getName(), x + 10, (y + height) + 4 + (boost * height), -1);
    }

    private static void drawMode(Mode m, int x, int y) {
        int color = 0xCC232323;
        if (GUIUtil.mouseOver(x + 4, y + height + (boost * height) + 2, (x + width) - 1, (y + height) + height + (boost * height))) {
            color = 0xCC383838;

            if (GUIUtil.ldown)
                m.setMode(m.nextMode());

            if (GUIUtil.rdown)
                m.toggleState();
        }

        GuiScreen.drawRect(x, y + height + (boost * height), x + width, y + height * 2 + (boost * height), 0x99202020);
        GuiScreen.drawRect(x + 4, y + height + (boost * height) + 1, (x + width) - 1, (y + height) + height + (boost * height), color);
        FontUtil.drawString(m.getName() + ": " + m.getMode(m.getValue()), x + 7, (y + height) + 4 + (boost * height), -1);

        if (m.hasSubSettings() && !m.isOpened())
            FontUtil.drawString("...", (x + width) - 12, (y + height + 1) + (boost * height), -1);
    }

    private static void drawSubMode(SubMode sm, int x, int y) {
        int color = 0xCC232323;

        if (GUIUtil.mouseOver(x + 8, y + height + (boost * height) + 2, (x + width) - 1, (y + height) + height + (boost * height))) {
            color = 0xCC383838;

            if (GUIUtil.ldown)
                sm.setMode(sm.nextMode());
        }

        GuiScreen.drawRect(x, y + height + (boost * height), x + width, y + height*2 + (boost * height), 0x99202020);
        GuiScreen.drawRect(x + 8, y + height + (boost * height) + 1, (x + width) - 1, (y + height) + height + (boost * height), color);
        FontUtil.drawString(sm.getName() + ": " + sm.getMode(sm.getValue()), x + 12, (y + height) + 4 + (boost * height), -1);
    }

    private static void drawSlider(Slider sl, int x, int y) {
        int color = 0xCC232323;
        int rectAdd = (int) MathHelper.clamp((((x - 2) + width) - (x + 3)) * ((sl.getValue() - sl.getMinValue()) / (sl.getMaxValue() - sl.getMinValue())), 0.0D, (((x + 3) + width) - (x)));

        if (GUIUtil.mouseOver(x + 4, y + height + (boost * height) + 2, (x + width), (y + height) + height + (boost * height))) {
            color = 0xCC383838;
            if (GUIUtil.lheld) {
                int percentError = (GUIUtil.mX - (x + 4)) * 100 / (((x) + width) - (x + 4));
                sl.setValue(MathUtil.roundDouble(percentError * ((sl.getMaxValue() - sl.getMinValue()) / 100.0D) + sl.getMinValue(), sl.getRoundingScale()));
            }
        }

        if (GUIUtil.mouseOver(x + 4, y + height + (boost * height) + 2, (int) (x + ClickGUI.snapSub.getValue()), (y + height) + height + (boost * height))) {
            if (ClickGUI.snapSlider.getValue() && GUIUtil.lheld) {
                rectAdd = 0;
                sl.setValue(sl.getMinValue());
            }
        }

        if (GUIUtil.mouseOver((int) ((x + 4 + width) - ClickGUI.snapSub.getValue()), y + height + (boost * height) + 2, (x + width) - 1, (y + height) + height + (boost * height))) {
            if (ClickGUI.snapSlider.getValue() && GUIUtil.lheld) {
                rectAdd = ((x + 4 + width) - (x + 4));
                sl.setValue(sl.getMaxValue());
            }
        }

        GuiScreen.drawRect(x, y + height + (boost * height), x + width, y + height*2 + (boost * height), 0x99202020);
        GuiScreen.drawRect(x + 4, y + height + (boost * height) + 1, (x + width) - 1, (y + height) + height + (boost * height), color);
        GuiScreen.drawRect(x + 4, y + height + (boost * height) + 1, x + 4 + (rectAdd > width - 1 ? (width - 5) : rectAdd), (y + height) + height + (boost * height), ThemeColor.GRADIENT ? ColorUtil.rainbow(boost) : ThemeColor.COLOR);
        FontUtil.drawString(sl.getName() + " " + sl.getValue(), x + 6, (y + height) + 4 + (boost * height), -1);

        if (sl.hasSubSettings() && !sl.isOpened())
            FontUtil.drawString("...", (x + width) - 12, (y + height + 1) + (boost * height), -1);
    }

    private static void drawSubSlider(SubSlider ssl, int x, int y) {
        int color = 0xCC232323;
        int rectAdd = (int) MathHelper.clamp((((x - 2) + width) - (x + 7)) * ((ssl.getValue() - ssl.getMinValue()) / (ssl.getMaxValue() - ssl.getMinValue())), 0.0D, (((x + 7) + width) - (x)));

        if (GUIUtil.mouseOver(x + 8, y + height + (boost * height) + 2, (x + width), (y + height) + height + (boost * height))) {
            color = 0xCC383838;
            if (GUIUtil.lheld) {
                int percentError = (GUIUtil.mX - (x + 8)) * 100 / (((x) + width) - (x + 8));
                ssl.setValue(MathUtil.roundDouble(percentError * ((ssl.getMaxValue() - ssl.getMinValue()) / 100.0D) + ssl.getMinValue(), ssl.getRoundingScale()));
            }
        }

        if (GUIUtil.mouseOver(x + 8, y + height + (boost * height) + 2, (int) (x + ClickGUI.snapSub.getValue()), (y + height) + height + (boost * height))) {
            if (ClickGUI.snapSlider.getValue() && GUIUtil.lheld) {
                rectAdd = 0;
                ssl.setValue(ssl.getMinValue());
            }
        }

        if (GUIUtil.mouseOver((int) ((x + 8 + width) - ClickGUI.snapSub.getValue()), y + height + (boost * height) + 2, (x + width) - 1, (y + height) + height + (boost * height))) {
            if (ClickGUI.snapSlider.getValue() && GUIUtil.lheld) {
                rectAdd = ((x + 8 + width) - (x + 8));
                ssl.setValue(ssl.getMaxValue());
            }
        }

        GuiScreen.drawRect(x, y + height + (boost * height), x + width, y + height*2 + (boost * height), 0x99202020);
        GuiScreen.drawRect(x + 8, y + height + (boost * height) + 1, (x + width) - 1, (y + height) + height + (boost * height), color);
        GuiScreen.drawRect(x + 8, y + height + (boost * height) + 1, x + 8 + (rectAdd > width ? (width-8) : rectAdd), (y + height) + height + (boost * height), ThemeColor.GRADIENT ? ColorUtil.rainbow(boost) : ThemeColor.COLOR);
        FontUtil.drawString(ssl.getName() + " " + ssl.getValue(), x + 10, (y + height) + 4 + (boost * height), -1);
    }

    public static void drawModuleBind(Keybind keybind, int key, int x, int y) {
        int color = 0xCC232323;
        if (GUIUtil.mouseOver(x + 4, y + height + (boost * height) + 2, (x + width) - 1, (y + height) + height + (boost * height))) {
            color = 0xCC383838;
            if (GUIUtil.ldown)
                keybind.setBinding(true);
        }

        if (keybind.isBinding() && key != -1 && key != Keyboard.KEY_ESCAPE && key != Keyboard.KEY_DELETE) {
            keybind.setKey((key == Keyboard.KEY_DELETE || key == Keyboard.KEY_BACK) ? Keyboard.KEY_NONE : key);
            keybind.setBinding(false);
        }

        if (keybind.isBinding() && key == Keyboard.KEY_ESCAPE)
            keybind.setBinding(false);

        GuiScreen.drawRect(x, y + height + (boost * height), x + width, y + height*2 + (boost * height), 0x99202020);
        GuiScreen.drawRect(x + 4, y + height + (boost * height) + 1, (x + width) - 1, (y + height) + height + (boost * height), color);

        if (!keybind.isBinding())
            FontUtil.drawString(keybind.getName() + ": " + (keybind.getKey() == -2 ? "None" : Keyboard.getKeyName(keybind.getKey())), x + 7, (y + height) + 4 + (boost * height), -1);
        else
            FontUtil.drawString("Listening...", x + 7, (y + height) + 4 + (boost * height), -1);
    }

    public static void drawSubModuleBind(SubKeybind skb, int key, int x, int y) {
        int color = 0xCC232323;
        if (GUIUtil.mouseOver(x + 4, y + height + (boost * height) + 2, (x + width) - 1, (y + height) + height + (boost * height))) {
            color = 0xCC383838;
            if (GUIUtil.ldown)
                skb.setBinding(true);
        }

        if (skb.isBinding() && key != -1 && key != Keyboard.KEY_ESCAPE && key != Keyboard.KEY_DELETE) {
            skb.setKey((key == Keyboard.KEY_DELETE || key == Keyboard.KEY_BACK) ? Keyboard.KEY_NONE : key);
            skb.setBinding(false);
        }

        if (skb.isBinding() && key == Keyboard.KEY_ESCAPE)
            skb.setBinding(false);

        GuiScreen.drawRect(x, y + height + (boost * height), x + width, y + height*2 + (boost * height), 0x99202020);
        GuiScreen.drawRect(x + 4, y + height + (boost * height) + 1, (x + width) - 1, (y + height) + height + (boost * height), color);

        if (!skb.isBinding())
            FontUtil.drawString(skb.getName() + ": " + (skb.getKey() == -2 ? "None" : Keyboard.getKeyName(skb.getKey())), x + 7, (y + height) + 4 + (boost * height), -1);
        else
            FontUtil.drawString("Listening...", x + 7, (y + height) + 4 + (boost * height), -1);
    }

    public static void drawColorPicker(SubColor sc, int x, int y, int mouseX, int mouseY) {
        drawPicker(sc, mouseX, mouseY, x + 3, y + height + (boost * height) + 2, x + 3, y + height + (boost * height) + 105, x + 3, y + height + (boost * height) + 124);
        sc.setColor(finalColor);
    }

    public static Color alphaIntegrate(Color color, float alpha) {
        float red = (float) color.getRed() / 255;
        float green = (float) color.getGreen() / 255;
        float blue = (float) color.getBlue() / 255;
        return new Color(red, green, blue, alpha);
    }

    public static void drawBind(Module m, int key, int x, int y) {
        int color = 0xCC232323;
        if (GUIUtil.mouseOver(x + 4, y + height + (boost * height) + 2, (x + width) - 1, (y + height) + height + (boost * height))) {
            color = 0xCC383838;
            if (GUIUtil.ldown)
                m.setBinding(true);
        }

        if (m.isBinding() && key != -1 && key != Keyboard.KEY_ESCAPE && key != Keyboard.KEY_DELETE) {
            m.getKeybind().setKeyCode((key == Keyboard.KEY_DELETE || key == Keyboard.KEY_BACK) ? Keyboard.KEY_NONE : key);
            m.setBinding(false);
        }

        if (m.isBinding() && key == Keyboard.KEY_ESCAPE)
            m.setBinding(false);

        GuiScreen.drawRect(x, y + height + (boost * height), x + width, y + height*2 + (boost * height), 0x99202020);
        GuiScreen.drawRect(x + 4, y + height + (boost * height) + 1, (x + width) - 1, (y + height) + height + (boost * height), color);

        if (!m.isBinding())
            FontUtil.drawString("Keybind: " + (m.getKeybind().getDisplayName().equalsIgnoreCase("NONE") ? "None" : m.getKeybind().getDisplayName()), x + 7, (y + height) + 4 + (boost * height), -1);

        else
            FontUtil.drawString("Listening...", x + 7, (y + height) + 4 + (boost * height), -1);
    }

    @Override
    public void drawHUDModules(List<HUDComponent> components, int x, int y) {
        boost = 0;
        int color = 0xCC232323;
        for (HUDComponent component : components) {
            if (GUIUtil.mouseOver(x, y + height + 1 + (boost * height), (x + width) - 1, y + height * 2 + (boost * height))) {
                if (GUIUtil.ldown)
                    component.toggle();

                if (GUIUtil.rdown)
                    component.toggleState();
            }

            GuiScreen.drawRect(x, y + height + (boost * height), x + width, y + height * 2 + (boost * height), component.isEnabled() ? (ThemeColor.GRADIENT ? ColorUtil.rainbow(boost) : ThemeColor.COLOR) : color);
            FontUtil.drawString(component.getName(), x + 4, y + height + 3 + (boost * height), -1);

            if (component.hasSettings() && !component.isOpened())
                FontUtil.drawString("...", (x + width) - 12, y + 2 + height + (boost * height), -1);

            if (component.isOpened()) {
                if (component.hasSettings()) {
                    FontUtil.drawString("...", (x + width) - 12, y + 2 + height + (boost * height), -1);
                    drawHUDDropdown(component, x, y);
                }
            }

            boost++;
        }
    }

    public static void drawPicker(SubColor subColor, int mouseX, int mouseY, int pickerX, int pickerY, int hueSliderX, int hueSliderY, int alphaSliderX, int alphaSliderY) {
        float[] color = new float[] {
                subColor.getColor().RGBtoHSB(subColor.getColor().getRed(), subColor.getColor().getGreen(), subColor.getColor().getBlue(), null)[0], subColor.getColor().RGBtoHSB(subColor.getColor().getRed(), subColor.getColor().getGreen(), subColor.getColor().getBlue(), null)[1], subColor.getColor().RGBtoHSB(subColor.getColor().getRed(), subColor.getColor().getGreen(), subColor.getColor().getBlue(), null)[2]
        };

        boolean pickingColor = false;
        boolean pickingHue = false;
        boolean pickingAlpha = false;

        int pickerWidth = 100;
        int pickerHeight = 100;
        int hueSliderWidth = pickerWidth + 3;
        int hueSliderHeight = 10;
        int alphaSliderWidth = pickerWidth;
        int alphaSliderHeight = 10;

        if (GUIUtil.lheld && GUIUtil.mouseOver(pickerX, pickerY, pickerX + pickerWidth, pickerY + pickerHeight))
            pickingColor = true;
        if (GUIUtil.lheld && GUIUtil.mouseOver(hueSliderX, hueSliderY, hueSliderX + hueSliderWidth, hueSliderY + hueSliderHeight))
            pickingHue = true;
        if (GUIUtil.lheld && GUIUtil.mouseOver(alphaSliderX, alphaSliderY, alphaSliderX + alphaSliderWidth, alphaSliderY + alphaSliderHeight))
            pickingAlpha = true;

        if (!GUIUtil.lheld)
            pickingColor = pickingHue = pickingAlpha = false;

        if (pickingHue) {
            if (hueSliderWidth > hueSliderHeight) {
                float restrictedX = (float) Math.min(Math.max(hueSliderX, mouseX), hueSliderX + hueSliderWidth);
                color[0] = (restrictedX - (float) hueSliderX) / hueSliderWidth;
            }

            else {
                float restrictedY = (float) Math.min(Math.max(hueSliderY, mouseY), hueSliderY + hueSliderHeight);
                color[0] = (restrictedY - (float) hueSliderY) / hueSliderHeight;
            }
        }

        if (pickingAlpha) {
            if (alphaSliderWidth > alphaSliderHeight) {
                float restrictedX = (float) Math.min(Math.max(alphaSliderX, mouseX), alphaSliderX + alphaSliderWidth);
                finalAlpha = 1 - (restrictedX - (float) alphaSliderX) / alphaSliderWidth;
            }

            else {
                float restrictedY = (float) Math.min(Math.max(alphaSliderY, mouseY), alphaSliderY + alphaSliderHeight);
                finalAlpha = 1 - (restrictedY - (float) alphaSliderY) / alphaSliderHeight;
            }
        }

        if (pickingColor) {
            float restrictedX = (float) Math.min(Math.max(pickerX, mouseX), pickerX + pickerWidth);
            float restrictedY = (float) Math.min(Math.max(pickerY, mouseY), pickerY + pickerHeight);
            color[1] = (restrictedX - (float) pickerX) / pickerWidth;
            color[2] = 1 - (restrictedY - (float) pickerY) / pickerHeight;
        }

        GuiScreen.drawRect(pickerX - 2, pickerY - 2, pickerX + pickerWidth + 2, pickerY + pickerHeight + 50, 0xCC232323);

        int selectedColor = Color.HSBtoRGB(color[0], 1.0f, 1.0f);
        float selectedRed = (selectedColor >> 16 & 0xFF) / 255.0f;
        float selectedGreen = (selectedColor >> 8 & 0xFF) / 255.0f;
        float selectedBlue = (selectedColor & 0xFF) / 255.0f;

        drawPickerBase(pickerX, pickerY, pickerWidth, pickerHeight, selectedRed, selectedGreen, selectedBlue, finalAlpha);
        drawHueSlider(hueSliderX, hueSliderY, hueSliderWidth, hueSliderHeight, color[0]);
        drawAlphaSlider(alphaSliderX, alphaSliderY, alphaSliderWidth, alphaSliderHeight, selectedRed, selectedGreen, selectedBlue, finalAlpha);

        int cursorX = (int) (pickerX + color[1] * pickerWidth);
        int cursorY = (int) ((pickerY + pickerHeight) - color[2] * pickerHeight);
        GuiScreen.drawRect(cursorX - 2, cursorY - 2, cursorX + 2, cursorY + 2, -1);
        finalColor = alphaIntegrate(new Color(Color.HSBtoRGB(color[0], color[1], color[2])), finalAlpha);
    }

    public static void drawPickerBase(int pickerX, int pickerY, int pickerWidth, int pickerHeight, float red, float green, float blue, float alpha) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glBegin(GL11.GL_POLYGON);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glVertex2f(pickerX, pickerY);
        GL11.glVertex2f(pickerX, pickerY + pickerHeight);
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glVertex2f(pickerX + pickerWidth, pickerY + pickerHeight);
        GL11.glVertex2f(pickerX + pickerWidth, pickerY);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBegin(GL11.GL_POLYGON);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glVertex2f(pickerX, pickerY);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        GL11.glVertex2f(pickerX, pickerY + pickerHeight);
        GL11.glVertex2f(pickerX + pickerWidth, pickerY + pickerHeight);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glVertex2f(pickerX + pickerWidth, pickerY);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void drawHueSlider(int x, int y, int width, int height, float hue) {
        int step = 0;

        if (height > width) {
            GuiScreen.drawRect(x, y, x + width, y + 4, 0xFFFF0000);
            y += 4;

            for (int colorIndex = 0; colorIndex < 6; colorIndex++) {
                int previousStep = Color.HSBtoRGB((float) step/6, 1.0f, 1.0f);
                int nextStep = Color.HSBtoRGB((float) (step+1)/6, 1.0f, 1.0f);
                drawGradientRect(x, y + step * (height/6), x + width, y + (step+1) * (height/6), previousStep, nextStep);
                step++;
            }

            int sliderMinY = (int) (y + (height * hue)) - 4;
            GuiScreen.drawRect(x, sliderMinY - 1, x+width, sliderMinY + 1, -1);
        }

        else {
            for (int colorIndex = 0; colorIndex < 6; colorIndex++) {
                int previousStep = Color.HSBtoRGB((float) step/6, 1.0f, 1.0f);
                int nextStep = Color.HSBtoRGB((float) (step+1)/6, 1.0f, 1.0f);
                gradient(x + step * (width/6), y, x + (step+1) * (width/6), y + height, previousStep, nextStep, true);
                step++;
            }

            int sliderMinX = (int) (x + (width * hue));
            GuiScreen.drawRect(sliderMinX - 1, y, sliderMinX + 1, y + height, -1);
        }
    }

    public static void drawAlphaSlider(int x, int y, int width, int height, float red, float green, float blue, float alpha) {
        boolean left = true;
        int checkerBoardSquareSize = height / 2;

        for (int squareIndex = -checkerBoardSquareSize; squareIndex < width; squareIndex += checkerBoardSquareSize) {
            if (!left) {
                GuiScreen.drawRect(x + squareIndex, y, x + squareIndex + checkerBoardSquareSize, y + height, 0xFFFFFFFF);
                GuiScreen.drawRect(x + squareIndex, y + checkerBoardSquareSize, x + squareIndex + checkerBoardSquareSize, y + height, 0xFF909090);

                if (squareIndex < height - checkerBoardSquareSize) {
                    int minX = x + squareIndex + checkerBoardSquareSize;
                    int maxX = Math.min(x + width, x + squareIndex + checkerBoardSquareSize * 2);
                    GuiScreen.drawRect(minX, y, maxX, y + height, 0xFF909090);
                    GuiScreen.drawRect(minX,y + checkerBoardSquareSize, maxX, y + height, 0xFFFFFFFF);
                }
            }

            left = !left;
        }

        gradient(x, y, x + width, y + height, new Color(red, green, blue, alpha).getRGB(), 0, true);
        int sliderMinX = (int) (x + width - (width * alpha));
        GuiScreen.drawRect(sliderMinX - 1, y,  sliderMinX + 1, y + height, -1);
    }

    public static void gradient(int minX, int minY, int maxX, int maxY, int startColor, int endColor, boolean left) {
        if (left) {
            float startA = (startColor >> 24 & 0xFF) / 255.0f;
            float startR = (startColor >> 16 & 0xFF) / 255.0f;
            float startG = (startColor >> 8 & 0xFF) / 255.0f;
            float startB = (startColor & 0xFF) / 255.0f;
            float endA = (endColor >> 24 & 0xFF) / 255.0f;
            float endR = (endColor >> 16 & 0xFF) / 255.0f;
            float endG = (endColor >> 8 & 0xFF) / 255.0f;
            float endB = (endColor & 0xFF) / 255.0f;

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            GL11.glBegin(GL11.GL_POLYGON);
            GL11.glColor4f(startR, startG, startB, startA);
            GL11.glVertex2f(minX, minY);
            GL11.glVertex2f(minX, maxY);
            GL11.glColor4f(endR, endG, endB, endA);
            GL11.glVertex2f(maxX, maxY);
            GL11.glVertex2f(maxX, minY);
            GL11.glEnd();
            GL11.glShadeModel(GL11.GL_FLAT);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
        }

        else
            drawGradientRect(minX, minY, maxX, maxY, startColor, endColor);
    }

    public static void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(right, top, 0).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(left, top, 0).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(left, bottom, 0).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos(right, bottom, 0).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
}