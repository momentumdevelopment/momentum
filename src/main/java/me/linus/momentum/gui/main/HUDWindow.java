package me.linus.momentum.gui.main;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.gui.theme.Theme;
import me.linus.momentum.gui.util.GuiUtil;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.Module.Category;
import me.linus.momentum.module.modules.client.ClickGui;

import java.util.List;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class HUDWindow implements MixinInterface {
	
	private int x;
	private int y;
	private final String name;
	private boolean dragging;
	int currentTheme;
	private int lastmX;
	private int lastmY;
	private boolean ldown;
	private boolean rdown;
	
	private final List<HUDComponent> modules;
	
	public HUDWindow(String name, int x, int y) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.modules = Momentum.componentManager.getComponents();
	}
	
	public static HUDWindow hw = new HUDWindow(Category.HUD.getName(), 300, 100);
	
	public void drawHud(int mouseX, int mouseY) {
		mouseListen();
		
		currentTheme = ClickGui.theme.getValue();
		Theme current = Theme.getTheme(currentTheme);
		current.drawTitles(name, x, y);
		current.drawHUDModules(modules, x, y);
		reset();
	}
	
	private void mouseListen() {
		if (dragging) {
			x = GuiUtil.mX - (lastmX - x);
			y = GuiUtil.mY - (lastmY - y);
		}

		lastmX = GuiUtil.mX;
		lastmY = GuiUtil.mY;
	}
	
	private void reset() {
		ldown = false;
		rdown = false;
	}
	
	public void lclickListen() {
		Theme current = Theme.getTheme(currentTheme);
		if (GuiUtil.mouseOver(x, y, x + current.getThemeWidth(), y + current.getThemeHeight())) {
			dragging = true;
		}
	}
	
	public void releaseListen() {
		ldown = false;
		dragging = false;
	}
	
}
