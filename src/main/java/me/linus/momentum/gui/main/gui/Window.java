package me.linus.momentum.gui.main.gui;

import me.linus.momentum.gui.theme.Theme;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.Module;
import me.linus.momentum.module.Module.Category;
import me.linus.momentum.module.ModuleManager;
import me.linus.momentum.module.modules.client.ClickGUI;
import me.linus.momentum.util.render.GUIUtil;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bon & linustouchtips
 * @since 11/16/20
 */

public class Window implements MixinInterface {
	
	public int x;
	public int y;

	private boolean ldown;
	private boolean rdown;
	private boolean dragging;
	
	int currentTheme;
	
	private int lastmX;
	private int lastmY;
	private final String name;
	private final Category category;
	private final List<Module> modules;
	public static final List<Window> windows = new ArrayList<>();
	
	/**
	 * @param name The title of the module tabs
	 * @param x The (top right) x position
	 * @param y The (top right) y position
	 * @param category The category the window represents
	 * 
	 * Maybe at some point add an x and a y modifier to themes so they aren't stuck at
	 * a static value.
	 */
	public Window(String name, int x, int y, Category category) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.category = category;
		this.modules = ModuleManager.getModulesInCategory(category);
	}
	
	public static void initGui() {
		windows.add(new Window(Category.COMBAT.getName(), 18, 22, Category.COMBAT));
		windows.add(new Window(Category.PLAYER.getName(), 128, 22, Category.PLAYER));
		windows.add(new Window(Category.MISC.getName(), 238, 22, Category.MISC));
		windows.add(new Window(Category.MOVEMENT.getName(), 348, 22, Category.MOVEMENT));
		windows.add(new Window(Category.RENDER.getName(), 458, 22, Category.RENDER));
		windows.add(new Window(Category.CLIENT.getName(), 568, 22, Category.CLIENT));
		windows.add(new Window(Category.BOT.getName(), 568, 258, Category.BOT));
	}
	
	public void drawGui(int mouseX, int mouseY) {
		mouseListen();
		
		currentTheme = ClickGUI.theme.getValue();
		Theme current = Theme.getTheme(currentTheme);
		current.drawTitles(name, x, y);
		current.drawModules(modules, x, y);
		reset();
	}
	
	/**
	 * I would ideally like to have the below 4 methods in some other class, but
	 * messing around with static things in constructors doesn't usually work so well.
	 */
	
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

	public void mouseWheelListen() {
		int scrollWheel = Mouse.getDWheel();

		for (final Window windows : Window.windows) {
			if (scrollWheel < 0)
				windows.setY((int) (windows.getY() - ClickGUI.scrollSpeed.getValue()));

			else if (scrollWheel > 0)
				windows.setY((int) (windows.getY() + ClickGUI.scrollSpeed.getValue()));
		}
	}
	
	public void releaseListen() {
		ldown = false;
		dragging = false;
	}

	public int getY() {
		return this.y;
	}

	public void setY(int newY) {
		this.y = newY;
	}
}