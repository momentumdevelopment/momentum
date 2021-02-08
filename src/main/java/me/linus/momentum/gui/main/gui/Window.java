package me.linus.momentum.gui.main.gui;

import me.linus.momentum.gui.theme.Theme;
import me.linus.momentum.gui.theme.themes.DefaultTheme;
import me.linus.momentum.managers.AnimationManager;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.Module;
import me.linus.momentum.module.Module.Category;
import me.linus.momentum.managers.ModuleManager;
import me.linus.momentum.module.modules.client.ClickGUI;
import me.linus.momentum.util.render.GUIUtil;
import me.linus.momentum.util.render.builder.Render2DBuilder;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

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
	public int width;
	public int height = 0;

	public boolean ldown;
	public boolean rdown;
	public boolean dragging;
	public boolean opened = false;

	public int currentTheme;

	public int lastmX;
	public int lastmY;
	public String name;
	public Category category;
	public List<Module> modules;
	public static List<Window> windows = new ArrayList<>();
	AnimationManager animationManager = new AnimationManager(200, false);

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
		current.drawTitles(this.name, this.x, this.y);

		Render2DBuilder.prepareScissor(this.x, this.y + 14, this.x + 105, (int) (this.y + 14 + ((this.modules.size() * 14) * animationManager.getAnimationFactor())));
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		current.drawModules(this.modules, this.x, this.y, mouseX, mouseY, partialTicks);
		GL11.glDisable(GL11.GL_SCISSOR_TEST);

		animationManager.updateTime();

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

		if (GUIUtil.mouseOver(x, y, x + current.getThemeWidth(), y + current.getThemeHeight())) {
			opened = !opened;
			animationManager.updateState();
		}
	}

	public void mouseWheelListen() {
		int scrollWheel = Mouse.getDWheel();

		for (Window windows : Window.windows) {
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

	public int getX() {
		return this.x;
	}

	public void setX(int newX) {
		this.x = newX;
	}

	public int getY() {
		return this.y;
	}

	public void setY(int newY) {
		this.y = newY;
	}
}