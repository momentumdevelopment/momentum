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
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.mode.SubMode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.client.MathUtil;
import me.linus.momentum.util.client.ColorUtil;
import me.linus.momentum.util.render.gui.GUIUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;

import java.util.List;

/**
 * @author bon
 * @since 11/18/20
 *
 * More themes can be created by basically copy-pasting this class
 * and then changing colors, rectangles, sizes, and etc.
 */

public class DarkTheme extends Theme implements MixinInterface {
	public static int boost = 0;

	public static String name = "Dark";
	public static int width = 105;
	public static int height = 14;

	public DarkTheme() {
		super(name, width, height);
	}

	private static FontRenderer font = mc.fontRenderer;

	@Override
	public void updateColors() {
		ThemeColor.updateColors();
	}

	@Override
	public void drawTitles(String name, int x, int y) {
		GuiScreen.drawRect(x, y, (x + width), y + height, 0xCC000000);
		GuiScreen.drawRect(x - 1, y - 1, x + width + 1, y, 0xFF202020);
		drawTextWithShadow(name, (x + ((x + width) - x) / 2 - (ModuleManager.getModuleByName("Font").isEnabled() ? Momentum.fontManager.getCustomFont().getStringWidth(name) : font.getStringWidth(name)) / 2), y + 3, -1);
	}

	@Override
	public void drawModules(List<Module> modules, int x, int y, int mouseX, int mouseY, float partialTicks) {
		boost = 0;
		for(Module m : modules) {
			if (GUIUtil.mouseOver(x, y + height + 1 + (boost * height), (x + width) - 1, y + height*2 + (boost * height))) {
				if (GUIUtil.ldown)
					m.toggle();

				if (GUIUtil.rdown) {
					m.toggleState();
				}
			}

			GuiScreen.drawRect(x, y + height + (boost * height), x + width, y + height*2 + (boost * height), 0x99202020);
			GuiScreen.drawRect(x + 1, y + height + (boost * height) + 1, (x + width) - 1, y + height*2 + (boost * height), m.isEnabled() ? (ThemeColor.GRADIENT ? ColorUtil.rainbow(boost) : ThemeColor.COLOR) : 0xCC232323);
			drawTextWithShadow(m.getName(), x + 4, y + height + 4 + (boost * height), -1);

			if (m.hasSettings())
				drawText("...", (x + width) - 12, y + 1 + height + (boost * height), -1);

			if (m.isOpened()) {
				if (m.hasSettings())
					drawDropdown(m, x, y);

				if (!m.hasSettings()) boost++;
				drawBind(m, GUIUtil.keydown, x, y);
			}

			boost++;
		}

		GuiScreen.drawRect(x - 1, y, x, y + height + (boost * height) + 1, 0xFF202020);
		GuiScreen.drawRect(x + width, y, x + width + 1, y + height + (boost * height) + 1, 0xFF202020);
		GuiScreen.drawRect(x - 1, y + height + (boost * height) + 1, x + width + 1, y + height + (boost * height) + 2, 0xFF202020);
		GuiScreen.drawRect(x - 1, y + height + (boost * height), x + width + 1, y + height + (boost * height) + 1, 0x99202020);
	}

