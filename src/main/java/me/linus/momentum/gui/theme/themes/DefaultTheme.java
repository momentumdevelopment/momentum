package me.linus.momentum.gui.theme.themes;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.linus.momentum.managers.CommandManager;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.gui.main.console.ConsoleWindow;
import me.linus.momentum.gui.theme.ThemeColor;
import me.linus.momentum.gui.theme.Theme;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.Module;
import me.linus.momentum.managers.ModuleManager;
import me.linus.momentum.module.modules.client.ClickGUI;
import me.linus.momentum.module.modules.client.Colors;
import me.linus.momentum.setting.Setting;
import me.linus.momentum.setting.SubSetting;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.color.ColorPicker;
import me.linus.momentum.setting.keybind.Keybind;
import me.linus.momentum.setting.keybind.SubKeybind;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.mode.SubMode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.client.ColorUtil;
import me.linus.momentum.util.render.FontUtil;
import me.linus.momentum.util.render.Render2DUtil;
import me.linus.momentum.util.render.GUIUtil;
import me.linus.momentum.util.render.builder.Render2DBuilder;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.settings.KeyModifier;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
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

    public static int consoleWidth = 305;
    public static int consoleHeight = 14;

    public static Color finalColor;
    public static float finalAlpha = 0.2f;

    public static String typedCharacters = "";
    public static List<String> outputs = new ArrayList<>();

    ResourceLocation consoleIcon = new ResourceLocation("momentum:console.png");

    public DefaultTheme() {
        super(name, width, height);
    }

    @Override
    public void updateColors() {
        ThemeColor.updateColors();
    }

    @Override
    public void drawTitles(String name, int x, int y) {
        Render2DUtil.drawRect(x - 2, y, (x + width + 2), y + height, 0, ThemeColor.GRADIENT ? ColorUtil.rainbow(boost) : ThemeColor.COLOR, -1, false, Render2DBuilder.Render2DMode.Normal);
        FontUtil.drawString(name, (x + ((x + width) - x) / 2 - (ModuleManager.getModuleByName("Font").isEnabled() ? FontUtil.getStringWidth(name) : mc.fontRenderer.getStringWidth(name)) / 2), y + 3, -1);
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

            Render2DUtil.drawRect(x, y + height + (boost * height), x + width, y + height * 2 + (boost * height), 0, m.isEnabled() ? (ThemeColor.GRADIENT ? ColorUtil.rainbow(boost) : ThemeColor.COLOR) : color, -1, false, Render2DBuilder.Render2DMode.Normal);
            FontUtil.drawString(m.getName(), x + 4, y + height + 4 + (boost * height), -1);

            if (m.hasSettings() && !m.isOpened() && ClickGUI.indicators.getValue())
                FontUtil.drawString("+", (x + width) - 12, y + 1 + height + (boost * height), -1);

            if (m.isOpened()) {
                if (m.hasSettings()) {
                    if (ClickGUI.indicators.getValue())
                        FontUtil.drawString("-", (x + width) - 12, y + 1 + height + (boost * height), -1);

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
                                drawSubMode(m, sm, x, y);
                            }

                            if (ss instanceof SubSlider) {
                                SubSlider ssl = (SubSlider) ss;
                                drawSubSlider(ssl, x, y);
                            }

                            if (ss instanceof SubKeybind) {
                                SubKeybind skb = (SubKeybind) ss;
                                drawSubModuleBind(skb, GUIUtil.keydown, x, y);
                            }

                            if (ss instanceof ColorPicker) {
                                ColorPicker sc = (ColorPicker) ss;
                                drawColorPicker(sc, x, y, mouseX, mouseY);
                                boost += 9;
                            }
                        }
                    }
                }

                if (s instanceof Mode) {
                    Mode mode = (Mode) s;
                    drawMode(m, mode, x, y);
                    for (SubSetting ss : mode.getSubSettings()) {
                        if (mode.isOpened()) {
                            boost++;

                            if (ss instanceof SubCheckbox) {
                                SubCheckbox sc = (SubCheckbox) ss;
                                drawSubCheckbox(sc, x, y);
                            }

                            if (ss instanceof SubMode) {
                                SubMode sm = (SubMode) ss;
                                drawSubMode(m, sm, x, y);
                            }

                            if (ss instanceof SubSlider) {
                                SubSlider ssl = (SubSlider) ss;
                                drawSubSlider(ssl, x, y);
                            }

                            if (ss instanceof SubKeybind) {
                                SubKeybind skb = (SubKeybind) ss;
                                drawSubModuleBind(skb, GUIUtil.keydown, x, y);
                            }

                            if (ss instanceof ColorPicker) {
                                ColorPicker sc = (ColorPicker) ss;
                                drawColorPicker(sc, x, y, mouseX, mouseY);
                                boost += 9;
                            }
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
                                drawSubMode(m, sm, x, y);
                            }

                            if (ss instanceof SubSlider) {
                                SubSlider ssl = (SubSlider) ss;
                                drawSubSlider(ssl, x, y);
                            }

                            if (ss instanceof SubKeybind) {
                                SubKeybind skb = (SubKeybind) ss;
                                drawSubModuleBind(skb, GUIUtil.keydown, x, y);
                            }

                            if (ss instanceof ColorPicker) {
                                ColorPicker sc = (ColorPicker) ss;
                                drawColorPicker(sc, x, y, mouseX, mouseY);
                                boost += 9;
                            }
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
                                drawSubMode(m, sm, x, y);
                            }

                            if (ss instanceof SubSlider) {
                                SubSlider ssl = (SubSlider) ss;
                                drawSubSlider(ssl, x, y);
                            }

                            if (ss instanceof SubKeybind) {
                                SubKeybind skb = (SubKeybind) ss;
                                drawSubModuleBind(skb, GUIUtil.keydown, x, y);
                            }

                            if (ss instanceof ColorPicker) {
                                ColorPicker sc = (ColorPicker) ss;
                                drawColorPicker(sc, x, y, mouseX, mouseY);
                                boost += 9;
                            }
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
                    drawMode(null, mode, x, y);
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

        Render2DUtil.drawRect(x, y + height + (boost * height), x + width, y + height * 2 + (boost * height), 0, 0x99202020, -1, false, Render2DBuilder.Render2DMode.Normal);
        Render2DUtil.drawRect(x + 4, y + height + (boost * height), (x + width) - 1, (y + height) + height + (boost * height), 0, checkbox.getValue() ? (ThemeColor.GRADIENT ? ColorUtil.rainbow(boost) : ThemeColor.COLOR) : color, -1, false, Render2DBuilder.Render2DMode.Normal);
        FontUtil.drawString(checkbox.getName(), x + 7, (y + height) + 4 + (boost * height), -1);

        if (checkbox.hasSubSettings() && !checkbox.isOpened() && ClickGUI.indicators.getValue())
            FontUtil.drawString("+", (x + width) - 12, (y + height + 1) + (boost * height), -1);

        if (checkbox.hasSubSettings() && checkbox.isOpened() && ClickGUI.indicators.getValue())
            FontUtil.drawString("-", (x + width) - 12, (y + height + 1) + (boost * height), -1);
    }

    private static void drawSubCheckbox(SubCheckbox sc, int x, int y) {
        int color = 0xCC232323;
        if (GUIUtil.mouseOver(x + 8, y + height + (boost * height) + 2, (x + width) - 1, (y + height) + height + (boost * height))) {
            color = 0xCC383838;

            if (GUIUtil.ldown)
                sc.toggleValue();
        }

        Render2DUtil.drawRect(x, y + height + (boost * height), x + width, y + height*2 + (boost * height), 0, 0x99202020, -1, false, Render2DBuilder.Render2DMode.Normal);
        Render2DUtil.drawRect(x + 8, y + height + (boost * height), (x + width) - 1, (y + height) + height + (boost * height), 0, sc.getValue() ? (ThemeColor.GRADIENT ? ColorUtil.rainbow(boost) : ThemeColor.COLOR) : color, -1, false, Render2DBuilder.Render2DMode.Normal);
        FontUtil.drawString(sc.getName(), x + 10, (y + height) + 4 + (boost * height), -1);
    }

    private static void drawMode(@Nullable Module module, Mode m, int x, int y) {
        int color = 0xCC232323;
        if (GUIUtil.mouseOver(x + 4, y + height + (boost * height) + 2, (x + width) - 1, (y + height) + height + (boost * height))) {
            color = 0xCC383838;

            if (GUIUtil.ldown) {
                try {
                    module.onValueChange();
                } catch (Exception e) {

                }

                m.setMode(m.nextMode());
            }

            if (GUIUtil.rdown)
                m.toggleState();
        }

        Render2DUtil.drawRect(x, y + height + (boost * height), x + width, y + height * 2 + (boost * height), 0, 0x99202020, -1, false, Render2DBuilder.Render2DMode.Normal);
        Render2DUtil.drawRect(x + 4, y + height + (boost * height), (x + width) - 1, (y + height) + height + (boost * height), 0, color, -1, false, Render2DBuilder.Render2DMode.Normal);
        FontUtil.drawString(m.getName() + ": " + m.getMode(m.getValue()), x + 7, (y + height) + 4 + (boost * height), -1);

        if (m.hasSubSettings() && !m.isOpened() && ClickGUI.indicators.getValue())
            FontUtil.drawString("+", (x + width) - 12, (y + height + 1) + (boost * height), -1);

        if (m.hasSubSettings() && m.isOpened() && ClickGUI.indicators.getValue())
            FontUtil.drawString("-", (x + width) - 12, (y + height + 1) + (boost * height), -1);
    }

    private static void drawSubMode(@Nullable Module module, SubMode sm, int x, int y) {
        int color = 0xCC232323;

        if (GUIUtil.mouseOver(x + 8, y + height + (boost * height) + 2, (x + width) - 1, (y + height) + height + (boost * height))) {
            color = 0xCC383838;

            if (GUIUtil.ldown) {
                try {
                    module.onValueChange();
                } catch (Exception e) {

                }

                sm.setMode(sm.nextMode());
            }
        }

        Render2DUtil.drawRect(x, y + height + (boost * height), x + width, y + height*2 + (boost * height), 0, 0x99202020, -1, false, Render2DBuilder.Render2DMode.Normal);
        Render2DUtil.drawRect(x + 8, y + height + (boost * height), (x + width) - 1, (y + height) + height + (boost * height), 0, color, -1, false, Render2DBuilder.Render2DMode.Normal);
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

        Render2DUtil.drawRect(x, y + height + (boost * height), x + width, y + height*2 + (boost * height), 0, 0x99202020, -1, false, Render2DBuilder.Render2DMode.Normal);
        Render2DUtil.drawRect(x + 4, y + height + (boost * height), (x + width) - 1, (y + height) + height + (boost * height), 0, color, -1, false, Render2DBuilder.Render2DMode.Normal);
        Render2DUtil.drawRect(x + 4, y + height + (boost * height), x + 4 + (rectAdd > width - 1 ? (width - 5) : rectAdd), (y + height) + height + (boost * height), 0, ThemeColor.GRADIENT ? ColorUtil.rainbow(boost) : ThemeColor.COLOR, -1, false, Render2DBuilder.Render2DMode.Normal);
        FontUtil.drawString(sl.getName() + " " + sl.getValue(), x + 6, (y + height) + 4 + (boost * height), -1);

        if (sl.hasSubSettings() && !sl.isOpened() && ClickGUI.indicators.getValue())
            FontUtil.drawString("+", (x + width) - 12, (y + height + 1) + (boost * height), -1);

        if (sl.hasSubSettings() && sl.isOpened() && ClickGUI.indicators.getValue())
            FontUtil.drawString("-", (x + width) - 12, (y + height + 1) + (boost * height), -1);
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

        Render2DUtil.drawRect(x, y + height + (boost * height), x + width, y + height*2 + (boost * height), 0, 0x99202020, -1, false, Render2DBuilder.Render2DMode.Normal);
        Render2DUtil.drawRect(x + 8, y + height + (boost * height), (x + width) - 1, (y + height) + height + (boost * height), 0, color, -1, false, Render2DBuilder.Render2DMode.Normal);
        Render2DUtil.drawRect(x + 8, y + height + (boost * height), x + 8 + (rectAdd > width ? (width-8) : rectAdd), (y + height) + height + (boost * height), 0, ThemeColor.GRADIENT ? ColorUtil.rainbow(boost) : ThemeColor.COLOR, -1, false, Render2DBuilder.Render2DMode.Normal);
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

        Render2DUtil.drawRect(x, y + height + (boost * height), x + width, y + height*2 + (boost * height), 0, 0x99202020, -1, false, Render2DBuilder.Render2DMode.Normal);
        Render2DUtil.drawRect(x + 4, y + height + (boost * height), (x + width) - 1, (y + height) + height + (boost * height), 0, color, -1, false, Render2DBuilder.Render2DMode.Normal);

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

        Render2DUtil.drawRect(x, y + height + (boost * height), x + width, y + height * 2 + (boost * height), 0, 0x99202020, -1, false, Render2DBuilder.Render2DMode.Normal);
        Render2DUtil.drawRect(x + 4, y + height + (boost * height), (x + width) - 1, (y + height) + height + (boost * height), 0, color, -1, false, Render2DBuilder.Render2DMode.Normal);

        if (!skb.isBinding())
            FontUtil.drawString(skb.getName() + ": " + (skb.getKey() == -2 ? "None" : Keyboard.getKeyName(skb.getKey())), x + 7, (y + height) + 4 + (boost * height), -1);
        else
            FontUtil.drawString("Listening...", x + 7, (y + height) + 4 + (boost * height), -1);
    }

    public static void drawColorPicker(ColorPicker sc, int x, int y, int mouseX, int mouseY) {
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
            m.getKeybind().setKeyModifierAndCode(KeyModifier.NONE, (key == Keyboard.KEY_DELETE || key == Keyboard.KEY_BACK) ? Keyboard.KEY_NONE : key);
            m.setBinding(false);
        }

        if (m.isBinding() && key == Keyboard.KEY_ESCAPE)
            m.setBinding(false);

        Render2DUtil.drawRect(x, y + height + (boost * height), x + width, y + height * 2 + (boost * height), 0, 0x99202020, -1, false, Render2DBuilder.Render2DMode.Normal);
        Render2DUtil.drawRect(x + 4, y + height + (boost * height), (x + width) - 1, (y + height) + height + (boost * height), 0, color, -1, false, Render2DBuilder.Render2DMode.Normal);

        if (!m.isBinding())
            FontUtil.drawString("Keybind: " + (m.getKeybind().getDisplayName().equalsIgnoreCase("NONE") ? "None" : m.getKeybind().getDisplayName()), x + 7, (y + height) + 4 + (boost * height), -1);
        else
            FontUtil.drawString("Listening...", x + 7, (y + height) + 4 + (boost * height), -1);
    }

    public static void drawPicker(ColorPicker subColor, int mouseX, int mouseY, int pickerX, int pickerY, int hueSliderX, int hueSliderY, int alphaSliderX, int alphaSliderY) {
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

        Render2DUtil.drawRect(pickerX - 2, pickerY - 2, pickerX + pickerWidth + 2, pickerY + pickerHeight + 50, 0, 0xCC232323, -1, false, Render2DBuilder.Render2DMode.Normal);

        int selectedColor = Color.HSBtoRGB(color[0], 1.0f, 1.0f);

        float selectedRed = (selectedColor >> 16 & 0xFF) / 255.0f;
        float selectedGreen = (selectedColor >> 8 & 0xFF) / 255.0f;
        float selectedBlue = (selectedColor & 0xFF) / 255.0f;

        Render2DUtil.drawPickerBase(pickerX, pickerY, pickerWidth, pickerHeight, selectedRed, selectedGreen, selectedBlue, finalAlpha);

        drawHueSlider(hueSliderX, hueSliderY, hueSliderWidth, hueSliderHeight, color[0]);

        int cursorX = (int) (pickerX + color[1] * pickerWidth);
        int cursorY = (int) ((pickerY + pickerHeight) - color[2] * pickerHeight);

        Render2DUtil.drawRect(cursorX - 2, cursorY - 2, cursorX + 2, cursorY + 2, 0, -1, -1, false, Render2DBuilder.Render2DMode.Normal);

        drawAlphaSlider(alphaSliderX, alphaSliderY, alphaSliderWidth, alphaSliderHeight, selectedRed, selectedGreen, selectedBlue, finalAlpha);

        finalColor = alphaIntegrate(new Color(Color.HSBtoRGB(color[0], color[1], color[2])), finalAlpha);
    }

    public static void drawHueSlider(int x, int y, int width, int height, float hue) {
        int step = 0;

        if (height > width) {
            Render2DUtil.drawRect(x, y, x + width, y + 4, 0, 0xFFFF0000, -1, false, Render2DBuilder.Render2DMode.Normal);
            y += 4;

            for (int colorIndex = 0; colorIndex < 6; colorIndex++) {
                int previousStep = Color.HSBtoRGB((float) step/6, 1.0f, 1.0f);
                int nextStep = Color.HSBtoRGB((float) (step+1)/6, 1.0f, 1.0f);
                Render2DUtil.drawGradientRect(x, y + step * (height/6), x + width, y + (step+1) * (height/6), previousStep, nextStep);
                step++;
            }

            int sliderMinY = (int) (y + (height * hue)) - 4;
            Render2DUtil.drawRect(x, sliderMinY - 1, x+width, sliderMinY + 1, 0, -1, -1, false, Render2DBuilder.Render2DMode.Normal);
        }

        else {
            for (int colorIndex = 0; colorIndex < 6; colorIndex++) {
                int previousStep = Color.HSBtoRGB((float) step/6, 1.0f, 1.0f);
                int nextStep = Color.HSBtoRGB((float) (step+1)/6, 1.0f, 1.0f);
                Render2DUtil.gradient(x + step * (width/6), y, x + (step+1) * (width/6), y + height, previousStep, nextStep, true);
                step++;
            }

            int sliderMinX = (int) (x + (width * hue));
            Render2DUtil.drawRect(sliderMinX - 1, y, sliderMinX + 1, y + height, 0, -1, -1, false, Render2DBuilder.Render2DMode.Normal);
        }
    }

    public static void drawAlphaSlider(int x, int y, int width, int height, float red, float green, float blue, float alpha) {
        boolean left = true;
        int checkerBoardSquareSize = height / 2;

        for (int squareIndex = -checkerBoardSquareSize; squareIndex < width; squareIndex += checkerBoardSquareSize) {
            if (!left) {
                Render2DUtil.drawRect(x + squareIndex, y, x + squareIndex + checkerBoardSquareSize, y + height, 0, 0xFFFFFFFF, -1, false, Render2DBuilder.Render2DMode.Normal);
                Render2DUtil.drawRect(x + squareIndex, y + checkerBoardSquareSize, x + squareIndex + checkerBoardSquareSize, y + height, 0, 0xFF909090, -1, false, Render2DBuilder.Render2DMode.Normal);

                if (squareIndex < width - checkerBoardSquareSize) {
                    int minX = x + squareIndex + checkerBoardSquareSize;
                    int maxX = Math.min(x + width, x + squareIndex + checkerBoardSquareSize * 2);
                    Render2DUtil.drawRect(minX, y, maxX, y + height, 0, 0xFF909090, -1, false, Render2DBuilder.Render2DMode.Normal);
                    Render2DUtil.drawRect(minX,y + checkerBoardSquareSize, maxX, y + height, 0, 0xFFFFFFFF, -1, false, Render2DBuilder.Render2DMode.Normal);
                }
            }

            left = !left;
        }

        Render2DUtil.drawLeftGradientRect(x, y, x + width, y + height, new Color(red, green, blue, 1).getRGB(), 0);
        int sliderMinX = (int) (x + width - (width * alpha));
        Render2DUtil.drawRect(sliderMinX - 1, y,  sliderMinX + 1, y + height, 0, -1, -1, false, Render2DBuilder.Render2DMode.Normal);
    }

    @Override
    public void drawHUDModules(List<HUDComponent> components, int x, int y) {
        boost = 0;
        for (HUDComponent component : components) {
            int color = 0xCC232323;
            if (GUIUtil.mouseOver(x, y + height + 1 + (boost * height), (x + width) - 1, y + height * 2 + (boost * height))) {
                color = 0xCC383838;
                if (GUIUtil.ldown)
                    component.toggle();

                if (GUIUtil.rdown)
                    component.toggleState();
            }

            Render2DUtil.drawRect(x, y + height + (boost * height), x + width, y + height * 2 + (boost * height), 0, component.isEnabled() ? (ThemeColor.GRADIENT ? ColorUtil.rainbow(boost) : ThemeColor.COLOR) : color, -1, false, Render2DBuilder.Render2DMode.Normal);
            FontUtil.drawString(component.getName(), x + 4, y + height + 3 + (boost * height), -1);

            if (component.hasSettings() && !component.isOpened() && ClickGUI.indicators.getValue())
                FontUtil.drawString("+", (x + width) - 12, y + 2 + height + (boost * height), -1);

            if (component.isOpened()) {
                if (component.hasSettings()) {
                    if (ClickGUI.indicators.getValue())
                        FontUtil.drawString("-", (x + width) - 12, y + 2 + height + (boost * height), -1);

                    drawHUDDropdown(component, x, y);
                }
            }

            boost++;
        }
    }

    @Override
    public void drawConsoleTitle(String name, int x, int y) {
        Render2DUtil.drawRect(x - 2, y, (x + consoleWidth + 2), y + consoleHeight, 0, ThemeColor.GRADIENT ? ColorUtil.rainbow(boost) : ThemeColor.COLOR, -1, false, Render2DBuilder.Render2DMode.Normal);
        FontUtil.drawString(name, x + 1, y + 3, -1);

        int cancelColor = new Color(255, 255, 255, 120).getRGB();
        if (GUIUtil.mouseOver(x + consoleWidth - 8, y + 2, x + consoleWidth + 2, y + consoleHeight - 2)) {
            cancelColor = new Color(255, 255, 255, 170).getRGB();
        }

        Render2DUtil.drawLine(x + consoleWidth + 1, y + consoleHeight - 2, x + consoleWidth - 9, y + 2, 3, cancelColor);
        Render2DUtil.drawLine(x + consoleWidth + 1, y + 2, x + consoleWidth - 9, y + consoleHeight - 2, 3, cancelColor);
    }

    @Override
    public void drawConsole(int x, int y) {
        int enterColor = ThemeColor.COLOR;
        if (GUIUtil.mouseOver(x + consoleWidth - 27, y + consoleHeight + 200, x + consoleWidth - 1, y + consoleHeight + 214)) {
            enterColor = new Color(Colors.clientPicker.getColor().getRed(), Colors.clientPicker.getColor().getGreen(), Colors.clientPicker.getColor().getBlue(), Colors.clientPicker.getColor().getAlpha() + 15).getRGB();

            if (GUIUtil.ldown)
                resetText();
        }

        Render2DUtil.drawRect(x - 2, y + consoleHeight, x + consoleWidth + 2, y + consoleHeight + 216, 1,  new Color(36, 36, 36, 60).getRGB(), ThemeColor.COLOR, false, Render2DBuilder.Render2DMode.Both);
        Render2DUtil.drawRect(x, y + consoleHeight + 200, x + consoleWidth - 28, y + consoleHeight + 214, 1, new Color(18, 18, 18, 90).getRGB(), new Color(0, 0, 0, 90).getRGB(), false, Render2DBuilder.Render2DMode.Both);

        Render2DUtil.drawRect(x + consoleWidth - 9, y + consoleHeight + 2, x + consoleWidth - 1, y + consoleHeight + 198, 1,  new Color(36, 36, 36, 70).getRGB(), new Color(0, 0, 0, 70).getRGB(), false, Render2DBuilder.Render2DMode.Both);

        Render2DUtil.drawRect(x + consoleWidth - 27, y + consoleHeight + 200, x + consoleWidth - 1, y + consoleHeight + 214, 1,  enterColor, -1, false, Render2DBuilder.Render2DMode.Normal);
        FontUtil.drawString("Enter", x + consoleWidth - 25, y + consoleHeight + 203, -1);

        Render2DUtil.drawRect(x + consoleWidth - 9, (int) MathUtil.clamp(y + consoleHeight + 183 + MathUtil.clamp(ConsoleWindow.scrollbar, -180, 0), y + consoleHeight, y + consoleHeight + 214), x + consoleWidth - 1, (int) MathUtil.clamp(y + consoleHeight + 198 + MathUtil.clamp(ConsoleWindow.scrollbar, -180, 0), y + consoleHeight, y + consoleHeight + 214), 1,  ThemeColor.COLOR, -1, false, Render2DBuilder.Render2DMode.Normal);

        int outputLength = 0;

        String[] command = getConsoleLine().split(" ");
        String predictionString = "";

        Collections.reverse(outputs);

        for (String output : outputs) {
            outputLength++;

            Render2DBuilder.prepareScissor(x, y + consoleHeight, x + consoleWidth + 2, y + consoleHeight + 200);
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            Render2DBuilder.translate(0, (int) -MathUtil.clamp(ConsoleWindow.scrollbar, -10000, 0));
            FontUtil.drawString(output, x + 3, y + consoleHeight + 200 - (12 * outputLength), ThemeColor.BRIGHT);
            Render2DBuilder.translate(0, (int) MathUtil.clamp(ConsoleWindow.scrollbar, -10000, 0));
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        }

        Collections.reverse(outputs);

        try {
            if ((!CommandManager.predictCommands(command[0]).isEmpty() || CommandManager.predictCommands(command[0]).size() != 0) && typedCharacters.length() > 0)
                predictionString = CommandManager.predictCommands(command[0]).get(0).getUsage() + " " + (command.length < 2 ? CommandManager.predictCommands(command[0]).get(0).getUsageException() : "");
        } catch (ArrayIndexOutOfBoundsException e) {

        }

        FontUtil.drawString(predictionString, x + 3, y + consoleHeight + 203, ThemeColor.COLOR);
        FontUtil.drawString(getConsoleLine() + (ConsoleWindow.isTyping ? FontUtil.insertionPoint() : ""), x + 3, y + consoleHeight + 203, -1);
    }

    @Override
    public void drawConsoleWindows() {
        Render2DUtil.drawRect(0, 0, 30, new ScaledResolution(mc).getScaledHeight(), 1, new Color(18, 18, 18, 90).getRGB(), new Color(0, 0, 0, 90).getRGB(), false, Render2DBuilder.Render2DMode.Both);

        int windowColor = ThemeColor.COLOR;
        if (GUIUtil.mouseOver(1, 1, 29, 30)) {
            windowColor = new Color(Colors.clientPicker.getColor().getRed(), Colors.clientPicker.getColor().getGreen(), Colors.clientPicker.getColor().getBlue(), Colors.clientPicker.getColor().getAlpha() + 15).getRGB();
        }

        for (ConsoleWindow windows : ConsoleWindow.windows) {
            Render2DUtil.drawRect(1, 1, 29, 30, 1, windowColor, -1, false, Render2DBuilder.Render2DMode.Normal);

            GlStateManager.enableAlpha();
            mc.getTextureManager().bindTexture(consoleIcon);
            GlStateManager.color(1, 1, 1, 0.47f);
            GL11.glPushMatrix();
            GuiScreen.drawScaledCustomSizeModalRect(1, 1, 0, 0, 256,256,26,26,256,256);
            GL11.glPopMatrix();
            GlStateManager.disableAlpha();
            GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
        }
    }

    private static void executeCommand(String[] command) {
        try {
            outputs.add(TextFormatting.WHITE + "> " + CommandManager.getCommandByUsage(command[0]).getUsage());

            try {
                CommandManager.getCommandByUsage(command[0]).onCommand(command);
            } catch (Exception e) {
                outputs.add(TextFormatting.RED + "Usage Incorrect!");
            }
        } catch (Exception e) {
            outputs.add(TextFormatting.RED + "No such command found!");
        }
    }

    public static void resetText() {
        executeCommand(typedCharacters.split(" "));
        typedCharacters = "";
    }

    public static String getConsoleLine() {
        if (!ConsoleWindow.isTyping)
            return ChatFormatting.WHITE + "Please enter a command ...";

        else {
            if (FontUtil.getStringWidth(ChatFormatting.WHITE + typedCharacters) < width)
                return typedCharacters;
            else
                resetText();
        }

        return "";
    }
}