package me.linus.momentum.gui.main;

import java.io.IOException;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.gui.util.GuiUtil;
import me.linus.momentum.module.ModuleManager;
import me.linus.momentum.module.modules.client.HudEditor;
import net.minecraft.client.gui.GuiScreen;

public class HUD extends GuiScreen {
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		HUDWindow.hw.drawHud(mouseX, mouseY);
		GuiUtil.mouseListen(mouseX, mouseY);

		for (HUDComponent c : Momentum.componentManager.getComponents()) {
			if (c.isEnabled())
			c.renderInGui(mouseX, mouseY);
		}
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if (mouseButton == 0) {
			HUDWindow.hw.lclickListen();
			GuiUtil.lclickListen();
		}

		if (mouseButton == 1) {
			GuiUtil.rclickListen();
		}

		for (HUDComponent c : Momentum.componentManager.getComponents()) {
			c.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		if (state == 0) {
			HUDWindow.hw.releaseListen();
			GuiUtil.releaseListen();
		}

		for (HUDComponent c : Momentum.componentManager.getComponents()) {
			c.mouseReleased(mouseX, mouseY, state);
		}
	}
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		ModuleManager.getModuleByClass(HudEditor.class).disable();

		for (HUDComponent c : Momentum.componentManager.getComponents()) {
			c.onGuiClosed();
		}
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
}