	public static void drawDropdown(Module m, int x, int y) {
		for (Setting s : m.getSettings()) {
			boost++;

			if( s instanceof Checkbox) {
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

						GuiScreen.drawRect(x + 4, y + (boost * height) + 1, x + 5, y + height*2 + (boost * height), 0xFF202020);
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

						GuiScreen.drawRect(x + 4, y + (boost * height) + 1, x + 5, y + height*2 + (boost * height), 0xFF202020);
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

						GuiScreen.drawRect(x + 4, y + (boost * height) + 1, x + 5, y + height*2 + (boost * height), 0xFF202020);
					}
				}
			}
		}

		boost++;
	}

	private static void drawCheckbox(Checkbox checkbox, int x, int y) {
		if (GUIUtil.mouseOver(x + 4, y + height + (boost * height) + 2, (x + width) - 1, (y + height) + height + (boost * height))) {
			if (GUIUtil.ldown)
				checkbox.toggleValue();

			if (GUIUtil.rdown)
				checkbox.toggleState();
		}

		GuiScreen.drawRect(x, y + height + (boost * height), x + width, y + height*2 + (boost * height), 0x99202020);
		GuiScreen.drawRect(x + 4, y + height + (boost * height) + 1, (x + width) - 1, (y + height) + height + (boost * height), checkbox.getValue() ? (ThemeColor.GRADIENT ? ColorUtil.rainbow(boost) : ThemeColor.COLOR) : 0xCC232323);
		drawTextWithShadow(checkbox.getName(), x + 7, (y + height) + 4 + (boost * height), -1);

		if (checkbox.hasSubSettings())
			drawText("...", (x + width) - 12, (y + height + 1) + (boost * height), -1);
	}

	private static void drawSubCheckbox(SubCheckbox sc, int x, int y) {
		if (GUIUtil.mouseOver(x + 8, y + height + (boost * height) + 2, (x + width) - 1, (y + height) + height + (boost * height))) {
			if (GUIUtil.ldown)
				sc.toggleValue();
		}

		GuiScreen.drawRect(x, y + height + (boost * height), x + width, y + height*2 + (boost * height), 0x99202020);
		GuiScreen.drawRect(x + 8, y + height + (boost * height) + 1, (x + width) - 1, (y + height) + height + (boost * height), sc.getValue() ? (ThemeColor.GRADIENT ? ColorUtil.rainbow(boost) : ThemeColor.COLOR) : 0xCC232323);
		drawTextWithShadow(sc.getName(), x + 10, (y + height) + 4 + (boost * height), -1);
	}

	private static void drawMode(Mode m, int x, int y) {
		if (GUIUtil.mouseOver(x + 4, y + height + (boost * height) + 2, (x + width) - 1, (y + height) + height + (boost * height))) {
			if (GUIUtil.ldown)
				m.setMode(m.nextMode());

			if (GUIUtil.rdown)
				m.toggleState();
		}

		GuiScreen.drawRect(x, y + height + (boost * height), x + width, y + height*2 + (boost * height), 0x99202020);
		GuiScreen.drawRect(x + 4, y + height + (boost * height) + 1, (x + width) - 1, (y + height) + height + (boost * height), 0xCC232323);
		drawTextWithShadow(m.getName(), x + 7, (y + height) + 4 + (boost * height), -1);
		drawTextWithShadow(m.getMode(m.getValue()), x + (ModuleManager.getModuleByName("Font").isEnabled() ? 10 : 14) + font.getStringWidth(m.getName()), (y + height) + 4 + (boost * height), 0xFF767676);

		if (m.hasSubSettings())
			drawText("...", (x + width) - 12, (y + height + 1) + (boost * height), -1);
	}

	private static void drawSubMode(SubMode sm, int x, int y) {
		if (GUIUtil.mouseOver(x + 8, y + height + (boost * height) + 2, (x + width) - 1, (y + height) + height + (boost * height))) {
			if (GUIUtil.ldown)
				sm.setMode(sm.nextMode());
		}

		GuiScreen.drawRect(x, y + height + (boost * height), x + width, y + height*2 + (boost * height), 0x99202020);
		GuiScreen.drawRect(x + 8, y + height + (boost * height) + 1, (x + width) - 1, (y + height) + height + (boost * height), 0xCC232323);
		drawTextWithShadow(sm.getName(), x + 12, (y + height) + 4 + (boost * height), -1);
		drawTextWithShadow(sm.getMode(sm.getValue()), x + (ModuleManager.getModuleByName("Font").isEnabled() ? 12 : 16) + font.getStringWidth(sm.getName()), (y + height) + 4 + (boost * height), 0xFF767676);
	}

	private static void drawSlider(Slider sl, int x, int y) {
		int rectAdd = (int) MathHelper.clamp((((x - 2) + width) - (x + 3)) * ((sl.getValue() - sl.getMinValue()) / (sl.getMaxValue() - sl.getMinValue())), 0.0D, (((x + 3) + width) - (x)));

		if (GUIUtil.mouseOver(x + 4, y + height + (boost * height) + 2, (x + width), (y + height) + height + (boost * height))) {
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
		GuiScreen.drawRect(x + 4, y + height + (boost * height) + 1, (x + width) - 1, (y + height) + height + (boost * height), 0xCC232323);
		GuiScreen.drawRect(x + 4, y + height + (boost * height) + 1, x + 4 + (rectAdd > width - 1 ? (width - 5) : rectAdd), (y + height) + height + (boost * height), (ThemeColor.GRADIENT ? ColorUtil.rainbow(boost) : ThemeColor.COLOR));
		drawTextWithShadow(sl.getName(), x + 6, (y + height) + 4 + (boost * height), -1);
		drawTextWithShadow(Double.toString(sl.getValue()), x + font.getStringWidth(sl.getName()) + (ModuleManager.getModuleByName("Font").isEnabled() ? 8 : 12), (y + height) + 4 + (boost * height), 0xFF767676);

		if (sl.hasSubSettings())
			drawText("...", (x + width) - 12, (y + height + 1) + (boost * height), -1);
	}

	private static void drawSubSlider(SubSlider ssl, int x, int y) {
		int rectAdd = (int) MathHelper.clamp((((x - 2) + width) - (x + 7)) * ((ssl.getValue() - ssl.getMinValue()) / (ssl.getMaxValue() - ssl.getMinValue())), 0.0D, (((x + 7) + width) - (x)));

		if (GUIUtil.mouseOver(x + 8, y + height + (boost * height) + 2, (x + width), (y + height) + height + (boost * height))) {
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
		GuiScreen.drawRect(x + 8, y + height + (boost * height) + 1, (x + width) - 1, (y + height) + height + (boost * height), 0xCC232323);
		GuiScreen.drawRect(x + 8, y + height + (boost * height) + 1, x + 8 + (rectAdd > width ? (width-8) : rectAdd), (y + height) + height + (boost * height), (ThemeColor.GRADIENT ? ColorUtil.rainbow(boost) : ThemeColor.COLOR));
		drawTextWithShadow(ssl.getName(), x + 10, (y + height) + 4 + (boost * height), -1);
		drawTextWithShadow(Double.toString(ssl.getValue()), x + font.getStringWidth(ssl.getName()) + (ModuleManager.getModuleByName("Font").isEnabled() ? 8 : 12), (y + height) + 4 + (boost * height), 0xFF767676);
	}

	public static void drawBind(Module m, int key, int x, int y) {
		if (GUIUtil.mouseOver(x + 4, y + height + (boost * height) + 2, (x + width) - 1, (y + height) + height + (boost * height))) {
			if (GUIUtil.ldown) {
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
		GuiScreen.drawRect(x + 4, y + height + (boost * height) + 1, (x + width) - 1, (y + height) + height + (boost * height), 0xCC232323);

		if (!m.isBinding()) {
			drawTextWithShadow("Keybind", x + 7, (y + height) + 4 + (boost * height), -1);
			drawTextWithShadow(m.getKeybind().getDisplayName().equalsIgnoreCase("NONE") ? "None" : m.getKeybind().getDisplayName(), x + (ModuleManager.getModuleByName("Font").isEnabled() ? 6 : 10) + font.getStringWidth("Keybind") + 3, (y + height) + 4 + (boost * height), 0xFF767676);
		}

		else
			drawTextWithShadow("Listening...", x + 7, (y + height) + 4 + (boost * height), -1);
	}

	@Override
	public void drawHUDModules(List<HUDComponent> modules, int x, int y) {
		boost = 0;
		for (HUDComponent m : modules) {
			if (GUIUtil.mouseOver(x, y + height + 1 + (boost * height), (x + width) - 1, y + height*2 + (boost * height))) {
				if (GUIUtil.ldown)
					m.toggle();

				if (GUIUtil.rdown)
					m.toggle();
			}

			GuiScreen.drawRect(x, y + height + (boost * height), x + width, y + height*2 + (boost * height), 0x99202020);
			GuiScreen.drawRect(x + 1, y + height + (boost * height) + 1, (x + width) - 1, y + height*2 + (boost * height), m.isEnabled() ? (ThemeColor.GRADIENT ? ColorUtil.rainbow(boost) : ThemeColor.COLOR) : 0xCC232323);
			drawTextWithShadow(m.getName(), x + 4, y + height + 4 + (boost * height), -1);
			boost++;
		}
	}

	@Override
	public void drawConsoleTitle(String name, int left, int top) {

	}

	@Override
	public void drawConsole(int left, int top) {

	}

	public static void drawTextWithShadow(String text, float x, float y, int color) {
		Momentum.fontManager.getCustomFont().drawStringWithShadow(text, x, y, color);
	}

	public static void drawText(String text, float x, float y, int color) {
		Momentum.fontManager.getCustomFont().drawString(text, (int) x, (int)  y, color);
	}
}