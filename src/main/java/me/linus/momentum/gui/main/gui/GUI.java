package me.linus.momentum.gui.main.gui;

import me.linus.momentum.module.ModuleManager;
import me.linus.momentum.module.modules.client.ClickGUI;
import me.linus.momentum.util.render.GUIUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

import java.io.IOException;

/**
 * @author bon & linustouchtips
 * @since 11/16/20
 */

public class GUI extends GuiScreen {

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		for (Window w : Window.windows) {
			w.mouseWheelListen();
			GlStateManager.enableTexture2D();
			w.drawGui(mouseX, mouseY, partialTicks);
		}

		GUIUtil.mouseListen(mouseX, mouseY);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if (mouseButton == 0) {
			for (Window w : Window.windows)
				w.lclickListen(mouseX, mouseY, mouseButton);

			GUIUtil.lclickListen();
		}

		if (mouseButton == 1) {
			for (Window w : Window.windows)
				w.rclickListen(mouseX, mouseY, mouseButton);

			GUIUtil.rclickListen();
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		if (state == 0) {
			for (Window w : Window.windows)
				w.releaseListen(mouseX, mouseY, state);

			GUIUtil.releaseListen();
		}
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		GUIUtil.keyListen(keyCode);
	}

	@Override
	public void onGuiClosed() {
		try {
			super.onGuiClosed();
			ModuleManager.getModuleByName("ClickGui").disable();

			mc.entityRenderer.getShaderGroup().deleteShaderGroup();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return ClickGUI.pauseGame.getValue();
	}
}
