package me.linus.momentum.gui.main;

import me.linus.momentum.gui.util.GuiUtil;
import me.linus.momentum.module.ModuleManager;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

/**
 * @author bon
 * @since 11/16/20
 */

public class GUI extends GuiScreen {
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		for (Window w : Window.windows) {
			w.mouseWheelListen();
			w.drawGui(mouseX, mouseY);
		}

		GuiUtil.mouseListen(mouseX, mouseY);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if(mouseButton == 0) {
			for(Window w : Window.windows) {
				w.lclickListen();
			}
			GuiUtil.lclickListen();
		}
		if(mouseButton == 1) {
			GuiUtil.rclickListen();
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		if(state == 0) {
			for(Window w : Window.windows) {
				w.releaseListen();
			}
			GuiUtil.releaseListen();
		}
	}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		GuiUtil.keyListen(keyCode);
	}
	
	@Override
	public void onGuiClosed() {
		try {
			super.onGuiClosed();
			ModuleManager.getModuleByName("ClickGui").disable();

			mc.entityRenderer.getShaderGroup().deleteShaderGroup();
		} catch (NullPointerException NPE) {

		}
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
