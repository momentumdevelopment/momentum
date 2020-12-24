package me.linus.momentum.gui.main.hud;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.gui.theme.Theme;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.Module.Category;
import me.linus.momentum.module.modules.client.ClickGUI;
import me.linus.momentum.util.render.GUIUtil;

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
		
		currentTheme = ClickGUI.theme.getValue();
		Theme current = Theme.getTheme(currentTheme);
		current.drawTitles(name, x, y);
		current.drawHUDModules(modules, x, y);
		reset();
	}
	
	private void mouseListen() {
		if (dragging) {
			x = GUIUtil.mX - (lastmX - x);
			y = GUIUtil.mY - (lastmY - y);
		}

		lastmX = GUIUtil.mX;
		lastmY = GUIUtil.mY;
	}
	
	private void reset() {
		ldown = false;
		rdown = false;
	}
	
	public void lclickListen() {
		Theme current = Theme.getTheme(currentTheme);

		if (GUIUtil.mouseOver(x, y, x + current.getThemeWidth(), y + current.getThemeHeight()))
			dragging = true;
	}
	
	public void releaseListen() {
		ldown = false;
		dragging = false;
	}
}