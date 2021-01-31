package me.linus.momentum.gui.main.hud;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.module.ModuleManager;
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
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		GlStateManager.enableTexture2D();
		HUDWindow.hw.drawHud(mouseX, mouseY);
		GUIUtil.mouseListen(mouseX, mouseY);

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