package me.linus.momentum.gui.main.hud;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.managers.ModuleManager;
import me.linus.momentum.module.modules.client.HUDEditor;
import me.linus.momentum.util.render.GUIUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

import java.io.IOException;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class HUD extends GuiScreen {

	int topRightHeight;
	int topLeftHeight;
	int bottomRightHeight;
	int bottomLeftHeight;

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		GlStateManager.enableTexture2D();
		HUDWindow.hw.drawHud(mouseX, mouseY);
		GUIUtil.mouseListen(mouseX, mouseY);

		/*

		int screenWidth = new ScaledResolution(mc).getScaledWidth();
		int screenHeight = new ScaledResolution(mc).getScaledHeight();

		topRightHeight = 0;
		topLeftHeight = 0;
		bottomRightHeight = 0;
		bottomLeftHeight = 0;

		for (HUDComponent component : HUDComponentManager.getComponents()) {
			switch (component.anchorPoint) {
				case TopRight:
					component.setX(screenWidth - 2 - component.width);
					component.setY(2 + topRightHeight);
					topRightHeight += component.height;
					break;
				case TopLeft:
					component.setX(2);
					component.setY(2 + topLeftHeight);
					topLeftHeight += component.height;
					break;
				case BottomRight:
					component.setX(screenWidth - 2 - component.width);
					component.setY(screenHeight - 2 - bottomRightHeight);
					bottomRightHeight += component.height;
					break;
				case BottomLeft:
					component.setX(2);
					component.setY(screenHeight - 2 - bottomLeftHeight);
					bottomLeftHeight += component.height;
					break;
			}
		}

		 */

		for (HUDComponent component : Momentum.componentManager.getComponents()) {
			GlStateManager.enableTexture2D();

			if (component.isEnabled())
				component.renderInGUI(mouseX, mouseY);
		}
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if (mouseButton == 0) {
			HUDWindow.hw.lclickListen();
			GUIUtil.lclickListen();
		}

		if (mouseButton == 1)
			GUIUtil.rclickListen();

		for (HUDComponent component : Momentum.componentManager.getComponents())
			component.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);

		if (state == 0) {
			HUDWindow.hw.releaseListen();
			GUIUtil.releaseListen();
		}

		for (HUDComponent component : Momentum.componentManager.getComponents())
			component.mouseReleased(mouseX, mouseY, state);
	}
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		ModuleManager.getModuleByName("HUD").disable();

		for (HUDComponent component : Momentum.componentManager.getComponents())
			component.onGuiClosed();
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return HUDEditor.pauseGame.getValue();
	}
}