package me.linus.momentum.gui.theme.themes;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.gui.theme.Color;
import me.linus.momentum.gui.theme.Theme;
import me.linus.momentum.gui.util.GuiUtil;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.Module;
import me.linus.momentum.module.modules.client.ClickGui;
import me.linus.momentum.setting.Setting;
import me.linus.momentum.setting.SubSetting;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.mode.SubMode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.client.system.MathUtil;
import me.linus.momentum.util.client.color.ColorUtil;
import me.linus.momentum.util.render.FontUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;

import java.util.List;

/**
 * @author linustouchtips
 * @since 12/01/2020
 */

public class DefaultTheme extends Theme implements MixinInterface {
    public static int boost = 0;

    public static final String name = "Default";
    public static final int width = 105;
    public static final int height = 14;

    public DefaultTheme() {
        super(name, width, height);
    }

    private static final FontRenderer font = mc.fontRenderer;

    @Override
    public void updateColors() {
        Color.updateColors();
    }

    @Override
    public void drawTitles(String name, int x, int y) {
        GuiScreen.drawRect(x - 2, y, (x + width + 2), y + height, Color.GRADIENT ? ColorUtil.rainbow(boost) : Color.COLOR);
        drawTextWithShadow(name, (x + ((x + width) - x) / 2 - (ClickGui.font.getValue() == 1 ? Momentum.latoFont.getStringWidth(name) : mc.fontRenderer.getStringWidth(name)) / 2), y + 3, -1);
    }

    @Override
    public void drawModules(List<Module> modules, int x, int y) {
        boost = 0;
        for (Module m : modules) {
            int color = 0xCC232323;
            if (GuiUtil.mouseOver(x, y + height + 1 + (boost * height), (x + width) - 1, y + height*2 + (boost * height))) {
                color = 0xCC383838;
                if (GuiUtil.ldown) {
                    m.toggle();
                }

                if (GuiUtil.rdown) {
                    m.toggleState();
                }
            }

            GuiScreen.drawRect(x, y + height + (boost * height), x + width, y + height * 2 + (boost * height), m.isEnabled() ? (Color.GRADIENT ? ColorUtil.rainbow(boost) : Color.COLOR) : color);
            drawTextWithShadow(m.getName(), x + 4, y + height + 4 + (boost * height), -1);

            if (m.hasSettings() && !m.isOpened()) {
                drawText("...", (x + width) - 12, y + 1 + height + (boost * height), -1);
            }

            if (m.isOpened()) {
                if (m.hasSettings()) {
                    drawText("...", (x + width) - 12, y + 1 + height + (boost * height), -1);
                    drawDropdown(m, x, y);
                }

                if (!m.hasSettings())
                    boost++;

                drawBind(m, GuiUtil.keydown, x, y);
            }

            boost++;
        }
    }

