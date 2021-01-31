package me.linus.momentum.gui.main.gui;

import me.linus.momentum.gui.theme.Theme;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.Module;
import me.linus.momentum.module.Module.Category;
import me.linus.momentum.module.ModuleManager;
import me.linus.momentum.module.modules.client.ClickGUI;
import me.linus.momentum.util.render.GUIUtil;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bon & linustouchtips
 * @since 11/16/20
 */

public class Window implements MixinInterface {

	public int x;
	public int y;

	boolean ldown;
	boolean rdown;
	boolean dragging;
	boolean opened = true;

	int currentTheme;

	int lastmX;
	int lastmY;
	final String name;
	final Category category;
	final List<Module> modules;
	public static final List<Window> windows = new ArrayList<>();

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

	public void drawGui(int mouseX, int mouseY, float partialTicks) {
		mouseListen();

		currentTheme = ClickGUI.theme.getValue();
		Theme current = Theme.getTheme(currentTheme);
		current.drawTitles(name, x, y);

		if (opened)
			current.drawModules(modules, x, y, mouseX, mouseY, partialTicks);

		reset();

		if (mc != null && !ClickGUI.allowOverflow.getValue())
			resetOverflow();
	}

	public void resetOverflow() {
		int screenWidth = new ScaledResolution(mc).getScaledWidth();
		int screenHeight = new ScaledResolution(mc).getScaledHeight();

		if (this.x > screenWidth)
			this.x = screenWidth;

		if (this.y > screenHeight)
			this.y = screenHeight;

		if (this.x < 0)
			this.x = 0;

		if (y < 0)
			this.y = 0;
	}

	void mouseListen() {
		if (dragging) {
			x = GUIUtil.mX - (lastmX - x);
			y = GUIUtil.mY - (lastmY - y);
		}

		lastmX = GUIUtil.mX;
		lastmY = GUIUtil.mY;
	}

	void reset() {
		ldown = false;
		rdown = false;
	}

	public void lclickListen(int mouseX, int mouseY, int mouseButton) throws IOException {
		Theme current = Theme.getTheme(currentTheme);

		if (GUIUtil.mouseOver(x, y, x + current.getThemeWidth(), y + current.getThemeHeight()))
			dragging = true;
	}

	public void rclickListen(int mouseX, int mouseY, int mouseButton) throws IOException {
		Theme current = Theme.getTheme(currentTheme);

		if (GUIUtil.mouseOver(x, y, x + current.getThemeWidth(), y + current.getThemeHeight()))
			opened = !opened;
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

	public void releaseListen(int mouseX, int mouseY, int state) {
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