    public static void drawDropdown(Module m, int x, int y) {
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

                            GuiScreen.drawRect(x + 4, y + (boost * height) + 1, x + 5, y + height * 2 + (boost * height), 0xFF202020);
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

                            GuiScreen.drawRect(x + 4, y + (boost * height) + 1, x + 5, y + height * 2 + (boost * height), 0xFF202020);
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

                            GuiScreen.drawRect(x + 4, y + (boost * height) + 1, x + 5, y + height * 2 + (boost * height), 0xFF202020);
                        }
                    }
                }
            }
        }

        boost++;
    }

    private static void drawCheckbox(Checkbox checkbox, int x, int y) {
        int color = 0xCC232323;
        if (GuiUtil.mouseOver(x + 4, y + height + (boost * height) + 2, (x + width) - 1, (y + height) + height + (boost * height))) {
            color = 0xCC383838;
            if (GuiUtil.ldown) {
                checkbox.toggleValue();
            }

            if (GuiUtil.rdown) {
                checkbox.toggleState();
            }

        }

        GuiScreen.drawRect(x, y + height + (boost * height), x + width, y + height*2 + (boost * height), 0x99202020);
        GuiScreen.drawRect(x + 4, y + height + (boost * height) + 1, (x + width) - 1, (y + height) + height + (boost * height), checkbox.getValue() ? (Color.GRADIENT ? ColorUtil.rainbow(boost) : Color.COLOR) : color);
        drawTextWithShadow(checkbox.getName(), x + 7, (y + height) + 4 + (boost * height), -1);
        if (checkbox.hasSubSettings() && !checkbox.isOpened()) {
            drawText("...", (x + width) - 12, (y + height + 1) + (boost * height), -1);
        }
    }

    private static void drawSubCheckbox(SubCheckbox sc, int x, int y) {
        int color = 0xCC232323;
        if (GuiUtil.mouseOver(x + 8, y + height + (boost * height) + 2, (x + width) - 1, (y + height) + height + (boost * height))) {
            color = 0xCC383838;
            if (GuiUtil.ldown) {
                sc.toggleValue();
            }
        }

        GuiScreen.drawRect(x, y + height + (boost * height), x + width, y + height*2 + (boost * height), 0x99202020);
        GuiScreen.drawRect(x + 8, y + height + (boost * height) + 1, (x + width) - 1, (y + height) + height + (boost * height), sc.getValue() ? (Color.GRADIENT ? ColorUtil.rainbow(boost) : Color.COLOR) : color);
        drawTextWithShadow(sc.getName(), x + 10, (y + height) + 4 + (boost * height), -1);
    }

    private static void drawMode(Mode m, int x, int y) {
        int color = 0xCC232323;
        if (GuiUtil.mouseOver(x + 4, y + height + (boost * height) + 2, (x + width) - 1, (y + height) + height + (boost * height))) {
            color = 0xCC383838;
            if (GuiUtil.ldown) {
                m.setMode(m.nextMode());
            }

            if (GuiUtil.rdown) {
                m.toggleState();
            }
        }

        GuiScreen.drawRect(x, y + height + (boost * height), x + width, y + height * 2 + (boost * height), 0x99202020);
        GuiScreen.drawRect(x + 4, y + height + (boost * height) + 1, (x + width) - 1, (y + height) + height + (boost * height), color);
        drawTextWithShadow(m.getName() + ": " + m.getMode(m.getValue()), x + 7, (y + height) + 4 + (boost * height), -1);
        if (m.hasSubSettings() && !m.isOpened()) {
            drawText("...", (x + width) - 12, (y + height + 1) + (boost * height), -1);
        }
    }

    private static void drawSubMode(SubMode sm, int x, int y) {
        int color = 0xCC232323;
        if (GuiUtil.mouseOver(x + 8, y + height + (boost * height) + 2, (x + width) - 1, (y + height) + height + (boost * height))) {
            color = 0xCC383838;
            if (GuiUtil.ldown) {
                sm.setMode(sm.nextMode());
            }
        }

        GuiScreen.drawRect(x, y + height + (boost * height), x + width, y + height*2 + (boost * height), 0x99202020);
        GuiScreen.drawRect(x + 8, y + height + (boost * height) + 1, (x + width) - 1, (y + height) + height + (boost * height), color);
        drawTextWithShadow(sm.getName() + ": " + sm.getMode(sm.getValue()), x + 12, (y + height) + 4 + (boost * height), -1);
    }

    private static void drawSlider(Slider sl, int x, int y) {
        int color = 0xCC232323;
        int rectAdd = (int) MathHelper.clamp((((x - 2) + width) - (x + 3)) * ((sl.getValue() - sl.getMinValue()) / (sl.getMaxValue() - sl.getMinValue())), 0.0D, (((x + 3) + width) - (x)));

        if (GuiUtil.mouseOver(x + 4, y + height + (boost * height) + 2, (x + width), (y + height) + height + (boost * height))) {
            color = 0xCC383838;
            if (GuiUtil.lheld) {
                int percentError = (GuiUtil.mX - (x + 4)) * 100 / (((x) + width) - (x + 4));
                sl.setValue(MathUtil.roundDouble(percentError * ((sl.getMaxValue() - sl.getMinValue()) / 100.0D) + sl.getMinValue(), sl.getRoundingScale()));
            }
        }

        if (GuiUtil.mouseOver(x + 4, y + height + (boost * height) + 2, (int) (x + ClickGui.snapSub.getValue()), (y + height) + height + (boost * height))) {
            if (ClickGui.snapSlider.getValue() && GuiUtil.lheld) {
                rectAdd = 0;
                sl.setValue(sl.getMinValue());
            }
        }

        if (GuiUtil.mouseOver((int) ((x + 4 + width) - ClickGui.snapSub.getValue()), y + height + (boost * height) + 2, (x + width) - 1, (y + height) + height + (boost * height))) {
            if (ClickGui.snapSlider.getValue() && GuiUtil.lheld) {
                rectAdd = ((x + 4 + width) - (x + 4));
                sl.setValue(sl.getMaxValue());
            }
        }

        GuiScreen.drawRect(x, y + height + (boost * height), x + width, y + height*2 + (boost * height), 0x99202020);
        GuiScreen.drawRect(x + 4, y + height + (boost * height) + 1, (x + width) - 1, (y + height) + height + (boost * height), color);
        GuiScreen.drawRect(x + 4, y + height + (boost * height) + 1, x + 4 + (rectAdd > width - 1 ? (width - 5) : rectAdd), (y + height) + height + (boost * height), Color.GRADIENT ? ColorUtil.rainbow(boost) : Color.COLOR);
        drawTextWithShadow(sl.getName() + " " + sl.getValue(), x + 6, (y + height) + 4 + (boost * height), -1);

        if (sl.hasSubSettings() && !sl.isOpened()) {
            drawText("...", (x + width) - 12, (y + height + 1) + (boost * height), -1);
        }
    }

    private static void drawSubSlider(SubSlider ssl, int x, int y) {
        int color = 0xCC232323;
        int rectAdd = (int) MathHelper.clamp((((x - 2) + width) - (x + 7)) * ((ssl.getValue() - ssl.getMinValue()) / (ssl.getMaxValue() - ssl.getMinValue())), 0.0D, (((x + 7) + width) - (x)));

        if (GuiUtil.mouseOver(x + 8, y + height + (boost * height) + 2, (x + width), (y + height) + height + (boost * height))) {
            color = 0xCC383838;
            if (GuiUtil.lheld) {
                int percentError = (GuiUtil.mX - (x + 8)) * 100 / (((x) + width) - (x + 8));
                ssl.setValue(MathUtil.roundDouble(percentError * ((ssl.getMaxValue() - ssl.getMinValue()) / 100.0D) + ssl.getMinValue(), ssl.getRoundingScale()));
            }
        }

        if (GuiUtil.mouseOver(x + 8, y + height + (boost * height) + 2, (int) (x + ClickGui.snapSub.getValue()), (y + height) + height + (boost * height))) {
            if (ClickGui.snapSlider.getValue() && GuiUtil.lheld) {
                rectAdd = 0;
                ssl.setValue(ssl.getMinValue());
            }
        }

        if (GuiUtil.mouseOver((int) ((x + 8 + width) - ClickGui.snapSub.getValue()), y + height + (boost * height) + 2, (x + width) - 1, (y + height) + height + (boost * height))) {
            if (ClickGui.snapSlider.getValue() && GuiUtil.lheld) {
                rectAdd = ((x + 8 + width) - (x + 8));
                ssl.setValue(ssl.getMaxValue());
            }
        }

        GuiScreen.drawRect(x, y + height + (boost * height), x + width, y + height*2 + (boost * height), 0x99202020);
        GuiScreen.drawRect(x + 8, y + height + (boost * height) + 1, (x + width) - 1, (y + height) + height + (boost * height), color);
        GuiScreen.drawRect(x + 8, y + height + (boost * height) + 1, x + 8 + (rectAdd > width ? (width-8) : rectAdd), (y + height) + height + (boost * height), Color.GRADIENT ? ColorUtil.rainbow(boost) : Color.COLOR);
        drawTextWithShadow(ssl.getName() + " " + ssl.getValue(), x + 10, (y + height) + 4 + (boost * height), -1);
    }

    public static void drawBind(Module m, int key, int x, int y) {
        int color = 0xCC232323;
        if (GuiUtil.mouseOver(x + 4, y + height + (boost * height) + 2, (x + width) - 1, (y + height) + height + (boost * height))) {
            color = 0xCC383838;
            if (GuiUtil.ldown) {
                m.setBinding(true);
            }
        }

        if (m.isBinding() && key != -1 && key != Keyboard.KEY_ESCAPE && key != Keyboard.KEY_DELETE) {
            m.getKeybind().setKeyCode((key == Keyboard.KEY_DELETE || key == Keyboard.KEY_BACK) ? Keyboard.KEY_NONE : key);
            m.setBinding(false);
        }

        if (m.isBinding() && key == Keyboard.KEY_ESCAPE) {
            m.setBinding(false);
        }

        GuiScreen.drawRect(x, y + height + (boost * height), x + width, y + height*2 + (boost * height), 0x99202020);
        GuiScreen.drawRect(x + 4, y + height + (boost * height) + 1, (x + width) - 1, (y + height) + height + (boost * height), color);

        if (!m.isBinding()) {
            drawTextWithShadow("Keybind " + (m.getKeybind().getDisplayName().equalsIgnoreCase("NONE") ? "None" : m.getKeybind().getDisplayName()), x + 7, (y + height) + 4 + (boost * height), -1);
        } else {
            drawTextWithShadow("Listening...", x + 7, (y + height) + 4 + (boost * height), -1);
        }
    }

    @Override
    public void drawHUDModules(List<HUDComponent> modules, int x, int y) {
        boost = 0;
        int color = 0xCC232323;
        for (HUDComponent m : modules) {
            if (GuiUtil.mouseOver(x, y + height + 1 + (boost * height), (x + width) - 1, y + height * 2 + (boost * height))) {
                if (GuiUtil.ldown) {
                    m.toggle();
                }

                if (GuiUtil.rdown) {
                    m.toggleState();
                }
            }

            GuiScreen.drawRect(x, y + height + (boost * height), x + width, y + height * 2 + (boost * height), m.isEnabled() ? (Color.GRADIENT ? ColorUtil.rainbow(boost) : Color.COLOR) : color);
            FontUtil.drawStringWithShadow(m.getName(), x + 4, y + height + 2 + (boost * height), -1);
            boost++;
        }
    }

    public static void drawTextWithShadow(String text, float x, float y, int color) {
        if (ClickGui.font.getValue() == 0)
            Momentum.latoFont.drawStringWithShadow(text, x - 1, y - 2, color);

        if (ClickGui.font.getValue() == 1)
            Momentum.ubuntuFont.drawStringWithShadow(text, x - 1, y - 2, color);

        if (ClickGui.font.getValue() == 2)
            Momentum.verdanaFont.drawStringWithShadow(text, x - 1, y - 2, color);

        if (ClickGui.font.getValue() == 3)
            Momentum.comfortaaFont.drawStringWithShadow(text, x - 1, y - 2, color);

        if (ClickGui.font.getValue() == 4)
            Momentum.comicFont.drawStringWithShadow(text, x - 1, y - 2, color);

        if (ClickGui.font.getValue() == 5)
            mc.fontRenderer.drawStringWithShadow(text, x, y, color);
    }

    public static void drawText(String text, float x, float y, int color) {
        if (ClickGui.font.getValue() == 0)
            Momentum.latoFont.drawString(text, x - 1, y - 2, color);

        if (ClickGui.font.getValue() == 1)
            Momentum.ubuntuFont.drawStringWithShadow(text, x - 1, y - 2, color);

        if (ClickGui.font.getValue() == 2)
            Momentum.verdanaFont.drawString(text, x - 1, y - 2, color);

        if (ClickGui.font.getValue() == 3)
            Momentum.comfortaaFont.drawString(text, x - 1, y - 2, color);

        if (ClickGui.font.getValue() == 4)
            Momentum.comicFont.drawString(text, x - 1, y - 2, color);

        if (ClickGui.font.getValue() == 5)
            mc.fontRenderer.drawString(text, (int) x, (int) y, color);
    }
}
